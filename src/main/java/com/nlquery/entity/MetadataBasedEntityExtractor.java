package com.nlquery.entity;

import com.application.service.DataSourceService;
import com.domain.model.metadata.ColumnInfo;
import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableInfo;
import com.nlquery.preprocess.EntityTag;
import com.nlquery.preprocess.EntityType;
import com.nlquery.preprocess.PreprocessedText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于元数据的实体提取器
 * 使用数据源的元数据信息来提取实体
 */
@Component
public class MetadataBasedEntityExtractor implements EntityExtractor {
    
    @Autowired
    private DataSourceService dataSourceService;
    
    @Override
    public List<EntityTag> extractEntities(PreprocessedText preprocessedText) {
        return extractEntities(preprocessedText, new EntityExtractionContext());
    }
    
    @Override
    public List<EntityTag> extractEntities(PreprocessedText preprocessedText, EntityExtractionContext context) {
        List<EntityTag> entities = new ArrayList<>();
        
        // 如果不使用元数据，则直接返回空列表
        if (!context.isUseMetadata()) {
            return entities;
        }
        
        // 如果没有数据源ID，则直接返回空列表
        if (context.getDataSourceId() == null) {
            return entities;
        }
        
        // 获取元数据信息
        SchemaInfo metadata = context.getMetadata();
        if (metadata == null) {
            // 如果上下文中没有元数据，则从数据源服务获取
            try {
                metadata = dataSourceService.getSchemaInfo(context.getDataSourceId(), context.getSchemaName());
                context.setMetadata(metadata);
            } catch (Exception e) {
                // 如果获取元数据失败，则直接返回空列表
                return entities;
            }
        }
        
        // 提取表名实体
        extractTableEntities(preprocessedText, metadata, entities, context);
        
        // 提取列名实体
        extractColumnEntities(preprocessedText, metadata, entities, context);
        
        return entities;
    }
    
    /**
     * 提取表名实体
     */
    private void extractTableEntities(PreprocessedText preprocessedText, SchemaInfo metadata, List<EntityTag> entities, EntityExtractionContext context) {
        String normalizedText = preprocessedText.getNormalizedText();
        List<String> tokens = preprocessedText.getTokens();
        
        // 创建表名映射
        Map<String, TableInfo> tableMap = new HashMap<>();
        for (TableInfo table : metadata.getTables()) {
            tableMap.put(table.getName().toLowerCase(), table);
            // 如果表有注释，也添加到映射中
            if (table.getDescription() != null && !table.getDescription().isEmpty()) {
                tableMap.put(table.getDescription().toLowerCase(), table);
            }
        }
        
        // 遍历分词结果，查找匹配的表名
        for (String token : tokens) {
            String lowerToken = token.toLowerCase();
            
            // 精确匹配
            if (tableMap.containsKey(lowerToken)) {
                int startPosition = normalizedText.toLowerCase().indexOf(lowerToken);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.TABLE, startPosition, startPosition + token.length(), 0.9));
                }
                continue;
            }
            
            // 模糊匹配
            if (context.isUseFuzzyMatching()) {
                for (Map.Entry<String, TableInfo> entry : tableMap.entrySet()) {
                    String tableName = entry.getKey();
                    
                    // 如果token是表名的一部分，或者表名是token的一部分
                    if (lowerToken.contains(tableName) || tableName.contains(lowerToken)) {
                        double similarity = calculateSimilarity(lowerToken, tableName);
                        if (similarity >= context.getMinConfidence()) {
                            int startPosition = normalizedText.toLowerCase().indexOf(lowerToken);
                            if (startPosition >= 0) {
                                entities.add(new EntityTag(token, EntityType.TABLE, startPosition, startPosition + token.length(), similarity));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 提取列名实体
     */
    private void extractColumnEntities(PreprocessedText preprocessedText, SchemaInfo metadata, List<EntityTag> entities, EntityExtractionContext context) {
        String normalizedText = preprocessedText.getNormalizedText();
        List<String> tokens = preprocessedText.getTokens();
        
        // 创建列名映射
        Map<String, ColumnInfo> columnMap = new HashMap<>();
        for (TableInfo table : metadata.getTables()) {
            for (ColumnInfo column : table.getColumns()) {
                columnMap.put(column.getName().toLowerCase(), column);
                // 如果列有注释，也添加到映射中
                if (column.getDescription() != null && !column.getDescription().isEmpty()) {
                    columnMap.put(column.getDescription().toLowerCase(), column);
                }
            }
        }
        
        // 遍历分词结果，查找匹配的列名
        for (String token : tokens) {
            String lowerToken = token.toLowerCase();
            
            // 精确匹配
            if (columnMap.containsKey(lowerToken)) {
                int startPosition = normalizedText.toLowerCase().indexOf(lowerToken);
                if (startPosition >= 0) {
                    entities.add(new EntityTag(token, EntityType.COLUMN, startPosition, startPosition + token.length(), 0.9));
                }
                continue;
            }
            
            // 模糊匹配
            if (context.isUseFuzzyMatching()) {
                for (Map.Entry<String, ColumnInfo> entry : columnMap.entrySet()) {
                    String columnName = entry.getKey();
                    
                    // 如果token是列名的一部分，或者列名是token的一部分
                    if (lowerToken.contains(columnName) || columnName.contains(lowerToken)) {
                        double similarity = calculateSimilarity(lowerToken, columnName);
                        if (similarity >= context.getMinConfidence()) {
                            int startPosition = normalizedText.toLowerCase().indexOf(lowerToken);
                            if (startPosition >= 0) {
                                entities.add(new EntityTag(token, EntityType.COLUMN, startPosition, startPosition + token.length(), similarity));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 计算两个字符串的相似度
     * 使用Levenshtein距离算法
     */
    private double calculateSimilarity(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        
        // 如果有一个字符串为空，则相似度为0
        if (len1 == 0 || len2 == 0) {
            return 0.0;
        }
        
        // 如果两个字符串相同，则相似度为1
        if (s1.equals(s2)) {
            return 1.0;
        }
        
        // 计算Levenshtein距离
        int[][] dp = new int[len1 + 1][len2 + 1];
        
        for (int i = 0; i <= len1; i++) {
            dp[i][0] = i;
        }
        
        for (int j = 0; j <= len2; j++) {
            dp[0][j] = j;
        }
        
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }
        
        // 计算相似度
        int maxLen = Math.max(len1, len2);
        return 1.0 - (double) dp[len1][len2] / maxLen;
    }
}