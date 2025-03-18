package com.nlquery.converter;

import java.util.ArrayList;
import java.util.List;

import com.domain.model.metadata.SchemaInfo;
import com.nlquery.entity.EntityExtractionContext;
import com.nlquery.intent.IntentRecognitionContext;
import com.nlquery.preprocess.PreprocessedText;
import com.nlquery.sql.SqlGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.domain.model.metadata.DataSource;
import com.domain.service.DataSourceService;
import com.nlquery.NLQueryRequest;
import com.nlquery.entity.EntityExtractor;
import com.nlquery.entity.EntityTag;
import com.nlquery.intent.IntentRecognizer;
import com.nlquery.intent.QueryIntent;
import com.nlquery.preprocess.TextPreprocessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultNLToSqlConverter implements NLToSqlConverter {

    @Qualifier("metadataBasedEntityExtractor")
    @Autowired
    private EntityExtractor entityExtractor;

    @Autowired
    private IntentRecognizer intentRecognizer;

    @Autowired
    private TextPreprocessor textPreprocessor;

    @Autowired
    private SqlGenerator sqlGenerator;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    public SqlConversionResult convert(NLQueryRequest request) {
        try {
            log.info("开始转换自然语言查询: {}", request.getQuery());

            // 1. 获取数据源
            DataSource dataSource = dataSourceService.getDataSourceById(request.getDataSourceId())
                    .orElseThrow(() -> new IllegalArgumentException("Data source not found: " + request.getDataSourceId()));
            SchemaInfo schemaInfo = dataSourceService.getSchemaInfo(request.getDataSourceId(), dataSource.getName());

            PreprocessedText preprocessedText = textPreprocessor.preprocess(request.getQuery());
            return convert(preprocessedText, schemaInfo);
        } catch (Exception e) {
            log.error("自然语言查询转换失败", e);
            throw new RuntimeException("Failed to convert natural language query", e);
        }
    }

    @Override
    public SqlConversionResult convert(PreprocessedText preprocessedText, SchemaInfo schemaInfo) {
        SqlConversionResult result = SqlConversionResult.builder().build();

        try {
            // 1. 实体提取
            List<EntityTag> entities = entityExtractor.extract(preprocessedText, new EntityExtractionContext());
            result.setExtractedEntities(entities);

            // 2. 意图识别
            QueryIntent queryIntent = intentRecognizer.recognizeIntent(preprocessedText, new IntentRecognitionContext());
            result.setQueryIntent(queryIntent);

            // 3. SQL生成
            SqlGenerator.SqlGenerationResult sqlGenerationResult = sqlGenerator.generate(entities, queryIntent, schemaInfo);

            // 4. 设置结果
            result.setSql(sqlGenerationResult.getSql());
            result.setParameters(sqlGenerationResult.getParameters());
            result.setConfidence(sqlGenerationResult.getConfidence());
            result.setExplanations(sqlGenerationResult.getExplanations());
            result.setAlternativeSqls(sqlGenerationResult.getAlternativeSqls());
            result.setSuccess(true);

            log.info("SQL转换成功: {}", result.getSql());
            return result;

        } catch (Exception e) {
            log.error("SQL转换失败", e);

            List<String> explanations = new ArrayList<>();
            explanations.add("转换失败原因: " + e.getMessage());

            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            result.setExplanations(explanations);
            result.setConfidence(0.0);

            return result;
        }
    }
}