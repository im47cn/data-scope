package com.insightdata.domain.nlquery.entity;

import lombok.Builder;
import lombok.Data;

/**
 * 实体标记
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
     * 开始位置
     */
    private int startOffset;

    /**
     * 结束位置
     */
    private int endOffset;

    /**
     * 置信度
     */
    private double confidence;

}