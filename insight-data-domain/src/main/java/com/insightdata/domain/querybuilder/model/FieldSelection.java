package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a field selected in a query, potentially with an alias or expression.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FieldSelection {

    /**
     * Unique identifier for this field selection.
     */
    private String id;

    /**
     * Reference to the field being selected.
     */
    private FieldReference field;

    /**
     * Optional alias for the selected field.
     */
    private String alias;

    /**
     * Optional expression to compute the field value.
     * If present, this overrides the field reference.
     */
    private String expression;

    /**
     * Validates the field selection.
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (field == null && (expression == null || expression.trim().isEmpty())) {
            throw new QueryModelValidationException("Field selection must specify either a field reference or an expression");
        }

        if (field != null) {
            field.validate();
        }
    }
}