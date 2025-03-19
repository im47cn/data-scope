package com.insightdata.domain.metadata.model;

import lombok.Data;

import java.util.List;

@Data
public class TableInfo {
    private String name;
    private String schemaName;
    private long rowCount;
    private long dataSize;
    private long indexSize;
    private List<ColumnInfo> columns;
    private List<ForeignKeyInfo> foreignKeys;
    private String description;


    public List<ColumnInfo> getColumns() {
        return this.columns;
    }

    public List<ForeignKeyInfo> getForeignKeys() {
        return this.foreignKeys;
    }

    // Added based on error messages
    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }
    public void setIndexSize(long indexSize) {
        this.indexSize = indexSize;
    }

    // Added based on error messages in TableRelationshipServiceImpl
    public String getName(){
        return this.name;
    }

    // Added based on error messages in MetadataBasedEntityExtractor
    public String getDescription() {
        return description;
    }
}