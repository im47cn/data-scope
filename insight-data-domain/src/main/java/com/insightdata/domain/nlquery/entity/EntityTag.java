package com.insightdata.domain.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entity tag for text preprocessing
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityTag {

    /**
     * Entity type
     */
    private EntityType type;

    /**
     * Entity value
     */
    private String value;

    /**
     * Start offset in original text
     */
    private int startOffset;

    /**
     * End offset in original text
     */
    private int endOffset;

    /**
     * Confidence score
     */
    private double confidence;

    /**
     * Create a new entity tag
     */
    public static EntityTag of(EntityType type, String value) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(1.0)
                .build();
    }

    /**
     * Create a new entity tag with confidence
     */
    public static EntityTag of(EntityType type, String value, double confidence) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(confidence)
                .build();
    }

    /**
     * Create a new entity tag with offsets
     */
    public static EntityTag of(EntityType type, String value, int startOffset, int endOffset) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .startOffset(startOffset)
                .endOffset(endOffset)
                .confidence(1.0)
                .build();
    }

    /**
     * Create a new entity tag with all fields
     */
    public static EntityTag of(EntityType type, String value, int startOffset, int endOffset, double confidence) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .startOffset(startOffset)
                .endOffset(endOffset)
                .confidence(confidence)
                .build();
    }
}