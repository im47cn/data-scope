package com.nlquery.sql;

import com.domain.model.metadata.SchemaInfo;
import com.nlquery.entity.EntityTag;
import com.nlquery.intent.QueryIntent;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

public interface SqlGenerator {

    SqlGenerationResult generate(List<EntityTag> entities, QueryIntent queryIntent, SchemaInfo schemaInfo);

    @Data
    @Builder
    class SqlGenerationResult{
        private String sql;
        private Map<String, Object> parameters;
        private double confidence;
        private List<String> explanations;
        private List<String> alternativeSqls; // Optional alternative SQL queries
        private boolean success;
        private String errorMessage;

        public String getSql() {
            return sql;
        }

        public Map<String, Object> getParameters() {
            return parameters;
        }

        public double getConfidence() {
            return confidence;
        }

        public List<String> getExplanations() {
            return explanations;
        }

        public List<String> getAlternativeSqls() {
            return alternativeSqls;
        }
        public boolean isSuccess() {
            return success;
        }
        public String getErrorMessage(){
            return this.errorMessage;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public void setParameters(Map<String, Object> parameters) {
            this.parameters = parameters;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public void setExplanations(List<String> explanations) {
            this.explanations = explanations;
        }

        public void setAlternativeSqls(List<String> alternativeSqls) {
            this.alternativeSqls = alternativeSqls;
        }
        public void setSuccess(boolean success){
            this.success = success;
        }
        public void setErrorMessage(String errorMessage){
            this.errorMessage = errorMessage;
        }
    }
}