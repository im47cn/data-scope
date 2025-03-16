package com.insightdata.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体标注
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityTag {
    
    /**
     * 实体文本
     */
    private String text;
    
    /**
     * 实体类型
     */
    private EntityType type;
    
    /**
     * 开始位置
     */
    private int startOffset;
    
    /**
     * 结束位置
     */
    private int endOffset;
    
    /**
     * 标准化值
     */
    private String normalizedValue;
    
    /**
     * 置信度分数(0-1)
     */
    private double confidence;
    
    /**
     * 是否是模糊匹配
     */
    private boolean fuzzyMatch;
    
    /**
     * 模糊匹配分数(0-1)
     */
    private double fuzzyScore;
    
    /**
     * 实体属性
     */
    @Builder.Default
    private EntityAttributes attributes = new EntityAttributes();
    
    /**
     * 创建一个简单的实体标注
     */
    public static EntityTag simple(String text, EntityType type) {
        return EntityTag.builder()
                .text(text)
                .type(type)
                .confidence(1.0)
                .fuzzyMatch(false)
                .fuzzyScore(1.0)
                .build();
    }
    
    /**
     * 创建一个带位置的实体标注
     */
    public static EntityTag withOffset(String text, EntityType type, int startOffset, int endOffset) {
        return EntityTag.builder()
                .text(text)
                .type(type)
                .startOffset(startOffset)
                .endOffset(endOffset)
                .confidence(1.0)
                .fuzzyMatch(false)
                .fuzzyScore(1.0)
                .build();
    }
    
    /**
     * 创建一个带标准化值的实体标注
     */
    public static EntityTag withNormalization(String text, EntityType type, String normalizedValue) {
        return EntityTag.builder()
                .text(text)
                .type(type)
                .normalizedValue(normalizedValue)
                .confidence(1.0)
                .fuzzyMatch(false)
                .fuzzyScore(1.0)
                .build();
    }
    
    /**
     * 创建一个带属性的实体标注
     */
    public static EntityTag withAttributes(String text, EntityType type, EntityAttributes attributes) {
        return EntityTag.builder()
                .text(text)
                .type(type)
                .attributes(attributes)
                .confidence(1.0)
                .fuzzyMatch(false)
                .fuzzyScore(1.0)
                .build();
    }
    
    /**
     * 获取实体长度
     */
    public int getLength() {
        return endOffset - startOffset;
    }
    
    /**
     * 是否与另一个实体重叠
     */
    public boolean isOverlap(EntityTag other) {
        return !(endOffset <= other.startOffset || startOffset >= other.endOffset);
    }
    
    /**
     * 是否包含另一个实体
     */
    public boolean contains(EntityTag other) {
        return startOffset <= other.startOffset && endOffset >= other.endOffset;
    }
    
    /**
     * 获取与另一个实体的重叠长度
     */
    public int getOverlapLength(EntityTag other) {
        if (!isOverlap(other)) {
            return 0;
        }
        return Math.min(endOffset, other.endOffset) - Math.max(startOffset, other.startOffset);
    }
}