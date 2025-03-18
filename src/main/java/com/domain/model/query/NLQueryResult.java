package com.domain.model.query;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class NLQueryResult {
    private String queryId;
    private String originalQuery;
    private String generatedSql;
    private List<String> columns;
    private List<String> columnTypes;
    private List<Map<String, Object>> data;
    private long totalRows;
    private long executionTime;
    private String status;
    private double confidence;
    private String errorMessage;
}