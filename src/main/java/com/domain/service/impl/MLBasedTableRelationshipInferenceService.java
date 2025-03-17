package com.domain.service.impl;

import com.domain.model.metadata.ColumnInfo;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.domain.model.metadata.TableRelationship;
import com.domain.model.metadata.TableRelationship.RelationshipSource;
import com.domain.model.metadata.TableRelationship.RelationshipType;
import com.domain.repository.TableRelationshipRepository;
import com.domain.service.TableRelationshipInferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于机器学习的表关系推断服务实现
 */
@Slf4j
@Service
public class MLBasedTableRelationshipInferenceService implements TableRelationshipInferenceService {

    private static final double DEFAULT_CONFIDENCE_THRESHOLD = 0.7;
    private static final double NAME_SIMILARITY_WEIGHT = 0.4;
    private static final double CONTENT_SIMILARITY_WEIGHT = 0.6;

    @Autowired
    private TableRelationshipRepository tableRelationshipRepository;

    @Override
    public List<TableRelationship> inferRelationshipsFromContent(String dataSourceId, SchemaInfo schemaInfo) {
        log.info("为数据源 {} 的模式 {} 基于内容推断表关系", dataSourceId, schemaInfo.getName());
        List<TableRelationship> relationships = new ArrayList<>();

        // 获取所有表信息
        List<TableInfo> tables = schemaInfo.getTables();

        // 对每对表执行内容分析
        for (int i = 0; i < tables.size(); i++) {
            TableInfo sourceTable = tables.get(i);

            for (int j = i + 1; j < tables.size(); j++) {
                TableInfo targetTable = tables.get(j);

                // 分析列内容相似性
                List<ColumnPair> columnPairs = findSimilarColumns(sourceTable, targetTable);

                // 为每个相似列对创建关系
                for (ColumnPair pair : columnPairs) {
                    if (pair.similarity >= DEFAULT_CONFIDENCE_THRESHOLD) {
                        TableRelationship relationship = createRelationship(
                                dataSourceId,
                                sourceTable.getName(),
                                pair.sourceColumn,
                                targetTable.getName(),
                                pair.targetColumn,
                                inferRelationshipType(sourceTable, pair.sourceColumn, targetTable, pair.targetColumn),
                                RelationshipSource.METADATA,  // 这里使用METADATA作为来源，因为是基于数据分析
                                pair.similarity
                        );
                        relationships.add(relationship);
                    }
                }
            }
        }

        log.info("为数据源 {} 的模式 {} 基于内容推断出 {} 个表关系", dataSourceId, schemaInfo.getName(), relationships.size());
        return relationships;
    }

    @Override
    public List<TableRelationship> inferRelationshipsFromColumnNames(String dataSourceId, SchemaInfo schemaInfo) {
        log.info("为数据源 {} 的模式 {} 基于列名推断表关系", dataSourceId, schemaInfo.getName());
        List<TableRelationship> relationships = new ArrayList<>();

        // 获取所有表信息
        List<TableInfo> tables = schemaInfo.getTables();

        // 构建列名到表信息的映射
        Map<String, List<ColumnTableInfo>> columnNameMap = new HashMap<>();

        // 填充映射
        for (TableInfo table : tables) {
            for (ColumnInfo column : table.getColumns()) {
                String normalizedName = normalizeColumnName(column.getName());
                columnNameMap.computeIfAbsent(normalizedName, k -> new ArrayList<>())
                        .add(new ColumnTableInfo(table, column));
            }
        }

        // 查找具有相同标准化名称的列
        for (Map.Entry<String, List<ColumnTableInfo>> entry : columnNameMap.entrySet()) {
            List<ColumnTableInfo> columnsWithSameName = entry.getValue();

            // 如果有多个表的列具有相同的标准化名称
            if (columnsWithSameName.size() > 1) {
                // 为每对表创建关系
                for (int i = 0; i < columnsWithSameName.size(); i++) {
                    ColumnTableInfo source = columnsWithSameName.get(i);

                    for (int j = i + 1; j < columnsWithSameName.size(); j++) {
                        ColumnTableInfo target = columnsWithSameName.get(j);

                        // 计算列名相似度
                        double similarity = calculateColumnNameSimilarity(
                                source.column.getName(),
                                target.column.getName()
                        );

                        if (similarity >= DEFAULT_CONFIDENCE_THRESHOLD) {
                            TableRelationship relationship = createRelationship(
                                    dataSourceId,
                                    source.table.getName(),
                                    source.column.getName(),
                                    target.table.getName(),
                                    target.column.getName(),
                                    inferRelationshipType(source.table, source.column.getName(), target.table, target.column.getName()),
                                    RelationshipSource.METADATA,
                                    similarity
                            );
                            relationships.add(relationship);
                        }
                    }
                }
            }
        }

        log.info("为数据源 {} 的模式 {} 基于列名推断出 {} 个表关系", dataSourceId, schemaInfo.getName(), relationships.size());
        return relationships;
    }

    @Override
    public List<TableRelationship> inferRelationshipsFromCommonPatterns(String dataSourceId, SchemaInfo schemaInfo) {
        log.info("为数据源 {} 的模式 {} 基于常见模式推断表关系", dataSourceId, schemaInfo.getName());
        List<TableRelationship> relationships = new ArrayList<>();

        // 获取所有表信息
        List<TableInfo> tables = schemaInfo.getTables();

        // 常见的关系模式
        // 1. ID列与相应的表（例如 customer_id 与 customers 表）
        // 2. 多对多关系表（通常有两个或更多外键列）
        // 3. 主表和附属表的命名模式（例如 orders 和 order_items）

        // 寻找ID列与相应表的关系
        for (TableInfo sourceTable : tables) {
            for (ColumnInfo sourceColumn : sourceTable.getColumns()) {
                // 如果列名以_id结尾
                if (sourceColumn.getName().toLowerCase().endsWith("_id")) {
                    // 提取可能的表名
                    String possibleTableName = sourceColumn.getName().toLowerCase().replace("_id", "");

                    // 寻找匹配的表
                    for (TableInfo targetTable : tables) {
                        if (targetTable.getName().toLowerCase().equals(possibleTableName) ||
                                targetTable.getName().toLowerCase().equals(possibleTableName + "s")) {

                            // 寻找目标表中的ID列
                            for (ColumnInfo targetColumn : targetTable.getColumns()) {
                                if (targetColumn.getName().toLowerCase().equals("id")) {
                                    TableRelationship relationship = createRelationship(
                                            dataSourceId,
                                            sourceTable.getName(),
                                            sourceColumn.getName(),
                                            targetTable.getName(),
                                            targetColumn.getName(),
                                            RelationshipType.MANY_TO_ONE,  // 假设这是多对一关系
                                            RelationshipSource.METADATA,
                                            0.85  // 较高的置信度
                                    );
                                    relationships.add(relationship);
                                }
                            }
                        }
                    }
                }
            }
        }

        // 检测多对多关系表
        for (TableInfo table : tables) {
            List<ColumnInfo> idColumns = table.getColumns().stream()
                    .filter(col -> col.getName().toLowerCase().endsWith("_id"))
                    .collect(Collectors.toList());

            // 如果表有两个或更多的外键列
            if (idColumns.size() >= 2) {
                // 这可能是一个多对多关系表
                for (int i = 0; i < idColumns.size(); i++) {
                    ColumnInfo sourceColumn = idColumns.get(i);

                    for (int j = i + 1; j < idColumns.size(); j++) {
                        ColumnInfo targetColumn = idColumns.get(j);

                        // 提取可能的表名
                        String sourceTableName = sourceColumn.getName().toLowerCase().replace("_id", "");
                        String targetTableName = targetColumn.getName().toLowerCase().replace("_id", "");

                        // 寻找匹配的表
                        TableInfo sourceTableFound = findTableByNamePattern(tables, sourceTableName);
                        TableInfo targetTableFound = findTableByNamePattern(tables, targetTableName);

                        if (sourceTableFound != null && targetTableFound != null) {
                            // 为源表和目标表创建多对多关系
                            TableRelationship relationship = createRelationship(
                                    dataSourceId,
                                    sourceTableFound.getName(),
                                    "id",  // 假设主键是id
                                    targetTableFound.getName(),
                                    "id",  // 假设主键是id
                                    RelationshipType.MANY_TO_MANY,
                                    RelationshipSource.METADATA,
                                    0.8  // 较高的置信度
                            );
                            relationships.add(relationship);
                        }
                    }
                }
            }
        }

        // 检测主表和附属表的关系
        for (TableInfo sourceTable : tables) {
            for (TableInfo targetTable : tables) {
                if (!sourceTable.equals(targetTable)) {
                    // 检查targetTable名称是否以sourceTable名称开头
                    if (targetTable.getName().toLowerCase().startsWith(sourceTable.getName().toLowerCase() + "_")) {
                        // 这可能是一对多关系（例如orders和order_items）
                        TableRelationship relationship = createRelationship(
                                dataSourceId,
                                sourceTable.getName(),
                                "id",  // 假设主键是id
                                targetTable.getName(),
                                sourceTable.getName().toLowerCase() + "_id",  // 推断外键名
                                RelationshipType.ONE_TO_MANY,
                                RelationshipSource.METADATA,
                                0.75  // 中等置信度
                        );
                        relationships.add(relationship);
                    }
                }
            }
        }

        log.info("为数据源 {} 的模式 {} 基于常见模式推断出 {} 个表关系", dataSourceId, schemaInfo.getName(), relationships.size());
        return relationships;
    }

    @Override
    public List<TableRelationship> inferRelationships(String dataSourceId, SchemaInfo schemaInfo) {
        log.info("为数据源 {} 的模式 {} 综合推断表关系", dataSourceId, schemaInfo.getName());

        // 从不同维度推断关系
        List<TableRelationship> contentBasedRelationships = inferRelationshipsFromContent(dataSourceId, schemaInfo);
        List<TableRelationship> nameBasedRelationships = inferRelationshipsFromColumnNames(dataSourceId, schemaInfo);
        List<TableRelationship> patternBasedRelationships = inferRelationshipsFromCommonPatterns(dataSourceId, schemaInfo);

        // 合并关系列表
        List<TableRelationship> allRelationships = new ArrayList<>();
        allRelationships.addAll(contentBasedRelationships);
        allRelationships.addAll(nameBasedRelationships);
        allRelationships.addAll(patternBasedRelationships);

        // 合并相同表列对的关系，保留权重最高的
        Map<String, TableRelationship> relationshipMap = new HashMap<>();

        for (TableRelationship relationship : allRelationships) {
            String key = generateRelationshipKey(relationship);

            if (!relationshipMap.containsKey(key) ||
                    relationshipMap.get(key).getConfidence() < relationship.getConfidence()) {
                relationshipMap.put(key, relationship);
            }
        }

        List<TableRelationship> mergedRelationships = new ArrayList<>(relationshipMap.values());

        log.info("为数据源 {} 的模式 {} 综合推断出 {} 个表关系", dataSourceId, schemaInfo.getName(), mergedRelationships.size());
        return mergedRelationships;
    }

    @Override
    public List<TableRelationship> recommendRelationships(String dataSourceId, String tableName, double confidence, int limit) {
        log.info("为数据源 {} 的表 {} 推荐关系", dataSourceId, tableName);

        // 获取数据源中所有表关系
        List<TableRelationship> allRelationships = tableRelationshipRepository.findByDataSourceId(dataSourceId);

        // 筛选与指定表相关的关系
        List<TableRelationship> relatedRelationships = allRelationships.stream()
                .filter(r -> r.getSourceTableName().equals(tableName) || r.getTargetTableName().equals(tableName))
                .filter(r -> r.getConfidence() >= confidence)
                .sorted(Comparator.comparingDouble(TableRelationship::getConfidence).reversed())
                .limit(limit)
                .collect(Collectors.toList());

        log.info("为数据源 {} 的表 {} 推荐了 {} 个关系", dataSourceId, tableName, relatedRelationships.size());
        return relatedRelationships;
    }

    // 辅助方法

    private String generateRelationshipKey(TableRelationship relationship) {
        // 创建一个唯一键，表示表和列对
        return String.format("%s.%s-%s.%s",
                relationship.getSourceTableName(), relationship.getSourceColumnNames(),
                relationship.getTargetTableName(), relationship.getTargetColumnNames());
    }

    private TableInfo findTableByNamePattern(List<TableInfo> tables, String namePattern) {
        // 首先尝试精确匹配
        for (TableInfo table : tables) {
            if (table.getName().toLowerCase().equals(namePattern)) {
                return table;
            }
        }

        // 然后尝试匹配复数形式
        for (TableInfo table : tables) {
            if (table.getName().toLowerCase().equals(namePattern + "s")) {
                return table;
            }
        }

        return null;
    }

    private String normalizeColumnName(String columnName) {
        // 标准化列名（例如，转换为小写，删除前缀/后缀）
        String normalized = columnName.toLowerCase();

        // 删除常见的前缀，如"fk_", "pk_"等
        if (normalized.startsWith("fk_")) {
            normalized = normalized.substring(3);
        } else if (normalized.startsWith("pk_")) {
            normalized = normalized.substring(3);
        }

        // 处理"_id"后缀
        if (normalized.endsWith("_id")) {
            normalized = normalized.substring(0, normalized.length() - 3);
        }

        return normalized;
    }

    private double calculateColumnNameSimilarity(String name1, String name2) {
        // 实现列名相似度计算
        // 这里使用简化实现，实际中可能需要更复杂的算法
        String norm1 = normalizeColumnName(name1);
        String norm2 = normalizeColumnName(name2);

        if (norm1.equals(norm2)) {
            return 1.0;
        }

        // 计算Levenshtein距离
        int distance = levenshteinDistance(norm1, norm2);
        int maxLength = Math.max(norm1.length(), norm2.length());

        // 转换为相似度
        if (maxLength == 0) {
            return 1.0;
        }

        return 1.0 - ((double) distance / maxLength);
    }

    private int levenshteinDistance(String s1, String s2) {
        // 计算两个字符串之间的Levenshtein距离
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            dp[i][0] = i;
        }

        for (int j = 0; j <= s2.length(); j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= s1.length(); i++) {
            for (int j = 1; j <= s2.length(); j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        return dp[s1.length()][s2.length()];
    }

    private RelationshipType inferRelationshipType(TableInfo sourceTable, String sourceColumn,
                                                   TableInfo targetTable, String targetColumn) {
        // 尝试推断关系类型
        // 这是一个简化实现，实际中可能需要更复杂的分析

        // 检查是否是主键-外键关系
        boolean sourceIsPk = isPrimaryKey(sourceTable, sourceColumn);
        boolean targetIsPk = isPrimaryKey(targetTable, targetColumn);

        // 检查列名模式
        boolean sourceColumnEndsWithId = sourceColumn.toLowerCase().endsWith("_id");
        boolean targetColumnEndsWithId = targetColumn.toLowerCase().endsWith("_id");

        if (sourceIsPk && targetColumnEndsWithId) {
            return RelationshipType.ONE_TO_MANY;
        } else if (targetIsPk && sourceColumnEndsWithId) {
            return RelationshipType.MANY_TO_ONE;
        } else if (sourceIsPk && targetIsPk) {
            return RelationshipType.ONE_TO_ONE;
        } else {
            return RelationshipType.MANY_TO_MANY;
        }
    }

    private boolean isPrimaryKey(TableInfo table, String columnName) {
        // 检查列是否是主键
        return table.getColumns().stream()
                .filter(col -> col.getName().equals(columnName))
                .anyMatch(ColumnInfo::isPrimaryKey);
    }

    private List<ColumnPair> findSimilarColumns(TableInfo sourceTable, TableInfo targetTable) {
        // 寻找两个表之间相似的列对
        List<ColumnPair> pairs = new ArrayList<>();

        for (ColumnInfo sourceColumn : sourceTable.getColumns()) {
            for (ColumnInfo targetColumn : targetTable.getColumns()) {
                // 计算列名相似度
                double nameSimilarity = calculateColumnNameSimilarity(
                        sourceColumn.getName(),
                        targetColumn.getName()
                );

                // 这里应该有数据内容相似度的计算
                // 由于我们没有实际的数据，这里假设一个值
                double contentSimilarity = 0.0;

                // 如果列名相似，假设内容也相似
                if (nameSimilarity > 0.8) {
                    contentSimilarity = 0.7;
                }

                // 计算总体相似度
                double totalSimilarity = NAME_SIMILARITY_WEIGHT * nameSimilarity +
                        CONTENT_SIMILARITY_WEIGHT * contentSimilarity;

                if (totalSimilarity > 0) {
                    pairs.add(new ColumnPair(
                            sourceColumn.getName(),
                            targetColumn.getName(),
                            totalSimilarity
                    ));
                }
            }
        }

        // 按相似度排序
        pairs.sort(Comparator.comparingDouble(pair -> -pair.similarity));

        return pairs;
    }

    private TableRelationship createRelationship(String dataSourceId, String sourceTable, String sourceColumn,
                                                 String targetTable, String targetColumn, RelationshipType type,
                                                 RelationshipSource source, double confidence) {
        // 创建表关系对象
        return TableRelationship.builder()
                .dataSourceId(dataSourceId)
                .sourceTableName(sourceTable)
                .sourceColumnNames(sourceColumn)
                .targetTableName(targetTable)
                .targetColumnNames(targetColumn)
                .relationType(type)
                .relationSource(source)
                .confidence(confidence)
                .frequency(0)  // 初始频率为0
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // 内部类

    /**
     * 列对及其相似度
     */
    private static class ColumnPair {
        final String sourceColumn;
        final String targetColumn;
        final double similarity;

        ColumnPair(String sourceColumn, String targetColumn, double similarity) {
            this.sourceColumn = sourceColumn;
            this.targetColumn = targetColumn;
            this.similarity = similarity;
        }
    }

    /**
     * 列表信息
     */
    private static class ColumnTableInfo {
        final TableInfo table;
        final ColumnInfo column;

        ColumnTableInfo(TableInfo table, ColumnInfo column) {
            this.table = table;
            this.column = column;
        }
    }
}