package com.insightdata.nlquery.entity;

import java.util.List;

import com.insightdata.nlquery.preprocess.EntityTag;
import com.insightdata.nlquery.preprocess.PreprocessedText;

/**
 * 实体提取器接口
 * 负责从预处理后的文本中提取实体
 */
public interface EntityExtractor {
    
    /**
     * 提取实体
     *
     * @param preprocessedText 预处理后的文本
     * @return 提取的实体列表
     */
    List<EntityTag> extractEntities(PreprocessedText preprocessedText);
    
    /**
     * 提取实体
     *
     * @param preprocessedText 预处理后的文本
     * @param context 实体提取上下文
     * @return 提取的实体列表
     */
    List<EntityTag> extractEntities(PreprocessedText preprocessedText, EntityExtractionContext context);
}