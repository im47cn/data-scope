package com.insightdata.domain.metadata.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ColumnInfo {
    private String name;
    private String tableName;
    private String dataType;
    private boolean isPrimaryKey;
    private boolean isNullable;
    private String description; // Added based on error messages

    // Getters and setters are automatically generated by Lombok's @Data annotation

    // Added based on error messages
    public boolean isNumeric() {
        return dataType.equalsIgnoreCase("INT") ||
               dataType.equalsIgnoreCase("BIGINT") ||
               dataType.equalsIgnoreCase("FLOAT") ||
               dataType.equalsIgnoreCase("DOUBLE") ||
               dataType.equalsIgnoreCase("DECIMAL");
    }

    public boolean isString() {
        return dataType.equalsIgnoreCase("VARCHAR") ||
               dataType.equalsIgnoreCase("CHAR") ||
               dataType.equalsIgnoreCase("TEXT");
    }

    public boolean isDateTime() {
        return dataType.equalsIgnoreCase("DATE") ||
               dataType.equalsIgnoreCase("TIME") ||
               dataType.equalsIgnoreCase("DATETIME") ||
               dataType.equalsIgnoreCase("TIMESTAMP");
    }

    public String getDescription(){
        return this.description;
    }
}