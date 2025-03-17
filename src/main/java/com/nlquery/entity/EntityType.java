package com.nlquery.entity;

/**
 * 实体类型枚举
 */
public enum EntityType {
    /**
     * 表名
     */
    TABLE("表名"),
    
    /**
     * 列名
     */
    COLUMN("列名"),
    
    /**
     * 数据库名
     */
    DATABASE("数据库名"),
    
    /**
     * 模式名
     */
    SCHEMA("模式名"),
    
    /**
     * 函数名
     */
    FUNCTION("函数名"),
    
    /**
     * 聚合函数
     */
    AGGREGATE("聚合函数"),
    
    /**
     * 条件操作符
     */
    OPERATOR("条件操作符"),
    
    /**
     * 逻辑操作符
     */
    LOGICAL("逻辑操作符"),
    
    /**
     * 排序关键字
     */
    ORDER("排序关键字"),
    
    /**
     * 分组关键字
     */
    GROUP("分组关键字"),

    /**
     * 条件
     */
    CONDITION("条件"),

    /**
     * 值
     */
    VALUE("值"),

    /**
     * 数值
     */
    NUMBER("数值"),
    
    /**
     * 字符串
     */
    STRING("字符串"),
    
    /**
     * 日期时间
     */
    DATETIME("日期时间"),
    
    /**
     * 布尔值
     */
    BOOLEAN("布尔值"),
    
    /**
     * 时间单位
     */
    TIME_UNIT("时间单位"),
    
    /**
     * 时间范围
     */
    TIME_RANGE("时间范围"),
    
    /**
     * 限制条件
     */
    LIMIT("限制条件"),
    
    /**
     * 未知类型
     */
    UNKNOWN("未知类型");
    
    private final String displayName;
    
    EntityType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * 是否是元数据类型
     */
    public boolean isMetadata() {
        return this == TABLE || this == COLUMN || this == DATABASE || this == SCHEMA;
    }
    
    /**
     * 是否是函数类型
     */
    public boolean isFunction() {
        return this == FUNCTION || this == AGGREGATE;
    }
    
    /**
     * 是否是操作符类型
     */
    public boolean isOperator() {
        return this == OPERATOR || this == LOGICAL;
    }
    
    /**
     * 是否是关键字类型
     */
    public boolean isKeyword() {
        return this == ORDER || this == GROUP || this == LIMIT;
    }
    
    /**
     * 是否是值类型
     */
    public boolean isValue() {
        return this == NUMBER || this == STRING || this == DATETIME || this == BOOLEAN;
    }
    
    /**
     * 是否是时间类型
     */
    public boolean isTime() {
        return this == TIME_UNIT || this == TIME_RANGE || this == DATETIME;
    }
}