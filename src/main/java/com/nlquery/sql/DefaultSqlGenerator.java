package com.nlquery.sql;

import com.domain.model.metadata.ColumnInfo;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.nlquery.entity.EntityTag;
import com.nlquery.entity.EntityType;
import com.nlquery.intent.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SQL生成器的默认实现
 */
@Component
public class DefaultSqlGenerator implements SqlGenerator {

    @Override
    public SqlGenerationResult generateSql(List<EntityTag> entities, QueryIntent queryIntent, SchemaInfo schemaInfo) {
        SqlGenerationResult result = new SqlGenerationResult();
        List<String> explanations = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        
        try {
            // 提取表实体
            List<EntityTag> tableEntities = entities.stream()
                    .filter(e -> e.getType() == EntityType.TABLE)
                    .collect(Collectors.toList());
            
            // 提取列实体
            List<EntityTag> columnEntities = entities.stream()
                    .filter(e -> e.getType() == EntityType.COLUMN)
                    .collect(Collectors.toList());
            
            // 提取条件实体
            List<EntityTag> conditionEntities = entities.stream()
                    .filter(e -> e.getType() == EntityType.CONDITION ||
                                e.getType() == EntityType.OPERATOR ||
                                e.getType() == EntityType.VALUE ||
                                e.getType() == EntityType.NUMBER ||
                                e.getType() == EntityType.STRING ||
                                e.getType() == EntityType.DATETIME ||
                                e.getType() == EntityType.BOOLEAN)
                    .collect(Collectors.toList());
            
            // 验证表实体
            if (tableEntities.isEmpty()) {
                explanations.add("未能识别到表名");
                result.setConfidence(0.0);
                result.setExplanations(explanations);
                return result;
            }
            
            // 验证和解析表名
            List<String> tableNames = new ArrayList<>();
            for (EntityTag tableEntity : tableEntities) {
                String tableName = resolveTableName(tableEntity.getValue(), schemaInfo);
                if (tableName != null) {
                    tableNames.add(tableName);
                    explanations.add("识别到表: " + tableName);
                } else {
                    explanations.add("无法解析表名: " + tableEntity.getValue());
                }
            }
            
            if (tableNames.isEmpty()) {
                explanations.add("无法解析任何表名");
                result.setConfidence(0.0);
                result.setExplanations(explanations);
                return result;
            }
            
            // 验证和解析列名
            List<String> columnNames = new ArrayList<>();
            for (EntityTag columnEntity : columnEntities) {
                String columnName = resolveColumnName(columnEntity.getValue(), tableNames, schemaInfo);
                if (columnName != null) {
                    columnNames.add(columnName);
                    explanations.add("识别到列: " + columnName);
                } else {
                    explanations.add("无法解析列名: " + columnEntity.getValue());
                }
            }
            
            // 如果没有指定列，默认使用*
            if (columnNames.isEmpty()) {
                columnNames.add("*");
                explanations.add("未指定列，使用所有列");
            }
            
            // 构建SQL查询
            StringBuilder sqlBuilder = new StringBuilder();
            
            // 构建SELECT子句
            buildSelectClause(sqlBuilder, columnNames, queryIntent.getQueryType());
            
            // 构建FROM子句
            buildFromClause(sqlBuilder, tableNames);
            
            // 构建WHERE子句
            int paramIndex = 1;
            if (!conditionEntities.isEmpty()) {
                sqlBuilder.append(" WHERE ");
                paramIndex = buildWhereClause(sqlBuilder, conditionEntities, parameters, paramIndex, schemaInfo, tableNames);
            }
            
            // 构建GROUP BY子句
            if (queryIntent.getQueryType() == QueryType.GROUP) {
                buildGroupByClause(sqlBuilder, columnNames);
            }
            
            // 构建ORDER BY子句
            if (queryIntent.getSortRequirements() != null && !queryIntent.getSortRequirements().isEmpty()) {
                buildOrderByClause(sqlBuilder, queryIntent.getSortRequirements(), tableNames, schemaInfo);
            }
            
            // 构建LIMIT子句
            if (queryIntent.getLimitRequirement() != null && 
                queryIntent.getLimitRequirement().getLimitType() != LimitType.NONE) {
                buildLimitClause(sqlBuilder, queryIntent.getLimitRequirement());
            }
            
            // 设置结果
            result.setSql(sqlBuilder.toString());
            result.setParameters(parameters);
            result.setConfidence(calculateConfidence(entities, queryIntent));
            result.setExplanations(explanations);
            
            // 生成备选SQL
            List<String> alternativeSqls = generateAlternativeSqls(result.getSql(), tableNames, columnNames, queryIntent);
            result.setAlternativeSqls(alternativeSqls);
            
        } catch (Exception e) {
            explanations.add("生成SQL时发生错误: " + e.getMessage());
            result.setConfidence(0.0);
            result.setExplanations(explanations);
        }
        
        return result;
    }
    
    /**
     * 解析表名
     */
    private String resolveTableName(String tableName, SchemaInfo schemaInfo) {
        // 直接匹配
        for (TableInfo tableInfo : schemaInfo.getTables()) {
            if (tableInfo.getName().equalsIgnoreCase(tableName)) {
                return tableInfo.getName();
            }
        }
        
        // 模糊匹配
        for (TableInfo tableInfo : schemaInfo.getTables()) {
            if (tableInfo.getName().toLowerCase().contains(tableName.toLowerCase()) ||
                tableName.toLowerCase().contains(tableInfo.getName().toLowerCase())) {
                return tableInfo.getName();
            }
        }
        
        return null;
    }
    
    /**
     * 解析列名
     */
    private String resolveColumnName(String columnName, List<String> tableNames, SchemaInfo schemaInfo) {
        // 检查是否包含表名前缀
        if (columnName.contains(".")) {
            String[] parts = columnName.split("\\.");
            String tableName = parts[0];
            String colName = parts[1];
            
            // 验证表名
            if (!tableNames.contains(tableName)) {
                return null;
            }
            
            // 验证列名
            for (TableInfo tableInfo : schemaInfo.getTables()) {
                if (tableInfo.getName().equalsIgnoreCase(tableName)) {
                    for (ColumnInfo columnInfo : tableInfo.getColumns()) {
                        if (columnInfo.getName().equalsIgnoreCase(colName)) {
                            return tableName + "." + columnInfo.getName();
                        }
                    }
                }
            }
        } else {
            // 在所有表中查找列
            for (String tableName : tableNames) {
                for (TableInfo tableInfo : schemaInfo.getTables()) {
                    if (tableInfo.getName().equalsIgnoreCase(tableName)) {
                        for (ColumnInfo columnInfo : tableInfo.getColumns()) {
                            if (columnInfo.getName().equalsIgnoreCase(columnName)) {
                                return tableName + "." + columnInfo.getName();
                            }
                        }
                    }
                }
            }
            
            // 模糊匹配
            for (String tableName : tableNames) {
                for (TableInfo tableInfo : schemaInfo.getTables()) {
                    if (tableInfo.getName().equalsIgnoreCase(tableName)) {
                        for (ColumnInfo columnInfo : tableInfo.getColumns()) {
                            if (columnInfo.getName().toLowerCase().contains(columnName.toLowerCase()) ||
                                columnName.toLowerCase().contains(columnInfo.getName().toLowerCase())) {
                                return tableName + "." + columnInfo.getName();
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    /**
     * 构建SELECT子句
     */
    private void buildSelectClause(StringBuilder sqlBuilder, List<String> columnNames, QueryType queryType) {
        sqlBuilder.append("SELECT ");
        
        if (queryType == QueryType.COUNT) {
            sqlBuilder.append("COUNT(");
            if (columnNames.size() == 1 && !columnNames.get(0).equals("*")) {
                sqlBuilder.append(columnNames.get(0));
            } else {
                sqlBuilder.append("*");
            }
            sqlBuilder.append(")");
        } else if (queryType == QueryType.SUM) {
            sqlBuilder.append("SUM(");
            if (columnNames.size() >= 1) {
                sqlBuilder.append(columnNames.get(0));
            } else {
                sqlBuilder.append("0");
            }
            sqlBuilder.append(")");
        } else if (queryType == QueryType.AVG) {
            sqlBuilder.append("AVG(");
            if (columnNames.size() >= 1) {
                sqlBuilder.append(columnNames.get(0));
            } else {
                sqlBuilder.append("0");
            }
            sqlBuilder.append(")");
        } else if (queryType == QueryType.MAX) {
            sqlBuilder.append("MAX(");
            if (columnNames.size() >= 1) {
                sqlBuilder.append(columnNames.get(0));
            } else {
                sqlBuilder.append("0");
            }
            sqlBuilder.append(")");
        } else if (queryType == QueryType.MIN) {
            sqlBuilder.append("MIN(");
            if (columnNames.size() >= 1) {
                sqlBuilder.append(columnNames.get(0));
            } else {
                sqlBuilder.append("0");
            }
            sqlBuilder.append(")");
        } else {
            sqlBuilder.append(String.join(", ", columnNames));
        }
    }
    
    /**
     * 构建FROM子句
     */
    protected void buildFromClause(StringBuilder sqlBuilder, List<String> tableNames) {
        sqlBuilder.append(" FROM ");
        sqlBuilder.append(String.join(", ", tableNames));
    }
    
    /**
     * 构建WHERE子句
     */
    private int buildWhereClause(StringBuilder sqlBuilder, List<EntityTag> conditionEntities, 
                              Map<String, Object> parameters, int paramIndex, 
                              SchemaInfo schemaInfo, List<String> tableNames) {
        // 简单实现，实际应用中需要更复杂的逻辑
        for (int i = 0; i < conditionEntities.size(); i++) {
            EntityTag entity = conditionEntities.get(i);
            
            if (entity.getType() == EntityType.COLUMN) {
                String columnName = resolveColumnName(entity.getValue(), tableNames, schemaInfo);
                if (columnName != null) {
                    sqlBuilder.append(columnName);
                } else {
                    sqlBuilder.append(entity.getValue());
                }
            } else if (entity.getType() == EntityType.OPERATOR) {
                String operator = mapOperator(entity.getValue());
                sqlBuilder.append(" ").append(operator).append(" ");
            } else if (entity.getType() == EntityType.VALUE ||
                      entity.getType() == EntityType.NUMBER ||
                      entity.getType() == EntityType.STRING ||
                      entity.getType() == EntityType.DATETIME ||
                      entity.getType() == EntityType.BOOLEAN) {
                String paramName = "p" + paramIndex;
                sqlBuilder.append("?");
                parameters.put(paramName, parseValue(entity));
                paramIndex++;
            } else if (entity.getType() == EntityType.CONDITION) {
                if (entity.getValue().equalsIgnoreCase("AND") || 
                    entity.getValue().equalsIgnoreCase("OR")) {
                    sqlBuilder.append(" ").append(entity.getValue().toUpperCase()).append(" ");
                }
            }
        }
        
        return paramIndex;
    }
    
    /**
     * 映射操作符
     */
    private String mapOperator(String operator) {
        switch (operator.toLowerCase()) {
            case "等于":
            case "是":
                return "=";
            case "不等于":
            case "不是":
                return "!=";
            case "大于":
                return ">";
            case "小于":
                return "<";
            case "大于等于":
                return ">=";
            case "小于等于":
                return "<=";
            case "包含":
            case "like":
                return "LIKE";
            case "不包含":
            case "not like":
                return "NOT LIKE";
            case "在":
            case "in":
                return "IN";
            case "不在":
            case "not in":
                return "NOT IN";
            case "为空":
            case "is null":
                return "IS NULL";
            case "不为空":
            case "is not null":
                return "IS NOT NULL";
            default:
                return operator;
        }
    }
    
    /**
     * 解析值
     */
    private Object parseValue(EntityTag entity) {
        if (entity.getType() == EntityType.NUMBER) {
            try {
                return Double.parseDouble(entity.getValue());
            } catch (NumberFormatException e) {
                return entity.getValue();
            }
        } else if (entity.getType() == EntityType.BOOLEAN) {
            return Boolean.parseBoolean(entity.getValue());
        } else if (entity.getType() == EntityType.DATETIME) {
            // 简单实现，实际应用中需要更复杂的日期解析逻辑
            return entity.getValue();
        } else {
            return entity.getValue();
        }
    }
    
    /**
     * 构建GROUP BY子句
     */
    private void buildGroupByClause(StringBuilder sqlBuilder, List<String> columnNames) {
        if (columnNames.size() > 1 && !columnNames.get(0).equals("*")) {
            sqlBuilder.append(" GROUP BY ");
            sqlBuilder.append(columnNames.get(0));
        }
    }
    
    /**
     * 构建ORDER BY子句
     */
    private void buildOrderByClause(StringBuilder sqlBuilder, List<SortRequirement> sortRequirements, 
                                 List<String> tableNames, SchemaInfo schemaInfo) {
        sqlBuilder.append(" ORDER BY ");
        
        List<String> orderByItems = new ArrayList<>();
        for (SortRequirement requirement : sortRequirements) {
            String columnName = resolveColumnName(requirement.getField(), tableNames, schemaInfo);
            if (columnName != null) {
                orderByItems.add(columnName + " " + 
                                (requirement.getDirection() == SortRequirement.SortDirection.ASC ? "ASC" : "DESC"));
            } else {
                orderByItems.add(requirement.getField() + " " + 
                                (requirement.getDirection() == SortRequirement.SortDirection.ASC ? "ASC" : "DESC"));
            }
        }
        
        sqlBuilder.append(String.join(", ", orderByItems));
    }
    
    /**
     * 构建LIMIT子句
     */
    private void buildLimitClause(StringBuilder sqlBuilder, LimitRequirement limitRequirement) {
        if (limitRequirement.getLimitType() == LimitType.TOP_N) {
            sqlBuilder.append(" LIMIT ").append(limitRequirement.getLimitValue());
        }
    }
    
    /**
     * 计算置信度
     */
    private double calculateConfidence(List<EntityTag> entities, QueryIntent queryIntent) {
        // 简单实现，实际应用中需要更复杂的置信度计算逻辑
        double entityConfidence = entities.stream()
                .mapToDouble(EntityTag::getConfidence)
                .average()
                .orElse(0.0);
        
        double intentConfidence = queryIntent.getConfidence();
        
        return (entityConfidence + intentConfidence) / 2.0;
    }
    
    /**
     * 生成备选SQL
     */
    private List<String> generateAlternativeSqls(String mainSql, List<String> tableNames, 
                                             List<String> columnNames, QueryIntent queryIntent) {
        // 简单实现，实际应用中可以生成多个备选SQL
        List<String> alternativeSqls = new ArrayList<>();
        
        // 如果主SQL使用了COUNT，可以提供一个不使用COUNT的备选SQL
        if (queryIntent.getQueryType() == QueryType.COUNT) {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("SELECT ");
            sqlBuilder.append(String.join(", ", columnNames));
            sqlBuilder.append(" FROM ");
            sqlBuilder.append(String.join(", ", tableNames));
            
            alternativeSqls.add(sqlBuilder.toString());
        }
        
        return alternativeSqls;
    }
}