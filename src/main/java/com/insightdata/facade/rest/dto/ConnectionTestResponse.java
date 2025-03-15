package com.insightdata.facade.rest.dto;

import lombok.Data;

/**
 * 连接测试响应DTO
 */
@Data
public class ConnectionTestResponse {
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 消息
     */
    private String message;
}
