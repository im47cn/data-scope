package com.insightdata.facade.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 模式响应DTO
 */
@Data
public class SchemaResponse {
    
    /**
     * 模式ID
     */
    private Long id;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
