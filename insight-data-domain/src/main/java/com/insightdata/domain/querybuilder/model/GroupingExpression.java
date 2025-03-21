package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a grouping expression for the GROUP BY clause.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupingExpression {

    /**
     * Unique identifier for the grouping expression.
     */
    private String id;

    /**
     * The field to group by.
     */
    private String field;

    /**
     * Optional expression to apply to the field before grouping.
     */
    private String expression;
}