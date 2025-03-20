package com.insightdata.domain.nlquery.entity;

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
     * 运算符
     */
    OPERATOR,

    /**
     * 关键字
     */
    KEYWORD,

    /**
     * 时间
     */
    TIME,

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
     * 未知类型
     */
    UNKNOWN
}