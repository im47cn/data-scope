package com.insightdata.infrastructure.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 组件配置实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentConfigEntity {
    
    /**
     * 配置ID
     */
    private String id;
    
    /**
     * 组件名称
     */
    private String name;
    
    /**
     * 组件类型
     */
    private String type;
    
    /**
     * 组件描述
     */
    private String description;
    
    /**
     * 关联的查询ID
     */
    private Long queryId;
    
    /**
     * 组件属性配置
     */
    private Map<String, String> properties;
    
    /**
     * 组件样式配置
     */
    private Map<String, String> style;
    
    /**
     * 数据源配置
     */
    private Map<String, String> dataSourceConfig;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 更新人
     */
    private String updatedBy;
}