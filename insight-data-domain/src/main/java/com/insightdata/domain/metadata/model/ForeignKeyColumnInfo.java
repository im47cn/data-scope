package com.insightdata.domain.metadata.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ForeignKeyColumnInfo {
    private String sourceColumnName;
    private String targetColumnName;
}