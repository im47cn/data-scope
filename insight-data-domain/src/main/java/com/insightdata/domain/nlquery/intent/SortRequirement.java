package com.insightdata.domain.nlquery.intent;

import lombok.Builder;
import lombok.Data;

/**
 * 排序要求类
 */
@Data
@Builder
public class SortRequirement {
    
    /**
     * 排序方向枚举
     */
    public enum SortDirection {
        ASC,  // 升序
        DESC  // 降序
    }

    /**
     * 排序字段
     */
    private String field;

    /**
     * 排序方向
     */
    private SortDirection direction;

    /**
     * 排序优先级
     */
    private int priority;

    /**
     * 创建一个升序排序要求
     */
    public static SortRequirement asc(String field) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.ASC)
                .priority(0)
                .build();
    }

    /**
     * 创建一个降序排序要求
     */
    public static SortRequirement desc(String field) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.DESC)
                .priority(0)
                .build();
    }

    /**
     * 创建一个带优先级的升序排序要求
     */
    public static SortRequirement ascWithPriority(String field, int priority) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.ASC)
                .priority(priority)
                .build();
    }

    /**
     * 创建一个带优先级的降序排序要求
     */
    public static SortRequirement descWithPriority(String field, int priority) {
        return SortRequirement.builder()
                .field(field)
                .direction(SortDirection.DESC)
                .priority(priority)
                .build();
    }
}
