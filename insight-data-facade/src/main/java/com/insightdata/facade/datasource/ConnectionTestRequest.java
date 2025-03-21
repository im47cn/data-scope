package com.insightdata.facade.datasource;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request for testing data source connection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestRequest {
    private DataSourceType type;
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private Map<String, Object> properties;
    private boolean validateSchema;
    private boolean validateTables;
    private boolean validatePrivileges;
}