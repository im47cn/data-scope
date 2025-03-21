package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a group of filters combined with a logical operator (AND, OR).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterGroup {

    /**
     * Unique identifier for the filter group.
     */
    private String id;

    /**
     * The logical operator to combine the filters (AND, OR).
     */
    private String operator;

    /**
     * List of filters in this group. Can include both simple filters and nested filter groups.
     */
    private List<Object> filters; // Can contain Filter or FilterGroup

    /**
     * Validates the filter group.
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (operator == null || operator.trim().isEmpty()) {
            throw new QueryModelValidationException("Filter group operator cannot be empty");
        }
        if (!operator.equalsIgnoreCase("AND") && !operator.equalsIgnoreCase("OR")) {
            throw new QueryModelValidationException("Invalid filter group operator: " + operator);
        }
        if (filters == null || filters.isEmpty()) {
            throw new QueryModelValidationException("Filter group must contain at least one filter or nested group");
        }
        for (Object filter : filters) {
            if (filter instanceof Filter) {
                ((Filter) filter).validate();
            } else if (filter instanceof FilterGroup) {
                ((FilterGroup) filter).validate();
            } else {
                throw new QueryModelValidationException("Invalid filter type in group: " + filter.getClass().getName());
            }
        }
    }
}