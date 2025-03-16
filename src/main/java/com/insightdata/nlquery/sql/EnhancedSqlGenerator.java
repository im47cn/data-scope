package com.insightdata.nlquery.sql;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.metadata.TableRelationship;
import com.insightdata.domain.service.TableRelationshipService;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.preprocess.EntityTag;

import lombok.extern.slf4j.Slf4j;

/**
 * 增强的SQL生成器
 * 支持自动JOIN和更复杂的查询条件
 */
@Slf4j
@Component
public class EnhancedSqlGenerator extends DefaultSqlGenerator {

    @Autowired
    private TableRelationshipService tableRelationshipService;

    @Override
    public SqlGenerationResult generateSql(List<EntityTag> entities, QueryIntent queryIntent, SchemaInfo schemaInfo) {
        // 获取数据源ID
        Long dataSourceId = schemaInfo.getDataSourceId();
        
        // 调用父类方法生成基本SQL
        SqlGenerationResult result = super.generateSql(entities, queryIntent, schemaInfo);
        
        // 如果生成失败，直接返回
        if (result.getConfidence() == 0.0) {
            return result;
        }
        
        // 提取表名
        List<String> tableNames = extractTableNames(result.getSql());
        
        // 如果只有一个表，不需要JOIN
        if (tableNames.size() <= 1) {
            return result;
        }
        
        try {
            // 获取表关系
            List<TableRelationship> relationships = tableRelationshipService.getTableRelationships(dataSourceId, tableNames);
            
            // 如果没有表关系，使用原始SQL
            if (relationships.isEmpty()) {
                return result;
            }
            
            // 重新生成带JOIN的SQL
            String enhancedSql = enhanceSqlWithJoins(result.getSql(), tableNames, relationships);
            
            // 更新结果
            result.setSql(enhancedSql);
            
            // 添加解释
            List<String> explanations = result.getExplanations();
            explanations.add("自动添加JOIN语句，基于表关系");
            result.setExplanations(explanations);
            
            // 增加置信度
            result.setConfidence(Math.min(1.0, result.getConfidence() + 0.1));
            
        } catch (Exception e) {
            log.error("增强SQL时发生错误", e);
            
            // 添加解释
            List<String> explanations = result.getExplanations();
            explanations.add("尝试增强SQL时发生错误: " + e.getMessage());
            result.setExplanations(explanations);
        }
        
        return result;
    }
    
    /**
     * 从SQL中提取表名
     */
    private List<String> extractTableNames(String sql) {
        List<String> tableNames = new ArrayList<>();
        
        // 简单实现，查找FROM和WHERE之间的表名
        int fromIndex = sql.toUpperCase().indexOf(" FROM ");
        if (fromIndex == -1) {
            return tableNames;
        }
        
        int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
        int groupByIndex = sql.toUpperCase().indexOf(" GROUP BY ");
        int orderByIndex = sql.toUpperCase().indexOf(" ORDER BY ");
        int limitIndex = sql.toUpperCase().indexOf(" LIMIT ");
        
        int endIndex = sql.length();
        if (whereIndex != -1) {
            endIndex = whereIndex;
        } else if (groupByIndex != -1) {
            endIndex = groupByIndex;
        } else if (orderByIndex != -1) {
            endIndex = orderByIndex;
        } else if (limitIndex != -1) {
            endIndex = limitIndex;
        }
        
        String fromClause = sql.substring(fromIndex + 6, endIndex).trim();
        String[] tables = fromClause.split(",");
        
        for (String table : tables) {
            tableNames.add(table.trim());
        }
        
        return tableNames;
    }
    
    /**
     * 使用JOIN增强SQL
     */
    private String enhanceSqlWithJoins(String sql, List<String> tableNames, List<TableRelationship> relationships) {
        // 替换FROM子句
        int fromIndex = sql.toUpperCase().indexOf(" FROM ");
        if (fromIndex == -1) {
            return sql;
        }
        
        int whereIndex = sql.toUpperCase().indexOf(" WHERE ");
        int groupByIndex = sql.toUpperCase().indexOf(" GROUP BY ");
        int orderByIndex = sql.toUpperCase().indexOf(" ORDER BY ");
        int limitIndex = sql.toUpperCase().indexOf(" LIMIT ");
        
        int endIndex = sql.length();
        if (whereIndex != -1) {
            endIndex = whereIndex;
        } else if (groupByIndex != -1) {
            endIndex = groupByIndex;
        } else if (orderByIndex != -1) {
            endIndex = orderByIndex;
        } else if (limitIndex != -1) {
            endIndex = limitIndex;
        }
        
        // 构建新的FROM子句
        StringBuilder fromClause = new StringBuilder();
        fromClause.append(" FROM ");
        
        // 添加主表
        String mainTable = tableNames.get(0);
        fromClause.append(mainTable);
        
        // 已处理的表
        Set<String> processedTables = new HashSet<>();
        processedTables.add(mainTable);
        
        // 添加JOIN
        for (int i = 1; i < tableNames.size(); i++) {
            String table = tableNames.get(i);
            
            // 查找与已处理表的关系
            boolean joined = false;
            for (TableRelationship relationship : relationships) {
                if ((relationship.getSourceTable().equals(table) && processedTables.contains(relationship.getTargetTable())) ||
                    (relationship.getTargetTable().equals(table) && processedTables.contains(relationship.getSourceTable()))) {
                    
                    // 构建JOIN语句
                    String sourceTable = relationship.getSourceTable();
                    String sourceColumn = relationship.getSourceColumn();
                    String targetTable = relationship.getTargetTable();
                    String targetColumn = relationship.getTargetColumn();
                    
                    fromClause.append(" JOIN ")
                            .append(table)
                            .append(" ON ");
                    
                    if (sourceTable.equals(table)) {
                        fromClause.append(sourceTable).append(".").append(sourceColumn)
                                .append(" = ")
                                .append(targetTable).append(".").append(targetColumn);
                    } else {
                        fromClause.append(targetTable).append(".").append(targetColumn)
                                .append(" = ")
                                .append(sourceTable).append(".").append(sourceColumn);
                    }
                    
                    joined = true;
                    processedTables.add(table);
                    break;
                }
            }
            
            // 如果没有找到关系，使用交叉连接
            if (!joined) {
                fromClause.append(" CROSS JOIN ").append(table);
                processedTables.add(table);
            }
        }
        
        // 替换FROM子句
        String result = sql.substring(0, fromIndex) + fromClause.toString();
        if (endIndex < sql.length()) {
            result += sql.substring(endIndex);
        }
        
        return result;
    }
    
    /**
     * 重写构建FROM子句方法
     */
    @Override
    protected void buildFromClause(StringBuilder sqlBuilder, List<String> tableNames) {
        // 只添加第一个表，JOIN部分在generateSql方法中处理
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(tableNames.get(0));
    }
}