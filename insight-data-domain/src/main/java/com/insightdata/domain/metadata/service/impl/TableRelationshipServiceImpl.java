package com.insightdata.domain.metadata.service.impl;

import com.insightdata.domain.datasource.model.ColumnInfo;
import com.insightdata.domain.datasource.model.ForeignKeyInfo;
import com.insightdata.domain.datasource.model.SchemaInfo;
import com.insightdata.domain.datasource.model.TableInfo;
import com.insightdata.domain.metadata.model.*;
import com.insightdata.domain.metadata.model.TableRelationship.RelationshipSource;
import com.insightdata.domain.metadata.model.TableRelationship.RelationshipType;
import com.insightdata.domain.query.model.QueryHistory;
import com.insightdata.domain.lowcode.repository.TableRelationshipRepository;
import com.insightdata.domain.metadata.service.TableRelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表关系服务实现类
 */
@Slf4j
@Service
public class TableRelationshipServiceImpl implements TableRelationshipService {

    @Autowired
    private TableRelationshipRepository tableRelationshipRepository;

    @Override
    public TableRelationship findById(String id) {
        return tableRelationshipRepository.findById(id).get();
    }

    @Override
    public List<TableRelationship> getAllTableRelationships(String dataSourceId) {
        return tableRelationshipRepository.findByDataSourceId(dataSourceId);
    }

    @Override
    public List<TableRelationship> getTableRelationships(String dataSourceId, List<String> tableNames) {
        List<TableRelationship> allRelationships = tableRelationshipRepository.findByDataSourceId(dataSourceId);

        return allRelationships.stream()
                .filter(r -> tableNames.contains(r.getSourceTableName()) || tableNames.contains(r.getTargetTableName()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TableRelationship> getTableRelationships(String dataSourceId, String tableName) {
        return tableRelationshipRepository.findByDataSourceIdAndTable(dataSourceId, tableName);
    }

    @Override
    @Transactional
    public List<TableRelationship> learnFromMetadata(String dataSourceId, SchemaInfo schemaInfo) {
        List<TableRelationship> relationships = new ArrayList<>();

        // 遍历所有表
        for (TableInfo tableInfo : schemaInfo.getTables()) {
            // 处理外键关系
            for (ForeignKeyInfo fkInfo : tableInfo.getForeignKeys()) {
                TableRelationship relationship = TableRelationship.builder()
                        .dataSourceId(dataSourceId)
                        .sourceTableName(tableInfo.getName())
                        .sourceColumnNames(fkInfo.getSourceColumnNamesAsString()) // 使用字符串形式，逗号分隔
                        .targetTableName(fkInfo.getTargetTable())
                        .targetColumnNames(fkInfo.getTargetColumnNamesAsString()) // 使用字符串形式，逗号分隔
                        .relationType(RelationshipType.MANY_TO_ONE) // 默认为多对一
                        .relationSource(RelationshipSource.METADATA)
                        .confidence(1.0) // 元数据来源的权重最高
                        .frequency(1)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build();

                relationships.add(relationship);
            }

            // 处理列名相似性
            for (TableInfo otherTable : schemaInfo.getTables()) {
                if (tableInfo.getName().equals(otherTable.getName())) {
                    continue;
                }

                // 查找列名相同的列
                for (ColumnInfo column : tableInfo.getColumns()) {
                    for (ColumnInfo otherColumn : otherTable.getColumns()) {
                        if (column.getName().equalsIgnoreCase(otherColumn.getName()) &&
                                column.getType().equalsIgnoreCase(otherColumn.getType())) {

                            // 检查是否已经存在外键关系
                            boolean existsForeignKey = false;
                            for (ForeignKeyInfo fkInfo : tableInfo.getForeignKeys()) {
                                if (fkInfo.getTargetTable().equals(otherTable.getName())) {
                                    boolean sourceColumnMatch = fkInfo.getSourceColumnNames().stream()
                                            .anyMatch(c -> c.equals(column.getName()));
                                    boolean targetColumnMatch = fkInfo.getTargetColumnNames().stream()
                                            .anyMatch(c -> c.equals(otherColumn.getName()));
                                    if (sourceColumnMatch && targetColumnMatch) {
                                        existsForeignKey = true;
                                        break;
                                    }
                                }
                            }

                            if (!existsForeignKey) {
                                TableRelationship relationship = TableRelationship.builder()
                                        .dataSourceId(dataSourceId)
                                        .sourceTableName(tableInfo.getName())
                                        .sourceColumnNames(column.getName())
                                        .targetTableName(otherTable.getName())
                                        .targetColumnNames(otherColumn.getName())
                                        .relationType(RelationshipType.MANY_TO_MANY) // 默认为多对多
                                        .relationSource(RelationshipSource.METADATA)
                                        .confidence(0.7) // 列名相似性的权重较低
                                        .frequency(1)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build();

                                relationships.add(relationship);
                            }
                        }
                    }
                }
            }
        }

        // 保存关系
        for (TableRelationship relationship : relationships) {
            tableRelationshipRepository.save(relationship);
        }

        return relationships;
    }

    @Override
    @Transactional
    public List<TableRelationship> learnFromQueryHistory(String dataSourceId, List<QueryHistory> queryHistories) {
        List<TableRelationship> relationships = new ArrayList<>();
        Map<String, TableRelationship> relationshipMap = new HashMap<>();

        for (QueryHistory history : queryHistories) {
            String sql = history.getExecutedSql().toLowerCase();

            // 分析JOIN语句
            analyzeJoins(dataSourceId, sql, relationshipMap);

            // 分析WHERE条件
            analyzeWhereConditions(dataSourceId, sql, relationshipMap);
        }

        // 保存关系
        for (TableRelationship relationship : relationshipMap.values()) {
            tableRelationshipRepository.save(relationship);
            relationships.add(relationship);
        }

        return relationships;
    }

    /**
     * 分析JOIN语句
     */
    private void analyzeJoins(String dataSourceId, String sql, Map<String, TableRelationship> relationshipMap) {
        // 简单实现，实际应用中需要更复杂的SQL解析
        String[] parts = sql.split("\\s+");

        for (int i = 0; i < parts.length - 3; i++) {
            if (parts[i].equals("join") && i + 3 < parts.length && parts[i + 2].equals("on")) {
                String joinTable = parts[i + 1];
                String onCondition = parts[i + 3];

                // 解析ON条件
                if (onCondition.contains("=")) {
                    String[] columns = onCondition.split("=");
                    if (columns.length == 2) {
                        String leftPart = columns[0].trim();
                        String rightPart = columns[1].trim();

                        if (leftPart.contains(".") && rightPart.contains(".")) {
                            String[] leftParts = leftPart.split("\\.");
                            String[] rightParts = rightPart.split("\\.");

                            if (leftParts.length == 2 && rightParts.length == 2) {
                                String leftTable = leftParts[0];
                                String leftColumn = leftParts[1];
                                String rightTable = rightParts[0];
                                String rightColumn = rightParts[1];

                                // 创建关系
                                String key = leftTable + "." + leftColumn + "-" + rightTable + "." + rightColumn;

                                if (!relationshipMap.containsKey(key)) {
                                    TableRelationship relationship = TableRelationship.builder()
                                            .dataSourceId(dataSourceId)
                                            .sourceTableName(leftTable)
                                            .sourceColumnNames(leftColumn)
                                            .targetTableName(rightTable)
                                            .targetColumnNames(rightColumn)
                                            .relationType(RelationshipType.MANY_TO_ONE) // 默认为多对一
                                            .relationSource(RelationshipSource.LEARNING)
                                            .confidence(0.8) // 查询历史的权重较高
                                            .frequency(1)
                                            .createdAt(LocalDateTime.now())
                                            .updatedAt(LocalDateTime.now())
                                            .build();

                                    relationshipMap.put(key, relationship);
                                } else {
                                    // 更新频率
                                    TableRelationship relationship = relationshipMap.get(key);
                                    relationship.setFrequency(relationship.getFrequency() + 1);
                                    relationship.setUpdatedAt(LocalDateTime.now());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 分析WHERE条件
     */
    private void analyzeWhereConditions(String dataSourceId, String sql, Map<String, TableRelationship> relationshipMap) {
        // 简单实现，实际应用中需要更复杂的SQL解析
        int whereIndex = sql.indexOf("where");
        if (whereIndex == -1) {
            return;
        }

        String whereClause = sql.substring(whereIndex + 5);
        String[] conditions = whereClause.split("and|or");

        for (String condition : conditions) {
            if (condition.contains("=")) {
                String[] parts = condition.split("=");
                if (parts.length == 2) {
                    String leftPart = parts[0].trim();
                    String rightPart = parts[1].trim();

                    if (leftPart.contains(".") && rightPart.contains(".")) {
                        String[] leftParts = leftPart.split("\\.");
                        String[] rightParts = rightPart.split("\\.");

                        if (leftParts.length == 2 && rightParts.length == 2) {
                            String leftTable = leftParts[0];
                            String leftColumn = leftParts[1];
                            String rightTable = rightParts[0];
                            String rightColumn = rightParts[1];

                            // 创建关系
                            String key = leftTable + "." + leftColumn + "-" + rightTable + "." + rightColumn;

                            if (!relationshipMap.containsKey(key)) {
                                TableRelationship relationship = TableRelationship.builder()
                                        .dataSourceId(dataSourceId)
                                        .sourceTableName(leftTable)
                                        .sourceColumnNames(leftColumn)
                                        .targetTableName(rightTable)
                                        .targetColumnNames(rightColumn)
                                        .relationType(RelationshipType.MANY_TO_ONE) // 默认为多对一
                                        .relationSource(RelationshipSource.LEARNING)
                                        .confidence(0.7) // WHERE条件的权重略低于JOIN
                                        .frequency(1)
                                        .createdAt(LocalDateTime.now())
                                        .updatedAt(LocalDateTime.now())
                                        .build();

                                relationshipMap.put(key, relationship);
                            } else {
                                // 更新频率
                                TableRelationship relationship = relationshipMap.get(key);
                                relationship.setFrequency(relationship.getFrequency() + 1);
                                relationship.setUpdatedAt(LocalDateTime.now());
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    @Transactional
    public TableRelationship learnFromUserFeedback(String dataSourceId, String sourceTable, String sourceColumn,
                                                   String targetTable, String targetColumn, RelationshipType type) {
        // 检查是否已存在
        List<TableRelationship> existingRelationships = tableRelationshipRepository.findByDataSourceIdAndTables(
                dataSourceId, sourceTable, targetTable);

        for (TableRelationship relationship : existingRelationships) {
            if (relationship.getSourceColumnNames().equals(sourceColumn) &&
                    relationship.getTargetColumnNames().equals(targetColumn)) {

                // 更新类型和权重
                relationship.setRelationType(type);
                relationship.setConfidence(1.0); // 用户反馈的权重最高
                relationship.setRelationSource(RelationshipSource.USER_FEEDBACK);
                relationship.setFrequency(relationship.getFrequency() + 1);
                relationship.setUpdatedAt(LocalDateTime.now());

                return tableRelationshipRepository.save(relationship);
            }
        }

        // 创建新关系
        TableRelationship relationship = TableRelationship.builder()
                .dataSourceId(dataSourceId)
                .sourceTableName(sourceTable)
                .sourceColumnNames(sourceColumn)
                .targetTableName(targetTable)
                .targetColumnNames(targetColumn)
                .relationType(type)
                .relationSource(RelationshipSource.USER_FEEDBACK)
                .confidence(1.0) // 用户反馈的权重最高
                .frequency(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return tableRelationshipRepository.save(relationship);
    }

    @Override
    @Transactional
    public TableRelationship saveTableRelationship(TableRelationship relationship) {
        return tableRelationshipRepository.save(relationship);
    }

    @Override
    @Transactional
    public void deleteTableRelationship(String relationshipId) {
        tableRelationshipRepository.deleteById(relationshipId);
    }

    @Override
    @Transactional
    public TableRelationship updateRelationshipWeight(String relationshipId, double weightDelta) {
        TableRelationship relationship = tableRelationshipRepository.findById(relationshipId).get();
        if (relationship != null) {
            double newWeight = relationship.getConfidence() + weightDelta;
            // 确保权重在0-1之间
            newWeight = Math.max(0, Math.min(1, newWeight));

            relationship.setConfidence(newWeight);
            relationship.setFrequency(relationship.getFrequency() + 1);
            relationship.setUpdatedAt(LocalDateTime.now());

            return tableRelationshipRepository.save(relationship);
        }
        return null;
    }

    @Override
    public List<TableRelationship> recommendRelationships(String dataSourceId, String tableName, int limit) {
        List<TableRelationship> allRelationships = tableRelationshipRepository.findByDataSourceId(dataSourceId);

        // 按权重和频率排序
        return allRelationships.stream()
                .filter(r -> r.getSourceTableName().equals(tableName) || r.getTargetTableName().equals(tableName))
                .sorted((r1, r2) -> {
                    // 计算综合得分
                    double score1 = r1.getConfidence() * 0.7 + r1.getFrequency() * 0.3;
                    double score2 = r2.getConfidence() * 0.7 + r2.getFrequency() * 0.3;
                    return Double.compare(score2, score1); // 降序
                })
                .limit(limit)
                .collect(Collectors.toList());
    }
}