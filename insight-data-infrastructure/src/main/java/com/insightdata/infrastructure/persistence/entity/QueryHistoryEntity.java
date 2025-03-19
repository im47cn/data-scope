package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询历史实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistoryEntity {
    
    /**
     * 查询历史ID
     */
    private String id;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户名
     */
    private String userName;
    
    /**
     * 接口配置ID
     */
    private String interfaceConfigId;
    
    /**
     * 接口名称
     */
    private String interfaceName;
    
    /**
     * 接口版本
     */
    private String interfaceVersion;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 原始SQL
     */
    private String originalSql;
    
    /**
     * 执行的SQL
     */
    private String executedSql;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 执行时间(毫秒)
     */
    private Long executionTimeMs;
    
    /**
     * 结果行数
     */
    private Integer resultRowCount;
    
    /**
     * 状态
     */
    private String status;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 错误代码
     */
    private String errorCode;
    
    /**
     * 是否已导出
     */
    private Boolean exported;
    
    /**
     * 导出格式
     */
    private String exportFormat;
    
    /**
     * 导出行数
     */
    private Integer exportedRowCount;
    
    /**
     * 客户端IP
     */
    private String clientIp;
    
    /**
     * 用户代理
     */
    private String userAgent;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 是否收藏
     */
    private Boolean favorite;
    
    /**
     * 标题
     */
    private String title;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 标签
     */
    private String tags;
    
    /**
     * 查询来源
     */
    private String querySource;
    
    /**
     * 是否命中缓存
     */
    private Boolean cacheHit;
    
    /**
     * 缓存键
     */
    private String cacheKey;
    
    /**
     * API请求ID
     */
    private String apiRequestId;
    
    /**
     * 相似查询ID
     */
    private String similarQueryId;
    
    /**
     * 相似度分数
     */
    private Double similarityScore;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 查询参数
     */
    private Map<String, Object> queryParams;
    
    /**
     * 性能统计
     */
    private Map<String, Object> performanceStats;
}