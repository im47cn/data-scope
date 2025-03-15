package com.insightdata.common.enums;

/**
 * 数据源类型枚举
 */
public enum DataSourceType {
    /**
     * MySQL数据库
     */
    MYSQL("MySQL"),
    
    /**
     * DB2数据库
     */
    DB2("DB2"),
    
    /**
     * Oracle数据库
     */
    ORACLE("Oracle"),
    
    /**
     * SQL Server数据库
     */
    SQL_SERVER("SQL Server"),
    
    /**
     * PostgreSQL数据库
     */
    POSTGRESQL("PostgreSQL");
    
    private final String displayName;
    
    DataSourceType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}