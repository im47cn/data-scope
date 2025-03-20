package com.insightdata.domain.nlquery.intent;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询意图类，用于表示自然语言查询的意图
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class QueryIntent {

    /**
     * 查询类型
     */
    private QueryType queryType;

    /**
     * 查询目的
     */
    private QueryPurpose queryPurpose;

    /**
     * 时间范围
     */
    private TimeRange timeRange;

    /**
     * 置信度
     */
    private double confidence;

    /**
     * 排序要求
     */
    @Builder.Default
    private List<SortRequirement> sortRequirements = new ArrayList<>();

    /**
     * 限制条件
     */
    private LimitRequirement limitRequirement;

    /**
     * 创建一个简单查询意图
     */
    public static QueryIntent simple(QueryType type) {
        return QueryIntent.builder()
                .queryType(type)
                .queryPurpose(QueryPurpose.DATA_RETRIEVAL)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建一个统计分析查询意图
     */
    public static QueryIntent statistical(QueryType type) {
        return QueryIntent.builder()
                .queryType(type)
                .queryPurpose(QueryPurpose.STATISTICAL_ANALYSIS)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建一个趋势分析查询意图
     */
    public static QueryIntent trend(QueryType type, TimeRange timeRange) {
        return QueryIntent.builder()
                .queryType(type)
                .queryPurpose(QueryPurpose.TREND_ANALYSIS)
                .timeRange(timeRange)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建一个对比分析查询意图
     */
    public static QueryIntent comparison(QueryType type) {
        return QueryIntent.builder()
                .queryType(type)
                .queryPurpose(QueryPurpose.COMPARISON_ANALYSIS)
                .confidence(1.0)
                .build();
    }

    /**
     * 添加排序要求
     */
    public QueryIntent addSortRequirement(SortRequirement requirement) {
        sortRequirements.add(requirement);
        return this;
    }

    /**
     * 判断是否需要时间范围
     */
    public boolean needsTimeRange() {
        return queryPurpose != null && queryPurpose.needsTimeRange();
    }

    /**
     * 判断是否需要分组
     */
    public boolean needsGrouping() {
        return (queryPurpose != null && queryPurpose.needsGrouping()) ||
                (queryType != null && queryType.needsGrouping());
    }

    /**
     * 判断是否需要排序
     */
    public boolean needsSorting() {
        return (queryPurpose != null && queryPurpose.needsSorting()) ||
                (queryType != null && queryType.needsSorting()) ||
                !sortRequirements.isEmpty();
    }

    /**
     * 判断是否需要限制
     */
    public boolean needsLimit() {
        return (queryPurpose != null && queryPurpose.needsLimit()) ||
                (queryType != null && queryType.needsLimit()) ||
                limitRequirement != null;
    }
}
