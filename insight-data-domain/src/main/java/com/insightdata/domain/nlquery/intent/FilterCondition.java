package com.insightdata.domain.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Filter condition class that represents a filtering condition in a query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCondition {

    /**
     * Field name to filter on
     */
    private String field;

    /**
     * Operator for the filter condition
     */
    private String operator;

    /**
     * Value to compare against
     */
    private Object value;

    /**
     * Logical operator (AND/OR) to connect with other conditions
     */
    private String logicalOperator;

    /**
     * Confidence score of this filter condition (0-1)
     */
    private double confidence;

    /**
     * Whether this is a nested condition
     */
    private boolean nested;

    /**
     * List of nested conditions if this is a nested condition
     */
    private List<FilterCondition> nestedConditions;

    /**
     * Original text that generated this condition
     */
    private String originalText;
}