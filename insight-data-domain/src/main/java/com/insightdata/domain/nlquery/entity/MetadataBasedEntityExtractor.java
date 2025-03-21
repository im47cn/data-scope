package com.insightdata.domain.nlquery.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.insightdata.domain.metadata.model.ColumnInfo;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.metadata.model.TableInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insightdata.domain.metadata.service.DataSourceService;
import com.insightdata.domain.nlquery.QueryContext;
import com.insightdata.domain.nlquery.preprocess.PreprocessedText;

@Component
public class MetadataBasedEntityExtractor implements EntityExtractor {

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public List<EntityTag> extract(PreprocessedText preprocessedText, EntityExtractionContext context) {
        // Implementation for MetadataBasedEntityExtractor
        List<EntityTag> entities = new ArrayList<>();
        if (!context.isUseMetadata()) {
            return entities;
        }

        SchemaInfo metadata = context.getMetadata();
        if (metadata == null) {
            if (context.getDataSourceId() == null) {
                return entities;
            }
            metadata = dataSourceService.getSchemaInfo(context.getDataSourceId(), context.getMetadata().getName());
            context.setMetadata(metadata);
        }

        // Extract table names
        Map<String, TableInfo> tableMap = new HashMap<>();
        for (TableInfo table : metadata.getTables()) {
            tableMap.put(table.getName().toLowerCase(), table);
            if (table.getComment() != null && !table.getComment().isEmpty()) {
                tableMap.put(table.getComment().toLowerCase(), table);
            }
        }

        for (String token : preprocessedText.getTokens()) {
            String normalizedToken = token.toLowerCase();
            if (tableMap.containsKey(normalizedToken)) {
                TableInfo table = tableMap.get(normalizedToken);
                entities.add(EntityTag.builder()
                        .value(table.getName()) // Use original case for value
                        .type(EntityType.TABLE) // Use the enum
                        .confidence(1.0) // Metadata-based extraction is high confidence
                        .build());
            } else if (context.isUseFuzzyMatching()) {
                for (String tableName : tableMap.keySet()) {
                    double similarity = calculateSimilarity(normalizedToken, tableName); // Implement this method
                    if (similarity >= context.getMinConfidence()) {
                        TableInfo table = tableMap.get(tableName);
                        entities.add(EntityTag.builder()
                                .value(table.getName()) // Use original case for value
                                .type(EntityType.TABLE) // Use the enum
                                .confidence(similarity)
                                .build());
                    }
                }
            }
        }

        // Extract column names
        Map<String, ColumnInfo> columnMap = new HashMap<>();
        for (TableInfo table : metadata.getTables()) {
            for (ColumnInfo column : table.getColumns()) {
                columnMap.put(column.getName().toLowerCase(), column);
                if (column.getComment() != null && !column.getComment().isEmpty()) {
                    columnMap.put(column.getComment().toLowerCase(), column);
                }
            }
        }

        for (String token : preprocessedText.getTokens()) {
            String normalizedToken = token.toLowerCase();
            if (columnMap.containsKey(normalizedToken)) {
                ColumnInfo column = columnMap.get(normalizedToken);
                entities.add(EntityTag.builder()
                        .value(column.getName()) // Use original case for value
                        .type(EntityType.COLUMN) // Use the enum
                        .confidence(1.0)
                        .build());
            }
            else if (context.isUseFuzzyMatching()) {
                for(String columnName : columnMap.keySet()){
                    double similarity = calculateSimilarity(normalizedToken, columnName);
                    if(similarity >= context.getMinConfidence()){
                        ColumnInfo column = columnMap.get(columnName);
                        entities.add(EntityTag.builder()
                                .value(column.getName())
                                .type(EntityType.COLUMN)
                                .confidence(similarity)
                                .build());
                    }
                }
            }
        }

        return entities;
    }

    @Override
    public List<EntityTag> extract(PreprocessedText preprocessedText) {
        // 创建默认上下文
        EntityExtractionContext defaultContext = EntityExtractionContext.builder()
                .useFuzzyMatching(true)
                .minConfidence(0.7)
                .build();

        // 调用已实现的方法
        return extract(preprocessedText, defaultContext);
    }

    @Override
    public List<EntityTag> validate(List<EntityTag> entities, QueryContext context) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        // 过滤掉置信度低于阈值的实体
        return entities.stream()
                .filter(entity -> entity.getConfidence() >= 0.5) // 默认阈值
                .collect(Collectors.toList());

        // 注意：在实际实现中，可能需要更复杂的验证逻辑
        // 例如，检查实体是否存在于数据源的元数据中
    }

    @Override
    public List<EntityTag> merge(List<EntityTag> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }

        // 使用Map按实体类型和值分组，然后合并
        Map<String, EntityTag> mergedEntities = new HashMap<>();

        for (EntityTag entity : entities) {
            String key = entity.getType() + ":" + entity.getValue();
            if (mergedEntities.containsKey(key)) {
                // 如果已存在相同实体，选择置信度更高的一个
                EntityTag existing = mergedEntities.get(key);
                if (entity.getConfidence() > existing.getConfidence()) {
                    mergedEntities.put(key, entity);
                }
            } else {
                mergedEntities.put(key, entity);
            }
        }

        return new ArrayList<>(mergedEntities.values());
    }

    // Dummy implementation for now
    private double calculateSimilarity(String token, String entityName) {
        return 0.8;
    }

}