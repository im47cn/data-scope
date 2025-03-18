package com.insightdata.domain.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体标记类
 * 用于标识和存储从文本中识别出的实体信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityTag {

    /**
     * 实体值
     */
    private String value;

    /**
     * 实体类型
     */
    private EntityType type;

    /**
     * 实体属性
     */
    private EntityAttributes attributes;

    /**
     * 在原文中的开始位置
     */
    private int startOffset;

    /**
     * 在原文中的结束位置
     */
    private int endOffset;

    /**
     * 识别置信度(0-1)
     */
    private double confidence;

    /**
     * 规范化后的值
     */
    private String normalizedValue;

    /**
     * 创建表实体标记
     *
     * @param value 表名
     * @return 表实体标记
     */
    public static EntityTag table(String value) {
        return EntityTag.builder()
                .value(value)
                .type(EntityType.TABLE)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建列实体标记
     *
     * @param value 列名
     * @return 列实体标记
     */
    public static EntityTag column(String value) {
        return EntityTag.builder()
                .value(value)
                .type(EntityType.COLUMN)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建值实体标记
     *
     * @param value 值
     * @return 值实体标记
     */
    public static EntityTag value(String value) {
        return EntityTag.builder()
                .value(value)
                .type(EntityType.VALUE)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建函数实体标记
     *
     * @param value 函数名
     * @return 函数实体标记
     */
    public static EntityTag function(String value) {
        return EntityTag.builder()
                .value(value)
                .type(EntityType.FUNCTION)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建操作符实体标记
     *
     * @param value 操作符
     * @return 操作符实体标记
     */
    public static EntityTag operator(String value) {
        return EntityTag.builder()
                .value(value)
                .type(EntityType.OPERATOR)
                .confidence(1.0)
                .build();
    }

    /**
     * 判断是否为表实体
     *
     * @return 如果是表实体返回true
     */
    public boolean isTable() {
        return type == EntityType.TABLE;
    }

    /**
     * 判断是否为列实体
     *
     * @return 如果是列实体返回true
     */
    public boolean isColumn() {
        return type == EntityType.COLUMN;
    }

    /**
     * 判断是否为值实体
     *
     * @return 如果是值实体返回true
     */
    public boolean isValue() {
        return type == EntityType.VALUE;
    }

    /**
     * 判断是否为函数实体
     *
     * @return 如果是函数实体返回true
     */
    public boolean isFunction() {
        return type == EntityType.FUNCTION;
    }

    /**
     * 判断是否为操作符实体
     *
     * @return 如果是操作符实体返回true
     */
    public boolean isOperator() {
        return type == EntityType.OPERATOR;
    }

    /**
     * 创建带有位置信息的实体标记
     *
     * @param value 实体值
     * @param type 实体类型
     * @param start 开始位置
     * @param end 结束位置
     * @return 实体标记
     */
    public static EntityTag of(String value, EntityType type, int start, int end) {
        return EntityTag.builder()
                .value(value)
                .type(type)
                .startOffset(start)
                .endOffset(end)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建带有位置信息和置信度的实体标记
     *
     * @param value 实体值
     * @param type 实体类型
     * @param start 开始位置
     * @param end 结束位置
     * @param confidence 置信度
     * @return 实体标记
     */
    public static EntityTag of(String value, EntityType type, int start, int end, double confidence) {
        return EntityTag.builder()
                .value(value)
                .type(type)
                .startOffset(start)
                .endOffset(end)
                .confidence(confidence)
                .build();
    }
}