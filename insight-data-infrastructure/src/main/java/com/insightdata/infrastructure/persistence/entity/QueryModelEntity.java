package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.insightdata.domain.querybuilder.enums.ModelStatus;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class QueryModelEntity {
    private String id;
    private String name;
    private String description;
    private String dataSources; // Store as JSON string
    private String tables; // Store as JSON string
    private String fields; // Store as JSON string
    private String joins; // Store as JSON string
    private String rootFilter; // Store as JSON string
    private String groupBy; // Store as JSON string
    private String orderBy; // Store as JSON string
    private String parameters; // Store as JSON string
    private String options; // Store as JSON string
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private boolean isPublic;
    private List<String> tags; // Store as JSON string
    private ModelStatus status;
}