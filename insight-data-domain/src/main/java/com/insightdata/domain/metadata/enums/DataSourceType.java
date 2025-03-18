package com.insightdata.domain.metadata.enums;

/**
 * 数据源类型枚举
 *
 * @author dreambt
 * @since 1.0.0
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
    DORIS("Doris");

    private final String displayName;

    DataSourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * 根据名称获取数据源类型
     *
     * @param name 数据源类型名称
     * @return 数据源类型
     */
    public static DataSourceType fromName(String name) {
        for (DataSourceType type : values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unsupported data source type: " + name);
    }

    /**
     * 判断是否支持该数据源类型
     *
     * @param name 数据源类型名称
     * @return 是否支持
     */
    public static boolean isSupported(String name) {
        try {
            fromName(name);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}