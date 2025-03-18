package com.nlquery.entity;

import com.domain.model.metadata.ColumnInfo;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.domain.model.query.QueryContext;
import com.domain.service.DataSourceService;
import com.nlquery.preprocess.PreprocessedText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (table.getDescription() != null && !table.getDescription().isEmpty()) {
                tableMap.put(table.getDescription().toLowerCase(), table);
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
                if (column.getDescription() != null && !column.getDescription().isEmpty()) {
                    columnMap.put(column.getDescription().toLowerCase(), column);
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

    // Dummy implementation for now
    private double calculateSimilarity(String token, String entityName) {
        return 0.8;
    }

}