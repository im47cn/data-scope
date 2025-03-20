package com.insightdata.domain.exception;

/**
 * 数据源异常
 */
public class DataSourceException extends RuntimeException {
    
    public DataSourceException(String message) {
        super(message);
    }
    
        public static DataSourceException alreadyExists(String message) {
            return new DataSourceException(message);
        }
    
        public static DataSourceException notFound(String message) {
            return new DataSourceException(message);
        }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataSourceException(Throwable cause) {
        super(cause);
    }
}