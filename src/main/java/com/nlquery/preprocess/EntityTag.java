package com.nlquery.preprocess;

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
    private int startPosition;
    
    /**
     * 结束位置
     */
    private int endPosition;
    
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
     * 词性标注
     */
    private String posTag;
    
    /**
     * 依存关系
     */
    private String dependency;
    
    /**
     * 语义角色
     */
    private String semanticRole;
    
    /**
     * 实体属性
     */
    private EntityAttributes attributes;
    
    /**
     * 创建一个简单的实体标注
     */
    public EntityTag(String text, EntityType type) {
        this.text = text;
        this.type = type;
        this.confidence = 1.0;
        this.fuzzyMatch = false;
        this.fuzzyScore = 1.0;
    }
}
