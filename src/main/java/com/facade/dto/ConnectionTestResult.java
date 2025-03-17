package com.facade.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源连接测试结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConnectionTestResult {
    
    /**
     * 是否连接成功
     */
    private Boolean success;
    
    /**
     * 结果消息
     */
    private String message;
    
    /**
     * 连接耗时（毫秒）
     */
    private Long connectionTime;
    
    /**
     * 详细错误信息（如果连接失败）
     */
    private String errorDetails;
    
    /**
     * 数据库版本信息（如果连接成功）
     */
    private String databaseVersion;
    
    /**
     * 数据库时区（如果连接成功）
     */
    private String databaseTimezone;
}