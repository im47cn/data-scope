package com.insightdata.facade.querybuilder.model;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class DataSource {
    private String id;
    private String name;
    private String description;
    private DataSourceType type;
    private String host;
    private Integer port;
    private String database;
    private String username;
    private String password;
    private Map<String, String> properties;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
}