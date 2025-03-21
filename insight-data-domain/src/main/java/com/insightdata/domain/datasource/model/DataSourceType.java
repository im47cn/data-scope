package com.insightdata.domain.datasource.model;

/**
 * Enumeration of supported data source types
 */
public enum DataSourceType {
    MYSQL("MySQL"),
    POSTGRESQL("PostgreSQL"),
    ORACLE("Oracle"),
    SQLSERVER("SQL Server"),
    DB2("DB2"),
    HIVE("Hive"),
    CLICKHOUSE("ClickHouse"),
    DORIS("Doris"),
    OTHER("Other");

    private final String displayName;

    DataSourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static DataSourceType fromString(String type) {
        try {
            return valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}