package com.insightdata.security;

/**
 * 密钥管理异常
 */
public class KeyManagementException extends RuntimeException {
    
    public KeyManagementException(String message) {
        super(message);
    }
    
    public KeyManagementException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public KeyManagementException(Throwable cause) {
        super(cause);
    }
}