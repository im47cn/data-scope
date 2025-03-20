package com.insightdata.domain.nlquery.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 实体标签
 */
@Data
@Builder
public class EntityTag {

    /**
     * 实体类型
     */
    private EntityType type;

    /**
     * 实体值
     */
    private String value;

    /**
     * 置信度
     */
    private double confidence;

    /**
     * 实体属性
     */
    private EntityAttributes attributes;

    /**
     * 创建一个基本的实体标签
     */
    public static EntityTag basic(EntityType type, String value) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(1.0)
                .build();
    }

    /**
     * 创建一个带置信度的实体标签
     */
    public static EntityTag withConfidence(EntityType type, String value, double confidence) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(confidence)
                .build();
    }

    /**
     * 创建一个带属性的实体标签
     */
    public static EntityTag withAttributes(EntityType type, String value, EntityAttributes attributes) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(1.0)
                .attributes(attributes)
                .build();
    }

    /**
     * 创建一个完整的实体标签
     */
    public static EntityTag complete(EntityType type, String value, double confidence, EntityAttributes attributes) {
        return EntityTag.builder()
                .type(type)
                .value(value)
                .confidence(confidence)
                .attributes(attributes)
                .build();
    }

    /**
     * 获取开始位置
     */
    public int getStartOffset() {
        return attributes != null ? attributes.getStartPosition() : -1;
    }

    /**
     * 获取结束位置
     */
    public int getEndOffset() {
        return attributes != null ? attributes.getEndPosition() : -1;
    }

    /**
     * 获取原始文本
     */
    public String getOriginalText() {
        return attributes != null ? attributes.getOriginalText() : value;
    }

    /**
     * 获取标准化文本
     */
    public String getNormalizedText() {
        return attributes != null ? attributes.getNormalizedText() : value;
    }

    /**
     * 获取父实体ID
     */
    public String getParentId() {
        return attributes != null ? attributes.getParentId() : null;
    }
}