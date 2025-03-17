package com.common.exception;

/**
 * 系统基础异常类
 */
public class InsightDataException extends RuntimeException {
    
    private final String errorCode;
    
    public InsightDataException(String message) {
        super(message);
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public InsightDataException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public InsightDataException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "SYSTEM_ERROR";
    }
    
    public InsightDataException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}