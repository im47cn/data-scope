package com.facade.dto;

import lombok.Data;

@Data
public class TableRelationshipDTO {
    private String id;
    private String dataSourceId;
    private String sourceTable;
    private String sourceColumn;
    private String targetTable;
    private String targetColumn;
    private String type;
    private String source;
    private Double weight;
    private Integer frequency;
//    private LocalDateTime createdAt;
//    private LocalDateTime updatedAt;
}