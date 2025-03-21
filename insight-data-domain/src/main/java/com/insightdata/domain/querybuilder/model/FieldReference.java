package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reference to a field in a table
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldReference {
    /**
     * Unique identifier for the field reference
     */
    private String id;

    /**
     * Name of the field in the table
     */
    private String name;

    /**
     * Alias for the field in the query
     */
    private String alias;

    /**
     * Expression used to compute the field value
     */
    private String expression;

    /**
     * Type of the field (e.g., STRING, NUMBER, etc.)
     */
    private FieldType type;

    /**
     * Whether this field is an aggregate
     */
    private boolean isAggregate;

    /**
     * Aggregate function if this is an aggregate field
     */
    private String aggregateFunction;

    /**
     * Validates the field reference
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new QueryModelValidationException("Field name cannot be empty");
        }
    }
}