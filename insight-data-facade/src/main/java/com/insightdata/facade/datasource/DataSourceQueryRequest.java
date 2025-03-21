package com.insightdata.facade.datasource;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for querying data sources
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceQueryRequest {
    private String type;
    private String name;
    private String host;
    private String database;
    private Boolean enabled;
    private Integer page;
    private Integer size;
    private String sortBy;
    private String sortOrder;
}