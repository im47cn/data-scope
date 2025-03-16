package com.insightdata.nlquery.sql;

import java.util.List;
import java.util.Map;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.preprocess.EntityTag;

/**
 * SQL生成器接口
 * 负责根据提取的实体和识别的意图生成SQL查询
 */
public interface SqlGenerator {

    /**
     * 生成SQL查询
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
    class SqlGenerationResult {
        private String sql;
        private Map<String, Object> parameters;
        private double confidence;
        private List<String> explanations;
        private List<String> alternativeSqls;

        public SqlGenerationResult() {
        }

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public List<String> getExplanations() {
            return explanations;
        }

        public void setExplanations(List<String> explanations) {
            this.explanations = explanations;
        }

        public List<String> getAlternativeSqls() {
            return alternativeSqls;
        }

        public void setAlternativeSqls(List<String> alternativeSqls) {
            this.alternativeSqls = alternativeSqls;
        }
    }
}