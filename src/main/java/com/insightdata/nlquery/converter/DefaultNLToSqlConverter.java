package com.insightdata.nlquery.converter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.nlquery.entity.EntityExtractionContext;
import com.insightdata.nlquery.entity.EntityExtractor;
import com.insightdata.nlquery.entity.HybridEntityExtractor;
import com.insightdata.nlquery.intent.IntentRecognitionContext;
import com.insightdata.nlquery.intent.IntentRecognizer;
import com.insightdata.nlquery.intent.QueryIntent;
import com.insightdata.nlquery.intent.RuleBasedIntentRecognizer;
import com.insightdata.nlquery.preprocess.EntityTag;
import com.insightdata.nlquery.preprocess.PreprocessedText;
import com.insightdata.nlquery.sql.SqlGenerator;

import lombok.RequiredArgsConstructor;

/**
 * 自然语言到SQL转换器的默认实现
 */
@Component
@RequiredArgsConstructor
public class DefaultNLToSqlConverter implements NLToSqlConverter {

    private final EntityExtractor entityExtractor;
    private final IntentRecognizer intentRecognizer;
    private final SqlGenerator sqlGenerator;

    @Override
    public SqlConversionResult convertToSql(PreprocessedText preprocessedText, SchemaInfo schemaInfo) {
        return convertToSql(preprocessedText, schemaInfo, new EntityExtractionContext(), new IntentRecognitionContext());
    }

    @Override
    public SqlConversionResult convertToSql(
            PreprocessedText preprocessedText,
            SchemaInfo schemaInfo,
            EntityExtractionContext entityExtractionContext,
            IntentRecognitionContext intentRecognitionContext) {
        
        SqlConversionResult result = new SqlConversionResult();
        
        try {
            // 提取实体
            List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText, entityExtractionContext);
            result.setExtractedEntities(entities);
            
            // 识别意图
            QueryIntent queryIntent = intentRecognizer.recognizeIntent(preprocessedText, intentRecognitionContext);
            result.setQueryIntent(queryIntent);
            
            // 生成SQL
            SqlGenerator.SqlGenerationResult sqlGenerationResult = sqlGenerator.generateSql(entities, queryIntent, schemaInfo);
            
            // 设置结果
            result.setSql(sqlGenerationResult.getSql());
            result.setParameters(sqlGenerationResult.getParameters());
            result.setConfidence(sqlGenerationResult.getConfidence());
            result.setExplanations(sqlGenerationResult.getExplanations());
            result.setAlternativeSqls(sqlGenerationResult.getAlternativeSqls());
            
        } catch (Exception e) {
            // 处理异常
            List<String> explanations = new ArrayList<>();
            explanations.add("转换SQL时发生错误: " + e.getMessage());
            result.setExplanations(explanations);
            result.setConfidence(0.0);
        }
        
        return result;
    }
}