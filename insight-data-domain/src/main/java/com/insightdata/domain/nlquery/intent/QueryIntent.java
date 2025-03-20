package com.insightdata.domain.nlquery.intent;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * Confidence score
     */
    private double confidence;

    /**
     * Query type enum
     */
    public enum QueryType {
        SELECT,
        COUNT,
        SUM,
        AVG,
        MAX,
        MIN,
        GROUP
    }

    /**
     * Query purpose enum
     */
    public enum QueryPurpose {
        DATA_RETRIEVAL,
        STATISTICAL_ANALYSIS,
        TREND_ANALYSIS,
        COMPARISON_ANALYSIS
    }
}
