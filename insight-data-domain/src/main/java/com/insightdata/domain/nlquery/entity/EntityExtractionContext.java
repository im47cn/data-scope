package com.insightdata.domain.nlquery.entity;

import com.insightdata.domain.metadata.model.SchemaInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityExtractionContext {
    private Boolean fuzzyMatchingEnabled;
    private Double minConfidence;
    private Boolean metadataEnabled;
    private String dataSourceId;
    private SchemaInfo metadata;

    public boolean isUseFuzzyMatching() {
        return fuzzyMatchingEnabled != null && fuzzyMatchingEnabled;
    }

    public boolean isUseMetadata() {
        return metadataEnabled != null && metadataEnabled;
    }
}