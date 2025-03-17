package com.nlquery.converter;

import com.domain.model.metadata.SchemaInfo;
import com.nlquery.QueryContext;
import com.nlquery.entity.EntityExtractionContext;
import com.nlquery.entity.EntityExtractor;
import com.nlquery.entity.EntityTag;
import com.nlquery.intent.IntentRecognitionContext;
import com.nlquery.intent.IntentRecognizer;
import com.nlquery.intent.QueryIntent;
import com.nlquery.preprocess.PreprocessedText;
import com.nlquery.preprocess.TextPreprocessor;
import com.nlquery.sql.SqlGenerator;
import com.nlquery.sql.SqlGenerator.SqlGenerationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认的自然语言到SQL转换器实现
 */
@Slf4j
@Component
public class DefaultNLToSqlConverter implements NLToSqlConverter {

    @Autowired
    private EntityExtractor entityExtractor;
    
    @Autowired
    private IntentRecognizer intentRecognizer;
    
    @Autowired
    private SqlGenerator sqlGenerator;
    
    @Autowired
    private TextPreprocessor textPreprocessor;

    @Override
    public String convert(String naturalLanguageQuery, Long dataSourceId) {
        return convert(naturalLanguageQuery, dataSourceId, null);
    }

    @Override
    public String convert(String naturalLanguageQuery, Long dataSourceId, QueryContext context) {
        try {
            log.info("开始转换自然语言查询: {}", naturalLanguageQuery);
            
            // 1. 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(naturalLanguageQuery);
            
            // 2. 提取实体
            List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText, new EntityExtractionContext());
            
            // 3. 识别意图
            QueryIntent queryIntent = intentRecognizer.recognizeIntent(preprocessedText, new IntentRecognitionContext());
            
            // 4. 生成SQL
            SqlGenerationResult sqlGenerationResult = sqlGenerator.generateSql(entities, queryIntent, null);
            
            log.info("自然语言查询转换完成, 生成的SQL: {}", sqlGenerationResult.getSql());
            return sqlGenerationResult.getSql();
            
        } catch (Exception e) {
            log.error("自然语言查询转换失败", e);
            throw new RuntimeException("自然语言查询转换失败: " + e.getMessage());
        }
    }

    public SqlConversionResult convertToSql(PreprocessedText preprocessedText, SchemaInfo schemaInfo) {
        return convertToSql(preprocessedText, schemaInfo, new EntityExtractionContext(), new IntentRecognitionContext());
    }

    public SqlConversionResult convertToSql(
            PreprocessedText preprocessedText,
            SchemaInfo schemaInfo,
            EntityExtractionContext entityContext,
            IntentRecognitionContext intentContext) {

        SqlConversionResult result = SqlConversionResult.builder().build();

        try {
            // 1. 提取实体
            List<EntityTag> entities = entityExtractor.extractEntities(preprocessedText, entityContext);
            result.setExtractedEntities(entities);

            // 2. 识别意图
            QueryIntent queryIntent = intentRecognizer.recognizeIntent(preprocessedText, intentContext);
            result.setQueryIntent(queryIntent);

            // 3. 生成SQL
            SqlGenerationResult sqlGenerationResult = sqlGenerator.generateSql(entities, queryIntent, schemaInfo);

            // 4. 设置结果
            result.setSql(sqlGenerationResult.getSql());
            result.setParameters(sqlGenerationResult.getParameters());
            result.setConfidence(sqlGenerationResult.getConfidence());
            result.setExplanations(sqlGenerationResult.getExplanations());
            result.setAlternativeSqls(sqlGenerationResult.getAlternativeSqls());
            result.setSuccess(true);

            log.info("SQL转换成功: {}", result.getSql());

        } catch (Exception e) {
            log.error("SQL转换失败", e);
            List<String> explanations = new ArrayList<>();
            explanations.add("转换失败: " + e.getMessage());
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setExplanations(explanations);
            result.setConfidence(0.0);
        }

        return result;
    }
}