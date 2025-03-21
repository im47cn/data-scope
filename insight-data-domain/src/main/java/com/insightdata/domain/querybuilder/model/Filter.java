package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single filter condition in a query.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    /**
     * Unique identifier for the filter.
     */
    private String id;

    /**
     * The field to filter on.
     */
    private String field;

    /**
     * The operator to use for the filter (e.g., =, !=, >, <, IN, BETWEEN).
     */
    private String operator;

    /**
     * The value to compare the field against. Can be a literal value or a parameter reference.
     */
    private Object value;

    /**
     * Indicates if the value is a parameter reference (true) or a literal value (false).
     */
    private boolean isParameter;

    /**
     * Validates the filter.
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (field == null || field.trim().isEmpty()) {
            throw new QueryModelValidationException("Filter field cannot be empty");
        }
        if (operator == null || operator.trim().isEmpty()) {
            throw new QueryModelValidationException("Filter operator cannot be empty");
        }
        if (value == null) {
            throw new QueryModelValidationException("Filter value cannot be null");
        }
    }
}