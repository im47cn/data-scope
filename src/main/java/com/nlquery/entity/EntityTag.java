package com.nlquery.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 在原文中的起始位置
     */
    private int startOffset;
    
    /**
     * 在原文中的结束位置
     */
    private int endOffset;
    
    /**
     * 标准化后的值
     */
    private String normalizedValue;
    
    /**
     * 置信度
     */
    private double confidence;
    
    /**
     * 实体属性
     */
    private EntityAttributes attributes;
    
    /**
     * 是否是主要实体
     */
    private boolean primary;
    
    /**
     * 关联的实体ID
     */
    private String relatedEntityId;
    
    /**
     * 实体来源
     */
    private String source;
}