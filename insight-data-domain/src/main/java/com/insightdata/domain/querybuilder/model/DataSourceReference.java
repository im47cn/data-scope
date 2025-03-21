package com.insightdata.domain.querybuilder.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reference to a data source used in a query
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceReference {
    /**
     * Unique identifier for the data source
     */
    private String id;

    /**
     * Name of the data source
     */
    private String name;

    /**
     * Type of the data source
     */
    private String type;

    /**
     * Schema name in the data source
     */
    private String schema;
}