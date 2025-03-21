package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Reference to a table used in a query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableReference {
    /**
     * Unique identifier for the table reference
     */
    private String id;

    /**
     * ID of the data source containing this table
     */
    private String dataSourceId;

    /**
     * Catalog name in the data source
     */
    private String catalogName;

    /**
     * Schema name in the data source
     */
    private String schemaName;

    /**
     * Table name
     */
    private String tableName;

    /**
     * Alias for the table in the query
     */
    private String alias;

    /**
     * Type of the table (BASE_TABLE, VIEW, etc.)
     */
    private TableType type;

    /**
     * Fields selected from this table
     */
    private List<FieldReference> fields;

    /**
     * Validates the table reference
     *
     * @throws QueryModelValidationException if validation fails
     */
    public void validate() throws QueryModelValidationException {
        if (tableName == null || tableName.trim().isEmpty()) {
            throw new QueryModelValidationException("Table name cannot be empty");
        }

        if (dataSourceId == null || dataSourceId.trim().isEmpty()) {
            throw new QueryModelValidationException("Data source ID must be specified");
        }

        if (fields != null) {
            for (FieldReference field : fields) {
                field.validate();
            }
        }
    }

    /**
     * Gets the full qualified name of the table
     */
    public String getFullQualifiedName() {
        StringBuilder sb = new StringBuilder();
        if (catalogName != null && !catalogName.isEmpty()) {
            sb.append(catalogName).append(".");
        }
        if (schemaName != null && !schemaName.isEmpty()) {
            sb.append(schemaName).append(".");
        }
        sb.append(tableName);
        return sb.toString();
    }

    /**
     * Gets the name to use in SQL (alias if specified, otherwise table name)
     */
    public String getSqlName() {
        return alias != null && !alias.isEmpty() ? alias : tableName;
    }
}