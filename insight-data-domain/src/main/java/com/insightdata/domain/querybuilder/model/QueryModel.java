package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Core query model class representing a structured query definition.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryModel {
    /**
     * Unique identifier for the query model
     */
    private String id;

    /**
     * Name of the query model
     */
    private String name;

    /**
     * Description of the query's purpose
     */
    private String description;

    /**
     * List of data sources used in the query
     */
    private List<DataSourceReference> dataSources;

    /**
     * List of table references used in the query
     */
    private List<TableReference> tables;

    /**
     * List of field selections
     */
    private List<FieldSelection> fields;

    /**
     * List of join definitions between tables
     */
    private List<JoinDefinition> joins;

    /**
     * Root filter group for WHERE conditions
     */
    private FilterGroup rootFilter;

    /**
     * List of GROUP BY expressions
     */
    private List<GroupingExpression> groupBy;

    /**
     * List of ORDER BY expressions
     */
    private List<SortExpression> orderBy;

    /**
     * Query parameters
     */
    private Map<String, ParameterDefinition> parameters;

    /**
     * Additional options for query execution
     */
    private Map<String, Object> options;

    /**
     * Creation timestamp
     */
    private LocalDateTime createdAt;

    /**
     * Last update timestamp
     */
    private LocalDateTime updatedAt;

    /**
     * Creator user ID
     */
    private String createdBy;

    /**
     * Last updater user ID
     */
    private String updatedBy;

    /**
     * Whether this query model is public
     */
    private boolean isPublic;

    /**
     * Tags for categorization
     */
    private List<String> tags;

    /**
     * Current status of the query model
     */
    private ModelStatus status;

    /**
     * Validates the query model structure and relationships
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        validateBasicProperties();
        validateDataSources();
        validateTables();
        validateFields();
        validateJoins();
        validateFilters();
        validateParameters();
    }

    private void validateBasicProperties() throws QueryModelValidationException {
        if (name == null || name.trim().isEmpty()) {
            throw new QueryModelValidationException("Query model name cannot be empty");
        }
    }

    private void validateDataSources() throws QueryModelValidationException {
        if (dataSources == null || dataSources.isEmpty()) {
            throw new QueryModelValidationException("At least one data source must be specified");
        }
    }

    private void validateTables() throws QueryModelValidationException {
        if (tables == null || tables.isEmpty()) {
            throw new QueryModelValidationException("At least one table must be specified");
        }
    }

    private void validateFields() throws QueryModelValidationException {
        if (fields == null || fields.isEmpty()) {
            throw new QueryModelValidationException("At least one field must be selected");
        }
    }

    private void validateJoins() throws QueryModelValidationException {
        if (joins != null) {
            for (JoinDefinition join : joins) {
                if (join.getLeftTableId() == null || join.getRightTableId() == null) {
                    throw new QueryModelValidationException("Join must specify both left and right tables");
                }
            }
        }
    }

    private void validateFilters() throws QueryModelValidationException {
        if (rootFilter != null) {
            rootFilter.validate();
        }
    }

    private void validateParameters() throws QueryModelValidationException {
        if (parameters != null) {
            for (Map.Entry<String, ParameterDefinition> entry : parameters.entrySet()) {
                if (entry.getValue() == null) {
                    throw new QueryModelValidationException("Parameter definition cannot be null: " + entry.getKey());
                }
                entry.getValue().validate();
            }
        }
    }
}