package com.common.enums;

/**
 * 数据源类型枚举
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
     * 根据显示名称获取枚举值
     */
    public static DataSourceType fromDisplayName(String displayName) {
        for (DataSourceType type : values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown data source type: " + displayName);
    }
}