package com.insightdata.domain.datasource.enums;

/**
 * 数据源类型
 */
public enum DataSourceType {
    /**
     * MySQL数据库
     */
    MYSQL("MySQL"),

    /**
     * PostgreSQL数据库
     */
    POSTGRESQL("PostgreSQL"),

    /**
     * Oracle数据库
     */
    ORACLE("Oracle"),

    /**
     * SQL Server数据库
     */
    SQLSERVER("SQL Server"),

    /**
     * DB2数据库
     */
    DB2("DB2"),

    /**
     * Hive数据仓库
     */
    HIVE("Hive"),

    /**
     * ClickHouse数据库
     */
    CLICKHOUSE("ClickHouse"),

    /**
     * Doris数据库
     */
    DORIS("Doris"),

    /**
     * Presto查询引擎
     */
    PRESTO("Presto"),

    /**
     * Trino查询引擎
     */
    TRINO("Trino");

    private final String displayName;

    DataSourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}