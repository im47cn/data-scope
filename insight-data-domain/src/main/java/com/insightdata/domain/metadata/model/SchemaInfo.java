package com.insightdata.domain.metadata.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据库模式信息
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SchemaInfo {

    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 模式名称
     */
    private String name;
    
    /**
     * 模式描述
     */
    private String description;
    
    /**
     * 表信息列表
     */
    private List<TableInfo> tables;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 是否默认模式
     */
    private Boolean isDefault;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}