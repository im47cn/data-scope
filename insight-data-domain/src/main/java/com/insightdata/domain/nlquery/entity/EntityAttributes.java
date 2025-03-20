package com.insightdata.domain.nlquery.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 实体属性
 */
@Data
@Builder
public class EntityAttributes {

    /**
     * 原始文本
     */
    private String originalText;

    /**
     * 标准化文本
     */
    private String normalizedText;

    /**
     * 开始位置
     */
    private int startPosition;

    /**
     * 结束位置
     */
    private int endPosition;

    /**
     * 父实体ID
     */
    private String parentId;

    /**
     * 从文本中提取实体属性
     */
    public static EntityAttributes fromText(String text, int startPos, int endPos) {
        return EntityAttributes.builder()
                .originalText(text)
                .normalizedText(text.toLowerCase())
                .startPosition(startPos)
                .endPosition(endPos)
                .build();
    }

    /**
     * 从文本中提取实体属性,并设置父实体ID
     */
    public static EntityAttributes fromText(String text, int startPos, int endPos, String parentId) {
        return EntityAttributes.builder()
                .originalText(text)
                .normalizedText(text.toLowerCase())
                .startPosition(startPos)
                .endPosition(endPos)
                .parentId(parentId)
                .build();
    }

    /**
     * 创建一个新的实体属性,继承父实体的属性
     */
    public static EntityAttributes fromParent(EntityAttributes parent, String text) {
        return EntityAttributes.builder()
                .originalText(text)
                .normalizedText(text.toLowerCase())
                .startPosition(parent.getStartPosition())
                .endPosition(parent.getEndPosition())
                .parentId(parent.getParentId())
                .build();
    }
}