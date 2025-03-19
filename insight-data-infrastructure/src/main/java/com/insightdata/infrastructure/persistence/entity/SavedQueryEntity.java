package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存的查询实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQueryEntity {
    
    /**
     * 查询ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * 参数列表
     */
    private List<String> parameters;
    
    /**
     * 默认参数值
     */
    private Map<String, Object> defaultValues;
    
    /**
     * 标签
     */
    private List<String> tags;
    
    /**
     * 是否公开
     */
    private Boolean isPublic;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 使用次数
     */
    private Integer usageCount;
    
    /**
     * 平均执行时间(毫秒)
     */
    private Double averageExecutionTime;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;
}