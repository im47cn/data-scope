package com.insightdata.facade.datasource;

/**
 * Supported data source types
 */
public enum DataSourceType {
    MYSQL,
    POSTGRESQL,
    ORACLE,
    SQLSERVER,
    HIVE,
    CLICKHOUSE,
    DORIS,
    STARROCKS,
    PRESTO,
    TRINO,
    SPARK,
    ELASTICSEARCH,
    MONGODB,
    REDIS,
    KAFKA,
    OTHER;
}