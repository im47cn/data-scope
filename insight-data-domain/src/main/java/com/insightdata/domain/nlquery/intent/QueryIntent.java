package com.insightdata.domain.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Query intent class that represents the intent of a natural language query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryIntent {

    /**
     * Query type (SELECT, COUNT, SUM, etc.)
     */
    private QueryType queryType;

    /**
     * Query purpose (DATA_RETRIEVAL, STATISTICAL_ANALYSIS, etc.)
     */
    private QueryPurpose queryPurpose;

    /**
     * Time range requirement
     */
    private TimeRange timeRange;

    /**
     * Sort requirements
     */
    private List<SortRequirement> sortRequirements;

    /**
     * Limit requirement
     */
    private LimitRequirement limitRequirement;

    /**
     * Filter conditions
     */
    private List<FilterCondition> filterConditions;

    /**
     * Group by fields
     */
    private List<String> groupByFields;

    /**
     * Having conditions
     */
    private List<FilterCondition> havingConditions;

    /**
     * Selected fields
     */
    private List<String> selectedFields;

    /**
     * Confidence score
     */
    private double confidence;

    /**
     * Intent source
     */
    private IntentSource source;

    /**
     * Additional metadata
     */
    private String metadata;

    /**
     * Intent source enum
     */
    public enum IntentSource {
        RULE_BASED,          // Rule-based recognition
        MACHINE_LEARNING,    // Machine learning model
        USER_FEEDBACK,       // User feedback
        HYBRID,             // Hybrid approach
        UNKNOWN             // Unknown source
    }
}
