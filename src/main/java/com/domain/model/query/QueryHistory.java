package com.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 查询历史记录
 * 记录用户的查询历史，包括查询条件、结果和性能统计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryHistory {
    
    /**
     * 历史记录ID
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * 查询界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 查询界面配置名称
     */
    private String interfaceName;
    
    /**
     * 查询界面配置版本
     */
    private String interfaceVersion;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 数据源名称
     */
    private String dataSourceName;
    
    /**
     * 查询条件参数
     */
    @Builder.Default
    private Map<String, Object> queryParams = new HashMap<>();
    
    /**
     * 原始SQL语句
     */
    private String originalSql;
    
    /**
     * 实际执行的SQL语句（包含参数替换后）
     */
    private String executedSql;
    
    /**
     * 查询开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 查询结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 执行时长（毫秒）
     */
    private Long executionTimeMs;
    
    /**
     * 结果行数
     */
    private Long resultRowCount;
    
    /**
     * 查询状态
     * 例如: SUCCESS, FAILED, TIMEOUT, CANCELLED
     */
    private String status;
    
    /**
     * 错误信息（如果查询失败）
     */
    private String errorMessage;
    
    /**
     * 错误代码（如果查询失败）
     */
    private String errorCode;
    
    /**
     * 是否导出数据
     */
    private boolean exported;
    
    /**
     * 导出格式
     */
    private String exportFormat;
    
    /**
     * 导出行数
     */
    private Long exportedRowCount;
    
    /**
     * 客户端IP地址
     */
    private String clientIp;
    
    /**
     * 用户代理信息
     */
    private String userAgent;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 是否是收藏的查询
     */
    private boolean favorite;
    
    /**
     * 查询标题
     * 用户可以为收藏的查询指定标题
     */
    private String title;
    
    /**
     * 查询描述
     * 用户可以为收藏的查询添加描述
     */
    private String description;
    
    /**
     * 标签
     * 用户可以为收藏的查询添加标签
     */
    private String tags;
    
    /**
     * 性能分析数据
     */
    @Builder.Default
    private Map<String, Object> performanceStats = new HashMap<>();
    
    /**
     * 查询来源
     * 例如: MANUAL(手动查询), SCHEDULED(计划任务), API(API调用)
     */
    private String querySource;
    
    /**
     * 是否缓存命中
     */
    private boolean cacheHit;
    
    /**
     * 缓存键
     */
    private String cacheKey;
    
    /**
     * API请求ID
     * 如果是通过API调用，记录对应的请求ID
     */
    private String apiRequestId;
    
    /**
     * 相似查询ID
     * 与此查询相似的历史查询ID
     */
    private Long similarQueryId;
    
    /**
     * 相似度得分
     * 与相似查询的相似度得分
     */
    private Double similarityScore;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
