package com.insightdata.domain.nlquery.entity;

import com.insightdata.domain.nlquery.QueryContext;
import com.insightdata.domain.nlquery.preprocess.PreprocessedText;

import java.util.List;

/**
 * 实体提取器接口
 */
public interface EntityExtractor {

    /**
     * 从查询文本中提取实体
     *
     * @param preprocessedText 查询文本
     * @return 提取的实体列表
     */
    List<EntityTag> extract(PreprocessedText preprocessedText);

    /**
     * 从查询文本中提取实体
     *
     * @param preprocessedText 查询文本
     * @param preprocessedText 实体提取上下文
     * @return 提取的实体列表
     */
    List<EntityTag> extract(PreprocessedText preprocessedText, EntityExtractionContext context);

    /**
     * 验证提取的实体
     *
     * @param entities 提取的实体列表
     * @param context  查询上下文
     * @return 验证后的实体列表
     */
    List<EntityTag> validate(List<EntityTag> entities, QueryContext context);

    /**
     * 合并重复的实体
     *
     * @param entities 实体列表
     * @return 合并后的实体列表
     */
    List<EntityTag> merge(List<EntityTag> entities);
}