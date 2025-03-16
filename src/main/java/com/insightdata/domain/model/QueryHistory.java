package com.insightdata.domain.model;

import java.time.LocalDateTime;

import com.insightdata.domain.model.base.BaseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 查询历史
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class QueryHistory extends BaseEntity {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 执行时长(毫秒)
     */
    private long duration;
    
    /**
     * 返回行数
     */
    private int rowCount;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 执行时间
     */
    private LocalDateTime executeTime;
    
    /**
     * 是否来自缓存
     */
    private boolean fromCache;
    
    /**
     * 缓存过期时间
     */
    private LocalDateTime cacheExpireTime;
    
    /**
     * 执行的SQL
     */
    private String executedSql;
    
    /**
     * 执行参数
     */
    private String executedParams;
    
    /**
     * 执行计划
     */
    private String executionPlan;
    
    /**
     * 影响行数
     */
    private int affectedRows;
    
    /**
     * 是否超时
     */
    private boolean timeout;
    
    /**
     * 超时时间(毫秒)
     */
    private long timeoutMillis;
}