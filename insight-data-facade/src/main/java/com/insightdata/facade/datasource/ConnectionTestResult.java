package com.insightdata.facade.datasource;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of testing data source connection
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestResult {
    private boolean success;
    private String message;
    private long duration;
    private List<String> schemas;
    private Map<String, List<String>> tables;
    private Map<String, String> privileges;
    private Map<String, Object> metadata;
    private List<String> warnings;
    private List<String> errors;
}