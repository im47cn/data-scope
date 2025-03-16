package com.insightdata.common.exception;

/**
 * 数据源异常
 */
public class DataSourceException extends InsightDataException {
    
    private static final long serialVersionUID = 1L;

    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 数据源不存在
     */
    public static DataSourceException notFound(String message) {
        return new DataSourceException("Data source not found: " + message);
    }

    /**
     * 数据源已存在
     */
    public static DataSourceException alreadyExists(String message) {
        return new DataSourceException("Data source already exists: " + message);
    }

    /**
     * 数据源配置无效
     */
    public static DataSourceException invalidConfig(String message) {
        return new DataSourceException("Invalid data source config: " + message);
    }

    /**
     * 数据源连接失败
     */
    public static DataSourceException connectionFailed(String message) {
        return new DataSourceException("Failed to connect to data source: " + message);
    }

    /**
     * 数据源连接失败
     */
    public static DataSourceException connectionFailed(String message, Throwable cause) {
        return new DataSourceException("Failed to connect to data source: " + message, cause);
    }

    /**
     * 数据源连接错误
     */
    public static DataSourceException connectionError(String message, Throwable cause) {
        return new DataSourceException("Connection error: " + message, cause);
    }

    /**
     * 数据源查询失败
     */
    public static DataSourceException queryFailed(String message) {
        return new DataSourceException("Failed to execute query: " + message);
    }

    /**
     * 数据源查询失败
     */
    public static DataSourceException queryFailed(String message, Throwable cause) {
        return new DataSourceException("Failed to execute query: " + message, cause);
    }

    /**
     * 数据源同步失败
     */
    public static DataSourceException syncFailed(String message) {
        return new DataSourceException("Failed to sync data source: " + message);
    }

    /**
     * 数据源同步失败
     */
    public static DataSourceException syncFailed(String message, Throwable cause) {
        return new DataSourceException("Failed to sync data source: " + message, cause);
    }

    /**
     * 元数据提取错误
     */
    public static DataSourceException metadataExtractionError(String message) {
        return new DataSourceException("Failed to extract metadata: " + message);
    }

    /**
     * 元数据提取错误
     */
    public static DataSourceException metadataExtractionError(String message, Throwable cause) {
        return new DataSourceException("Failed to extract metadata: " + message, cause);
    }
}