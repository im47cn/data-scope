package com.insightdata.domain.nlquery;

import com.insightdata.domain.datasource.SchemaInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 查询上下文
 */
@Data
@Builder
public class QueryContext {

    /**
     * 数据源ID
     */
    private String dataSourceId;

    /**
     * 数据源元数据
     */
    private SchemaInfo metadata;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 会话ID
     */
    private String sessionId;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 超时时间(毫秒)
     */
    private Long timeout;

    /**
     * 是否缓存
     */
    private boolean enableCache;

    /**
     * 最大返回行数
     */
    private Integer maxRows;

    /**
     * 创建一个基本的查询上下文
     */
    public static QueryContext basic(String dataSourceId, SchemaInfo metadata) {
        return QueryContext.builder()
                .dataSourceId(dataSourceId)
                .metadata(metadata)
                .createTime(System.currentTimeMillis())
                .timeout(30000L)
                .enableCache(true)
                .maxRows(1000)
                .build();
    }

    /**
     * 创建一个带用户信息的查询上下文
     */
    public static QueryContext withUser(String dataSourceId, SchemaInfo metadata, Long userId) {
        return QueryContext.builder()
                .dataSourceId(dataSourceId)
                .metadata(metadata)
                .userId(userId)
                .createTime(System.currentTimeMillis())
                .timeout(30000L)
                .enableCache(true)
                .maxRows(1000)
                .build();
    }

    /**
     * 创建一个完整的查询上下文
     */
    public static QueryContext complete(String dataSourceId, SchemaInfo metadata, Long userId,
            String sessionId, Long timeout, boolean enableCache, Integer maxRows) {
        return QueryContext.builder()
                .dataSourceId(dataSourceId)
                .metadata(metadata)
                .userId(userId)
                .sessionId(sessionId)
                .createTime(System.currentTimeMillis())
                .timeout(timeout)
                .enableCache(enableCache)
                .maxRows(maxRows)
                .build();
    }
}