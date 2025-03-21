package com.insightdata.facade.datasource;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for creating a new data source
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceCreateRequest {
    private String name;
    private String description;
    private DataSourceType type;
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private Map<String, Object> properties;
    private boolean enabled;
    private boolean testConnection;
    private boolean syncMetadata;
}