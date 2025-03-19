package com.insightdata.domain.metadata.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SchemaInfo {
    private String name;
    private String dataSourceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TableInfo> tables;

    public List<TableInfo> getTables() {
        return this.tables;
    }

    public String getName() {
        return name;
    }
}