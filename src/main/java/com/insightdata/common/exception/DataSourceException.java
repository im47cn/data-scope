package com.insightdata.common.exception;

/**
 * 数据源异常类
 */
public class DataSourceException extends InsightDataException {
    
    public static final String CONNECTION_ERROR = "DS_CONNECTION_ERROR";
    public static final String AUTHENTICATION_ERROR = "DS_AUTH_ERROR";
    public static final String INVALID_CONFIG = "DS_INVALID_CONFIG";
    public static final String METADATA_EXTRACTION_ERROR = "DS_METADATA_ERROR";
    public static final String SYNC_ERROR = "DS_SYNC_ERROR";
    public static final String NOT_FOUND = "DS_NOT_FOUND";
    public static final String ALREADY_EXISTS = "DS_ALREADY_EXISTS";
    
    public DataSourceException(String message) {
        super(message);
    }
    
    public DataSourceException(String errorCode, String message) {
        super(errorCode, message);
    }
    
    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DataSourceException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
    
    /**
     * 创建连接错误异常
     */
    public static DataSourceException connectionError(String message) {
        return new DataSourceException(CONNECTION_ERROR, message);
    }
    
    /**
     * 创建连接错误异常
     */
    public static DataSourceException connectionError(String message, Throwable cause) {
        return new DataSourceException(CONNECTION_ERROR, message, cause);
    }
    
    /**
     * 创建认证错误异常
     */
    public static DataSourceException authenticationError(String message) {
        return new DataSourceException(AUTHENTICATION_ERROR, message);
    }
    
    /**
     * 创建认证错误异常
     */
    public static DataSourceException authenticationError(String message, Throwable cause) {
        return new DataSourceException(AUTHENTICATION_ERROR, message, cause);
    }
    
    /**
     * 创建配置无效异常
     */
    public static DataSourceException invalidConfig(String message) {
        return new DataSourceException(INVALID_CONFIG, message);
    }
    
    /**
     * 创建元数据提取错误异常
     */
    public static DataSourceException metadataExtractionError(String message) {
        return new DataSourceException(METADATA_EXTRACTION_ERROR, message);
    }
    
    /**
     * 创建元数据提取错误异常
     */
    public static DataSourceException metadataExtractionError(String message, Throwable cause) {
        return new DataSourceException(METADATA_EXTRACTION_ERROR, message, cause);
    }
    
    /**
     * 创建同步错误异常
     */
    public static DataSourceException syncError(String message) {
        return new DataSourceException(SYNC_ERROR, message);
    }
    
    /**
     * 创建同步错误异常
     */
    public static DataSourceException syncError(String message, Throwable cause) {
        return new DataSourceException(SYNC_ERROR, message, cause);
    }
    
    /**
     * 创建数据源不存在异常
     */
    public static DataSourceException notFound(String message) {
        return new DataSourceException(NOT_FOUND, message);
    }
    
    /**
     * 创建数据源已存在异常
     */
    public static DataSourceException alreadyExists(String message) {
        return new DataSourceException(ALREADY_EXISTS, message);
    }
}