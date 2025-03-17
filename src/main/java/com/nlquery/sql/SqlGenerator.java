package com.nlquery.sql;

import java.util.List;
import java.util.Map;

import com.domain.model.metadata.SchemaInfo;
import com.nlquery.QueryContext;
import com.nlquery.entity.EntityTag;
import com.nlquery.intent.QueryIntent;

import lombok.Builder;
import lombok.Data;

public interface SqlGenerator {

    /**
     * SQL生成结果
     */
    @Data
    @Builder
    class SqlGenerationResult {
        /**
         * 生成的SQL语句
         */
        private String sql;

        /**
         * SQL参数
         */
        private Map<String, Object> parameters;

        /**
         * 置信度
         */
        private double confidence;

        /**
         * SQL解释
         */
        private List<String> explanations;

        /**
         * 备选SQL语句
         */
        private List<String> alternativeSqls;
    }

    /**
     * 生成SQL
     *
     * @param entities    提取的实体
     * @param queryIntent 查询意图
     * @param schemaInfo
     * @return SQL生成结果
     */
    SqlGenerationResult generate(List<EntityTag> entities, QueryIntent queryIntent, SchemaInfo schemaInfo);

}