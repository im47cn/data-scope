package com.insightdata.domain.datasource.enums;

/**
 * 数据源类型
 */
public enum DataSourceType {
    /**
     * MySQL数据库
     */
    MYSQL("MySQL", "mysql", "com.mysql.cj.jdbc.Driver"),

    /**
     * PostgreSQL数据库
     */
    POSTGRESQL("PostgreSQL", "postgresql", "org.postgresql.Driver"),

    /**
     * Oracle数据库
     */
    ORACLE("Oracle", "oracle:thin", "oracle.jdbc.OracleDriver"),

    /**
     * SQL Server数据库
     */
    SQLSERVER("SQL Server", "sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),

    /**
     * DB2数据库
     */
    DB2("DB2", "db2", "com.ibm.db2.jcc.DB2Driver"),

    /**
     * Hive数据仓库
     */
    HIVE("Hive", "hive2", "org.apache.hive.jdbc.HiveDriver"),

    /**
     * ClickHouse数据库
     */
    CLICKHOUSE("ClickHouse", "clickhouse", "ru.yandex.clickhouse.ClickHouseDriver"),

    /**
     * Doris数据库
     */
    DORIS("Doris", "mysql", "com.mysql.cj.jdbc.Driver"),

    /**
     * Presto查询引擎
     */
    PRESTO("Presto", "presto", "com.facebook.presto.jdbc.PrestoDriver"),

    /**
     * Trino查询引擎
     */
    TRINO("Trino", "trino", "io.trino.jdbc.TrinoDriver");

    private final String displayName;
    private final String protocol;
    private final String driverClassName;

    DataSourceType(String displayName, String protocol, String driverClassName) {
        this.displayName = displayName;
        this.protocol = protocol;
        this.driverClassName = driverClassName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String buildJdbcUrl(String host, int port, String database) {
        return String.format("jdbc:%s://%s:%d/%s", protocol, host, port, database);
    }
}