package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a parameter definition in a query model.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterDefinition {

    /**
     * Unique identifier for the parameter.
     */
    private String id;

    /**
     * Name of the parameter.
     */
    private String name;

    /**
     * Data type of the parameter.
     */
    private FieldType type;

    /**
     * Optional default value for the parameter.
     */
    private Object defaultValue;

    /**
     * Indicates if the parameter is required.
     */
    private boolean required;

    /**
     * Optional description of the parameter.
     */
    private String description;

    /**
     * Validates the parameter definition.
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new QueryModelValidationException("Parameter name cannot be empty");
        }
        if (type == null) {
            throw new QueryModelValidationException("Parameter type cannot be null");
        }
    }
}