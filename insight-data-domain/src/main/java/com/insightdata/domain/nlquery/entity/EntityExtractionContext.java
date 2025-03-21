package com.insightdata.domain.nlquery.entity;

import com.insightdata.domain.metadata.model.SchemaInfo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityExtractionContext {
    private boolean useFuzzyMatching;
    private double minConfidence;

    public boolean isUseMetadata() {
        return true;
    }

    public String getDataSourceId(){
        return "";
    }
    public SchemaInfo getMetadata(){
        return null;
    }

    public void setMetadata(SchemaInfo metadata){
        return;
    }
    public boolean isUseFuzzyMatching(){
        return this.useFuzzyMatching;
    }
    public double getMinConfidence(){
        return this.minConfidence;
    }

    public EntityExtractionContext(boolean useFuzzyMatching, double minConfidence) {
        this.useFuzzyMatching = useFuzzyMatching;
        this.minConfidence = minConfidence;
    }

    public EntityExtractionContext() {

    }
}