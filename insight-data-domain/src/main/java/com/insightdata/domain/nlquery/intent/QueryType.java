package com.insightdata.domain.nlquery.intent;

/**
 * Query type enum that represents different types of SQL queries
 */
public enum QueryType {
    SELECT,         // 普通查询
    COUNT,          // 计数查询
    SUM,            // 求和查询
    AVG,            // 平均值查询
    MAX,            // 最大值查询
    MIN,            // 最小值查询
    GROUP,          // 分组查询
    DISTINCT,       // 去重查询
    HAVING,         // 分组过滤查询
    UNION,          // 联合查询
    INTERSECT,      // 交集查询
    EXCEPT,         // 差集查询
    UNKNOWN         // 未知类型
}
