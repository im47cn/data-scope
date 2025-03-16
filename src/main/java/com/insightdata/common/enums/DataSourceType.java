package com.insightdata.common.enums;

/**
 * 数据源类型枚举
 */
public enum DataSourceType {
    /**
     * MySQL数据库
     */
    MYSQL("mysql", "MySQL数据库"),
    
    /**
     * PostgreSQL数据库
     */
    POSTGRESQL("postgresql", "PostgreSQL数据库"),
    
    /**
     * Oracle数据库
     */
    ORACLE("oracle", "Oracle数据库"),
    
    /**
     * SQL Server数据库
     */
    SQLSERVER("sqlserver", "SQL Server数据库"),
    
    /**
     * DB2数据库
     */
    DB2("db2", "DB2数据库"),
    
    /**
     * SQLite数据库
     */
    SQLITE("sqlite", "SQLite数据库"),
    
    /**
     * H2数据库
     */
    H2("h2", "H2数据库"),
    
    /**
     * HSQLDB数据库
     */
    HSQLDB("hsqldb", "HSQLDB数据库"),
    
    /**
     * Derby数据库
     */
    DERBY("derby", "Derby数据库");
    
    /**
     * 类型编码
     */
    private final String code;
    
    /**
     * 类型描述
     */
    private final String description;
    
    DataSourceType(String code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据类型编码获取类型枚举
     */
    public static DataSourceType fromCode(String code) {
        for (DataSourceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return null;
    }
}