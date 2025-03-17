package com.facade.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 连接测试响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestResponse {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 消息
     */
    private String message;
    
    /**
     * 数据库版本
     */
    private String databaseVersion;
    
    /**
     * 驱动版本
     */
    private String driverVersion;
    
    /**
     * 连接时间(毫秒)
     */
    private long connectionTime;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 错误堆栈
     */
    private String errorStack;
    
    /**
     * 创建一个成功的响应
     */
    public static ConnectionTestResponse success(String databaseVersion, String driverVersion, long connectionTime) {
        return ConnectionTestResponse.builder()
                .success(true)
                .message("连接成功")
                .databaseVersion(databaseVersion)
                .driverVersion(driverVersion)
                .connectionTime(connectionTime)
                .build();
    }
    
    /**
     * 创建一个失败的响应
     */
    public static ConnectionTestResponse failed(String message, String errorCode, String errorStack) {
        return ConnectionTestResponse.builder()
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .errorStack(errorStack)
                .build();
    }
    
    /**
     * 创建一个超时的响应
     */
    public static ConnectionTestResponse timeout(long connectionTime) {
        return ConnectionTestResponse.builder()
                .success(false)
                .message("连接超时")
                .connectionTime(connectionTime)
                .build();
    }
    
    /**
     * 创建一个驱动未找到的响应
     */
    public static ConnectionTestResponse driverNotFound(String driverVersion) {
        return ConnectionTestResponse.builder()
                .success(false)
                .message("数据库驱动未找到")
                .driverVersion(driverVersion)
                .build();
    }
}
