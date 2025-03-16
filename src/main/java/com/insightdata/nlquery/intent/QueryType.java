package com.insightdata.nlquery.intent;

/**
 * 查询类型
 */
public enum QueryType {
    /**
     * 选择查询
     */
    SELECT,
    
    /**
     * 计数查询
     */
    COUNT,
    
    /**
     * 求和查询
     */
    SUM,
    
    /**
     * 平均值查询
     */
    AVG,
    
    /**
     * 最大值查询
     */
    MAX,
    
    /**
     * 最小值查询
     */
    MIN,
    
    /**
     * 分组查询
     */
    GROUP,
    
    /**
     * 未知类型
     */
    UNKNOWN
}
