package com.insightdata.facade.datasource;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data source data transfer object
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceDTO {
    private String id;
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
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private MetadataStats metadataStats;
    private LocalDateTime lastSyncTime;
    private String lastSyncStatus;
}