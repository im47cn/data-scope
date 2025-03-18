package com.application.service.impl;

import com.common.exception.InsightDataException;
import com.domain.model.query.QueryResult;
import com.domain.service.NLQueryService;
import com.nlquery.NLQueryRequest;
import com.nlquery.QueryContext;
import com.nlquery.converter.NLToSqlConverter;
import com.nlquery.executor.QueryExecutor;
import com.nlquery.intent.IntentDetector;
import com.nlquery.preprocess.TextPreprocessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class NLQueryServiceImpl implements NLQueryService {
    
    private final NLToSqlConverter nlToSqlConverter;
    private final IntentDetector intentDetector;
    private final TextPreprocessor textPreprocessor;
    private final QueryExecutor queryExecutor;

    @Override
    public QueryResult executeNLQuery(String dataSourceId, NLQueryRequest request) {
        try {
            log.info("Executing NL query for dataSource: {}, query: {}", dataSourceId, request.getQuery());
            
            // 预处理文本
            String preprocessedQuery = textPreprocessor.preprocess(request.getQuery());
            
            // 创建查询上下文
            QueryContext context = QueryContext.builder()
                .dataSourceId(dataSourceId)
                .nlQuery(preprocessedQuery)
                .parameters(request.getParameters() != null ? request.getParameters() : new HashMap<>())
                .options(request.getOptions() != null ? request.getOptions() : new HashMap<>())
                .build();

            // 检测意图
            context = intentDetector.detectIntent(context);
            log.debug("Detected intent: {}", context.getIntent());

            // 转换为SQL
            context = nlToSqlConverter.convert(context);
            log.debug("Generated SQL: {}", context.getSql());

            // 执行查询
            context = queryExecutor.execute(context);

            // 构建结果
            return QueryResult.builder()
                .sql(context.getSql())
                .parameters(context.getParameters())
                .data(context.getData())
                .metadata(context.getMetadata())
                .build();

        } catch (Exception e) {
            log.error("Failed to execute NL query: {}", e.getMessage(), e);
            throw new InsightDataException("Failed to execute natural language query", e);
        }
    }

    @Override
    public Map<String, Object> analyzeQuery(String dataSourceId, String query) {
        try {
            log.info("Analyzing query for dataSource: {}", dataSourceId);
            
            String preprocessedQuery = textPreprocessor.preprocess(query);
            
            QueryContext context = QueryContext.builder()
                .dataSourceId(dataSourceId)
                .nlQuery(preprocessedQuery)
                .build();

            context = intentDetector.detectIntent(context);
            
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("intent", context.getIntent());
            analysis.put("confidence", context.getConfidence());
            analysis.put("entities", context.getEntities());
            analysis.put("suggestions", context.getSuggestions());
            
            return analysis;

        } catch (Exception e) {
            log.error("Failed to analyze query: {}", e.getMessage(), e);
            throw new InsightDataException("Failed to analyze query", e);
        }
    }
}