package com.insightdata.domain.metadata.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class IndexInfo {
    // Placeholder: Add fields as needed
    private String name;
    private boolean isUnique;
    private List<String> columns;
}