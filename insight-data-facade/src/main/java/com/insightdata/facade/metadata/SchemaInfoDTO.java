package com.insightdata.facade.metadata;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库模式信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfoDTO {
    
    /**
     * 模式ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 表数量
     */
    private Integer tableCount;
    
    /**
     * 视图数量
     */
    private Integer viewCount;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}