package com.insightdata.domain.nlquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {
    /**
     * 查询ID
     */
    private String queryId;

    /**
     * 执行的SQL语句
     */
    private String sql;

    /**
     * 列标签
     */
    private List<String> columnLabels;

    /**
     * 数据行
     */
    private List<Map<String, Object>> rows;

    /**
     * 总行数
     */
    private Integer totalRows;

    /**
     * 执行时间(毫秒)
     */
    private Long duration;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 执行计划
     */
    private String executionPlan;

    /**
     * 是否来自缓存
     */
    private Boolean fromCache;

    /**
     * 缓存时间
     */
    private LocalDateTime cachedAt;

    /**
     * 缓存过期时间
     */
    private LocalDateTime cacheExpireAt;
}