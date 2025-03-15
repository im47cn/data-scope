package com.insightdata.facade.rest.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 表响应DTO
 */
@Data
public class TableResponse {
    
    /**
     * 表ID
     */
    private Long id;
    
    /**
     * 表名称
     */
    private String name;
    
    /**
     * 表类型
     */
    private String type;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 估计行数
     */
    private Long estimatedRowCount;
    
    /**
     * 数据大小（字节）
     */
    private Long dataSize;
    
    /**
     * 索引大小（字节）
     */
    private Long indexSize;
    
    /**
     * 最后分析时间
     */
    private LocalDateTime lastAnalyzed;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
