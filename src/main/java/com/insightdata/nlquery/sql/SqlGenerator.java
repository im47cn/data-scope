package com.insightdata.nlquery.sql;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.preprocess.EntityTag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

/**
 * SQL生成器接口
 */
public interface SqlGenerator {
    
    /**
     * 生成SQL
     *
     * @param entities 提取的实体
     * @param queryIntent 查询意图
     * @param schemaInfo 数据库模式信息
     * @return SQL生成结果
     */
    SqlGenerationResult generateSql(List<EntityTag> entities, QueryIntent queryIntent, SchemaInfo schemaInfo);

    /**
     * SQL生成结果
     */
    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class SqlGenerationResult {
        /**
         * 生成的SQL
         */
        String sql = "";

        /**
         * SQL参数
         */
        Map<String, Object> parameters;

        /**
         * 置信度
         */
        double confidence;

        /**
         * 解释说明
         */
        List<String> explanations;

        /**
         * 替代SQL语句
         */
        List<String> alternativeSqls;
    }
}