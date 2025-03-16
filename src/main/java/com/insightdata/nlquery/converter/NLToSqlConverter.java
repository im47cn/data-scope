package com.insightdata.nlquery.converter;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.QueryContext;
import com.insightdata.nlquery.entity.EntityExtractionContext;
import com.insightdata.nlquery.intent.IntentRecognitionContext;
import com.insightdata.nlquery.preprocess.PreprocessedText;

/**
 * 自然语言转SQL转换器接口
 */
public interface NLToSqlConverter {
    
    /**
     * 将自然语言查询转换为SQL
     *
     * @param naturalLanguageQuery 自然语言查询
     * @param dataSourceId 数据源ID
     * @return SQL语句
     */
    String convert(String naturalLanguageQuery, Long dataSourceId);
    
    /**
     * 将自然语言查询转换为SQL
     *
     * @param naturalLanguageQuery 自然语言查询
     * @param dataSourceId 数据源ID
     * @param context 查询上下文
     * @return SQL语句
     */
    String convert(String naturalLanguageQuery, Long dataSourceId, QueryContext context);
    
    /**
     * 将预处理后的文本转换为SQL
     *
     * @param preprocessedText 预处理后的文本
     * @param schemaInfo 数据库模式信息
     * @return SQL转换结果
     */
    SqlConversionResult convertToSql(PreprocessedText preprocessedText, SchemaInfo schemaInfo);
    
    /**
     * 将预处理后的文本转换为SQL
     *
     * @param preprocessedText 预处理后的文本
     * @param schemaInfo 数据库模式信息
     * @param entityContext 实体提取上下文
     * @param intentContext 意图识别上下文
     * @return SQL转换结果
     */
    SqlConversionResult convertToSql(
            PreprocessedText preprocessedText,
            SchemaInfo schemaInfo,
            EntityExtractionContext entityContext,
            IntentRecognitionContext intentContext);
}