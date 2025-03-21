package com.insightdata.facade.datasource;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result of batch deleting data sources
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDeleteResult {
    private boolean success;
    private String message;
    private int totalCount;
    private int successCount;
    private int failureCount;
    private List<String> successIds;
    private List<String> failureIds;
    private List<String> errorMessages;
}