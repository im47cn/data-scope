package com.insightdata.domain.nlquery.converter;

import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.nlquery.NLQueryRequest;
import com.insightdata.domain.nlquery.entity.EntityExtractionContext;
import com.insightdata.domain.nlquery.entity.EntityExtractor;
import com.insightdata.domain.nlquery.entity.EntityTag;
import com.insightdata.domain.nlquery.intent.IntentRecognitionContext;
import com.insightdata.domain.nlquery.intent.IntentRecognizer;
import com.insightdata.domain.nlquery.intent.QueryIntent;
import com.insightdata.domain.nlquery.preprocess.PreprocessedText;
import com.insightdata.domain.nlquery.preprocess.TextPreprocessor;
import com.insightdata.domain.nlquery.sql.SqlGenerator;
import com.insightdata.domain.metadata.service.DataSourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

            // 1. 实体提取
            List<EntityTag> entities = entityExtractor.extract(preprocessedText, new EntityExtractionContext());
            //result.setExtractedEntities(entities); // Temporarily commented out

            // 2. 意图识别
            QueryIntent queryIntent = intentRecognizer.recognizeIntent(preprocessedText, new IntentRecognitionContext());
            //result.setQueryIntent(queryIntent); // Temporarily commented out

            // 3. SQL生成
            SqlGenerator.SqlGenerationResult sqlGenerationResult = sqlGenerator.generate(entities, queryIntent, schemaInfo);

            // 4. 设置结果
            SqlConversionResult result = SqlConversionResult.builder()
                .sql(sqlGenerationResult.getSql())
                .parameters(sqlGenerationResult.getParameters())
                .confidence(sqlGenerationResult.getConfidence())
                .explanations(sqlGenerationResult.getExplanations())
                .alternativeSqls(sqlGenerationResult.getAlternativeSqls())
                .success(true).build();


            log.info("SQL转换成功: {}", result.getSql());
            return result;

        } catch (Exception e) {
            log.error("SQL转换失败", e);

            List<String> explanations = new ArrayList<>();
            explanations.add("转换失败原因: " + e.getMessage());
            return  SqlConversionResult.builder()
                    .success(false)
                    .errorMessage(e.getMessage())
                    .explanations(explanations)
                    .confidence(0.0)
                    .build();
        }
    }
}