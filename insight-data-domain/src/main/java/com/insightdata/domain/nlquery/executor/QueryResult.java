package com.insightdata.domain.nlquery.executor;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class QueryResult {

    /**
     * 列名列表
     */
    @Builder.Default
    private List<String> columnLabels = new ArrayList<>();

    /**
     * 列类型列表
     */
    @Builder.Default
    private List<String> columnTypes = new ArrayList<>();

    /**
     * 查询结果数据
     */
    @Builder.Default
    private List<Map<String, Object>> rows = new ArrayList<>();

    /**
     * 总行数
     */
    private Long totalRows;

    /**
     * 执行时长(毫秒)
     */
    private Long duration;

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 查询元数据
     */
    private QueryMetadata metadata;

    /**
     *
     */
    private int affectedRows;

    /**
     * 是否有更多数据
     */
    private boolean hasMore;

    /**
     * 下一页标记
     */
    private String nextPageToken;

    /**
     * 是否来自缓存
     */
    private boolean fromCache;

    /**
     * 缓存过期时间
     */
    private Long cacheExpireTime;
}