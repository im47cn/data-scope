package com.insightdata.domain.metadata.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {
    private String name;
    private String dataSourceId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<TableInfo> tables;
}