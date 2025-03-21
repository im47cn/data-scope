package com.insightdata.facade.querybuilder.model;

import lombok.Data;

@Data
public class TableReference {
    private String schema;
    private String name;
    private String alias;
    private String description;
    private String[] primaryKeys;
    private String[] uniqueKeys;
    private String[] foreignKeys;
    private String[] indexes;
    private long rowCount;
    private long sizeInBytes;
    private String lastUpdated;
}