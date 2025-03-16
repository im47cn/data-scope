package com.insightdata.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.insightdata.domain.model.base.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 保存的查询
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SavedQuery extends BaseEntity {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 查询参数
     */
    private List<String> parameters;
    
    /**
     * 查询标签
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 是否公开
     */
    private boolean isPublic;
    
    /**
     * 创建人ID
     */
    private Long createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    /**
     * 最后执行时间
     */
    private LocalDateTime lastExecuteTime;
    
    /**
     * 执行次数
     */
    private int executeCount;
    
    /**
     * 平均执行时长(毫秒)
     */
    private long avgExecuteTime;
    
    /**
     * 最大执行时长(毫秒)
     */
    private long maxExecuteTime;
    
    /**
     * 最小执行时长(毫秒)
     */
    private long minExecuteTime;
    
    /**
     * 是否启用缓存
     */
    private boolean useCache;
    
    /**
     * 缓存过期时间(秒)
     */
    private int cacheExpireSeconds;
}