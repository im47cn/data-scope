package com.insightdata.nlquery.preprocess;

/**
 * 实体类型枚举
 */
public enum EntityType {
    /**
     * 表名
     */
    TABLE,
    
    /**
     * 列名
     */
    COLUMN,
    
    /**
     * 函数
     */
    FUNCTION,
    
    /**
     * 操作符
     */
    OPERATOR,
    
    /**
     * 条件
     */
    CONDITION,
    
    /**
     * 值
     */
    VALUE,
    
    /**
     * 数字
     */
    NUMBER,
    
    /**
     * 字符串
     */
    STRING,
    
    /**
     * 日期
     */
    DATE,
    
    /**
     * 布尔值
     */
    BOOLEAN,
    
    /**
     * 排序
     */
    ORDER,
    
    /**
     * 分组
     */
    GROUP,
    
    /**
     * 限制
     */
    LIMIT,
    
    /**
     * 聚合
     */
    AGGREGATE,
    
    /**
     * 连接
     */
    JOIN,
    
    /**
     * 别名
     */
    ALIAS,
    
    /**
     * 子查询
     */
    SUBQUERY,
    
    /**
     * 其他
     */
    OTHER
}
