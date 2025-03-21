package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Definition of a join between two tables in a query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinDefinition {

    /**
     * Unique identifier for the join definition
     */
    private String id;

    /**
     * ID of the left table in the join
     */
    private String leftTableId;

    /**
     * ID of the right table in the join
     */
    private String rightTableId;

    /**
     * Type of join (INNER, LEFT, RIGHT, FULL)
     */
    private JoinType joinType;

    /**
     * List of join conditions
     */
    private List<JoinCondition> conditions;

    /**
     * Validates the join definition
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (leftTableId == null || leftTableId.trim().isEmpty()) {
            throw new QueryModelValidationException("Left table ID cannot be empty in join definition");
        }
        if (rightTableId == null || rightTableId.trim().isEmpty()) {
            throw new QueryModelValidationException("Right table ID cannot be empty in join definition");
        }
        if (conditions == null || conditions.isEmpty()) {
            throw new QueryModelValidationException("At least one join condition must be specified");
        }
        for (JoinCondition condition : conditions) {
            condition.validate();
        }
    }
}