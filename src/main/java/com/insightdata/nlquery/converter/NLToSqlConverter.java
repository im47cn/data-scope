package com.insightdata.nlquery.converter;

import java.util.List;
import java.util.Map;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.entity.EntityExtractionContext;
import com.insightdata.nlquery.intent.IntentRecognitionContext;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.preprocess.EntityTag;
import com.insightdata.nlquery.preprocess.PreprocessedText;

/**
 * 自然语言到SQL转换器接口
 * 负责将预处理后的自然语言文本转换为SQL查询
 */
public interface NLToSqlConverter {

    /**
     * 将自然语言转换为SQL
     *
     * @param preprocessedText 预处理后的文本
     * @param schemaInfo 数据库模式信息
     * @return SQL转换结果
     */
    SqlConversionResult convertToSql(PreprocessedText preprocessedText, SchemaInfo schemaInfo);

    /**
     * 将自然语言转换为SQL（带上下文）
     *
     * @param preprocessedText 预处理后的文本
     * @param schemaInfo 数据库模式信息
     * @param entityExtractionContext 实体提取上下文
     * @param intentRecognitionContext 意图识别上下文
     * @return SQL转换结果
     */
    SqlConversionResult convertToSql(
            PreprocessedText preprocessedText,
            SchemaInfo schemaInfo,
            EntityExtractionContext entityExtractionContext,
            IntentRecognitionContext intentRecognitionContext);

    /**
     * SQL转换结果
     */
    class SqlConversionResult {
        private String sql;
        private Map<String, Object> parameters;
        private List<EntityTag> extractedEntities;
        private QueryIntent queryIntent;
        private double confidence;
        private List<String> explanations;
        private List<String> alternativeSqls;

        public SqlConversionResult() {
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

        public List<EntityTag> getExtractedEntities() {
            return extractedEntities;
        }

        public void setExtractedEntities(List<EntityTag> extractedEntities) {
            this.extractedEntities = extractedEntities;
        }

        public QueryIntent getQueryIntent() {
            return queryIntent;
        }

        public void setQueryIntent(QueryIntent queryIntent) {
            this.queryIntent = queryIntent;
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