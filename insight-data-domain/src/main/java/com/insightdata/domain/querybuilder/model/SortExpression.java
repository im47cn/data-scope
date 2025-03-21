package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a sort expression for the ORDER BY clause.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortExpression {

    /**
     * Unique identifier for the sort expression.
     */
    private String id;

    /**
     * The field to sort by.
     */
    private String field;

    /**
     * The sort direction (ASC or DESC).
     */
    private SortDirection direction;

    /**
     * Optional expression to apply to the field before sorting.
     */
    private String expression;

    /**
     * Enum for sort direction (ASC or DESC).
     */
    public enum SortDirection {
        ASC,
        DESC
    }
}