package com.facade.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存的查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQueryDTO {
    
    /**
     * 查询ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
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
     * 参数定义
     */
    private Map<String, Object> parameterDefinitions;
    
    /**
     * 默认参数值
     */
    private Map<String, Object> defaultParameters;
    
    /**
     * 文件夹路径
     */
    private String folderPath;
    
    /**
     * 是否共享
     */
    private Boolean isShared;
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
    /**
     * 创建者ID
     */
    private String createdById;
    
    /**
     * 创建者名称
     */
    private String createdByName;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecutedAt;
    
    /**
     * 执行次数
     */
    private Long executionCount;
    
    /**
     * 标签
     */
    private String[] tags;
}