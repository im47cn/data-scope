package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single join condition between two tables.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinCondition {

    /**
     * The field on the left side of the join.
     */
    private String leftField;

    /**
     * The field on the right side of the join.
     */
    private String rightField;

    /**
     * The operator used to join the fields (e.g., =, !=, >, <).
     */
    private String operator;

    /**
     * Validates the join condition.
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (leftField == null || leftField.trim().isEmpty()) {
            throw new QueryModelValidationException("Left field cannot be empty in join condition");
        }
        if (rightField == null || rightField.trim().isEmpty()) {
            throw new QueryModelValidationException("Right field cannot be empty in join condition");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new QueryModelValidationException("Operator cannot be empty in join condition");
        }
    }
}