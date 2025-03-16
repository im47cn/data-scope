package com.insightdata.nlquery.intent;

/**
 * 查询类型枚举
 */
public enum QueryType {
    /**
     * 查询
     */
    SELECT("查询"),
    
    /**
     * 统计
     */
    COUNT("统计"),
    
    /**
     * 求和
     */
    SUM("求和"),
    
    /**
     * 平均值
     */
    AVG("平均值"),
    
    /**
     * 最大值
     */
    MAX("最大值"),
    
    /**
     * 最小值
     */
    MIN("最小值"),
    
    /**
     * 分组
     */
    GROUP("分组"),
    
    /**
     * 排序
     */
    ORDER("排序"),
    
    /**
     * 限制
     */
    LIMIT("限制"),
    
    /**
     * 去重
     */
    DISTINCT("去重"),
    
    /**
     * 联合
     */
    UNION("联合"),
    
    /**
     * 交集
     */
    INTERSECT("交集"),
    
    /**
     * 差集
     */
    EXCEPT("差集"),
    
    /**
     * 未知
     */
    UNKNOWN("未知");
    
    private final String displayName;
    
    QueryType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否是聚合类型
     */
    public boolean isAggregate() {
        return this == COUNT || 
               this == SUM || 
               this == AVG || 
               this == MAX || 
               this == MIN;
    }
    
    /**
     * 是否是集合操作类型
     */
    public boolean isSetOperation() {
        return this == UNION || 
               this == INTERSECT || 
               this == EXCEPT;
    }
    
    /**
     * 是否需要分组
     */
    public boolean needsGrouping() {
        return this == GROUP || isAggregate();
    }
    
    /**
     * 是否需要排序
     */
    public boolean needsSorting() {
        return this == ORDER;
    }
    
    /**
     * 是否需要限制条件
     */
    public boolean needsLimit() {
        return this == LIMIT;
    }
    
    /**
     * 是否需要去重
     */
    public boolean needsDistinct() {
        return this == DISTINCT;
    }
}
