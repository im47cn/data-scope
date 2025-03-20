package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnInfo {
    private String name;
    private String tableName;
    private String dataType;
    private boolean isPrimaryKey;
    private boolean isNullable;
    private String description;

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
}