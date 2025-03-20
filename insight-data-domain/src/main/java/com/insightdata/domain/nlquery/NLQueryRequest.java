package com.insightdata.domain.nlquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自然语言查询请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {
    /**
     * 数据源ID
     */
    private String dataSourceId;

    /**
     * 查询语句
     */
    private String query;

    /**
     * 最大返回行数
     */
    private Integer maxRows;

    /**
     * 超时时间(毫秒)
     */
    private Long timeout;

    /**
     * 是否包含元数据
     */
    private Boolean includeMetadata;

    /**
     * 是否包含执行计划
     */
    private Boolean includeExecutionPlan;

    /**
     * 是否异步执行
     */
    private Boolean async;

    /**
     * 是否缓存结果
     */
    private Boolean cacheResult;

    /**
     * 缓存过期时间(秒)
     */
    private Long cacheExpiration;
}