package com.facade.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库表信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TableInfoDTO {
    
    /**
     * 表ID
     */
    private String id;
    
    /**
     * 模式ID
     */
    private String schemaId;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 表名
     */
    private String name;
    
    /**
     * 表类型 (TABLE, VIEW)
     */
    private String type;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 列数量
     */
    private Integer columnCount;
    
    /**
     * 记录数量（估计值）
     */
    private Long rowCount;
    
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
    
    /**
     * 表是否有主键
     */
    private Boolean hasPrimaryKey;
    
    /**
     * 最近使用时间
     */
    private LocalDateTime lastUsedAt;
}