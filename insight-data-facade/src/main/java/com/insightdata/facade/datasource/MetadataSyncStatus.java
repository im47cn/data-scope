package com.insightdata.facade.datasource;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Status of metadata sync job
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSyncStatus {
    private String id;
    private String dataSourceId;
    private String type;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int progress;
    private int totalItems;
    private int processedItems;
    private Map<String, Object> parameters;
    private String errorMessage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}