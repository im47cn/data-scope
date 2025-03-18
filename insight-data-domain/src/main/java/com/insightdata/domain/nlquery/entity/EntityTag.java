package com.insightdata.domain.nlquery.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityTag {
    private String value;
    private EntityType type; // Changed to EntityType
    private double confidence;
    private int startOffset;
    private int endOffset;
}