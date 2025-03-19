package com.insightdata.domain.metadata.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IndexColumnInfo {
    // Placeholder: Add fields as needed
    private String name;
    private String columnName;
    private int ordinalPosition;
}