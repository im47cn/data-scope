package com.insightdata.domain.nlquery.entity;

/**
 * 实体类型
 */
public enum EntityType {
    
    /**
     * 表
     */
    TABLE("表", 1.0),

    /**
     * 列
     */
    COLUMN("列", 1.0),

    /**
     * 值
     */
    VALUE("值", 0.8),

    /**
     * 函数
     */
    FUNCTION("函数", 0.9),

    /**
     * 操作符
     */
    OPERATOR("操作符", 0.9),

    /**
     * 条件
     */
    CONDITION("条件", 0.8),

    /**
     * 排序
     */
    ORDER("排序", 0.8),

    /**
     * 限制
     */
    LIMIT("限制", 0.8),

    /**
     * 分组
     */
    GROUP("分组", 0.9),

    /**
     * 日期时间
     */
    DATETIME("日期时间", 0.9),

    /**
     * 数字
     */
    NUMBER("数字", 0.9),

    /**
     * 字符串
     */
    STRING("字符串", 0.9),

    /**
     * 布尔值
     */
    BOOLEAN("布尔值", 0.9),

    /**
     * 其他
     */
    OTHER("其他", 0.5);

    private final String description;
    private final double defaultConfidence;

    EntityType(String description, double defaultConfidence) {
        this.description = description;
        this.defaultConfidence = defaultConfidence;
    }

    /**
     * 获取实体类型描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 获取默认置信度
     */
    public double getDefaultConfidence() {
        return defaultConfidence;
    }

    /**
     * 检查是否为元数据类型
     */
    public boolean isMetadata() {
        return this == TABLE || this == COLUMN;
    }

    /**
     * 检查是否为数值类型
     */
    public boolean isNumeric() {
        return this == NUMBER || this == DATETIME;
    }

    /**
     * 检查是否为操作类型
     */
    public boolean isOperation() {
        return this == FUNCTION || this == OPERATOR || this == CONDITION;
    }

    /**
     * 检查是否为控制类型
     */
    public boolean isControl() {
        return this == ORDER || this == LIMIT || this == GROUP;
    }

    /**
     * 检查是否为基本值类型
     */
    public boolean isBasicValue() {
        return this == STRING || this == NUMBER || this == BOOLEAN || this == DATETIME;
    }
    
    /**
     * 从字符串获取实体类型
     */
    public static EntityType fromString(String type) {
        try {
            return EntityType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTHER;
        }
    }
}