package com.insightdata.domain.metadata.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfo {
    private String name;
    private String schemaName;
    private long rowCount;
    private long dataSize;
    private long indexSize;
    private List<ColumnInfo> columns;
    private List<ForeignKeyInfo> foreignKeys;
    private String description;
}