package com.insightdata.nlquery.preprocess;

/**
 * 实体类型
 * 表示识别出的实体的类型
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
     * 值
     */
    VALUE,
    
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
     * 排序
     */
    ORDER,
    
    /**
     * 限制
     */
    LIMIT,
    
    /**
     * 分组
     */
    GROUP,
    
    /**
     * 日期
     */
    DATE,
    
    /**
     * 数字
     */
    NUMBER,
    
    /**
     * 字符串
     */
    STRING,
    
    /**
     * 布尔值
     */
    BOOLEAN,
    
    /**
     * 未知
     */
    UNKNOWN
}
