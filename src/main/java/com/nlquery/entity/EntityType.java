package com.nlquery.entity;

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
     * 日期时间
     */
    DATETIME,
    
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
     * 未知类型
     */
    UNKNOWN
}