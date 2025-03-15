package com.insightdata.nlquery.entity;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.intent.QueryIntent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 实体提取上下文
 * 包含实体提取过程中需要的上下文信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntityExtractionContext {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 模式名称
     */
    private String schemaName;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 领域
     */
    private String domain;
    
    /**
     * 元数据信息
     */
    private SchemaInfo metadata;
    
    /**
     * 查询意图
     */
    private QueryIntent queryIntent;
    
    /**
     * 是否使用元数据
     */
    @Builder.Default
    private boolean useMetadata = true;
    
    /**
     * 是否使用同义词
     */
    @Builder.Default
    private boolean useSynonyms = true;
    
    /**
     * 是否使用模糊匹配
     */
    @Builder.Default
    private boolean useFuzzyMatching = true;
    
    /**
     * 最小置信度
     */
    @Builder.Default
    private double minConfidence = 0.5;
}