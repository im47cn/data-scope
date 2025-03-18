package com.infrastructure.service;

import com.domain.model.metadata.DataSource;
import com.domain.model.query.NLQueryResult;
import com.domain.model.query.QueryHistory;
import com.domain.model.query.SavedQuery;
import com.domain.repository.QueryHistoryRepository;
import com.domain.repository.SavedQueryRepository;
import com.domain.service.DataSourceService;
import com.domain.service.NLQueryService;
import com.nlquery.NLQueryRequest;
import com.nlquery.QueryContext;
import com.nlquery.converter.NLToSqlConverter;
import com.nlquery.converter.SqlConversionResult;
import com.nlquery.executor.QueryExecutor;
import com.nlquery.executor.QueryResult;
import com.nlquery.preprocess.PreprocessedText;
import com.nlquery.preprocess.TextPreprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 自然语言查询服务实现 - 基础设施层
 * 提供技术实现和外部系统集成
 */
@Service
public class NLQueryServiceImpl implements NLQueryService {

    private static final Logger logger = LoggerFactory.getLogger(NLQueryServiceImpl.class);

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    private TextPreprocessor textPreprocessor;

    @Autowired
    private NLToSqlConverter nlToSqlConverter;

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    private SavedQueryRepository savedQueryRepository;

    /**
     * 执行自然语言查询
     */
    @Override
    @Transactional
    public NLQueryResult executeQuery(String dataSourceId, String query, Map<String, Object> parameters) {
        logger.info("执行自然语言查询: {}", query);

        try {
            // 1. 获取数据源
            DataSource dataSource = dataSourceService.getDataSourceById(dataSourceId)
                    .orElseThrow(() -> new RuntimeException("数据源不存在: " + dataSourceId));

            // 2. 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
            String preprocessedQuery = preprocessedText.getText(); // Get the text from PreprocessedText

            // 3. 转换为SQL
            SqlConversionResult conversionResult = nlToSqlConverter.convert(
                    NLQueryRequest.builder()
                            .query(preprocessedQuery)
                            .dataSourceId(dataSourceId)
                            .parameters(parameters)
                            .build());

            // 4. 执行查询
            QueryResult result = queryExecutor.execute(conversionResult.getSql(), dataSource);

            // 5. 保存查询历史
            QueryHistory history = saveQueryHistory(dataSourceId, query, conversionResult, result);

            // 6. 构建返回结果
            return buildNLQueryResult(history, result, conversionResult);

        } catch (Exception e) {
            logger.error("执行自然语言查询时发生错误", e);

            // 记录失败的查询历史
            QueryHistory history = saveFailedQueryHistory(dataSourceId, query, e.getMessage());

            throw new RuntimeException("执行自然语言查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 分析自然语言查询
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> analyzeQuery(String dataSourceId, String query) {
        try {
            logger.info("分析查询: {}", query);

            // 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(query);
            String preprocessedQuery = preprocessedText.getText(); // Get text from PreprocessedText

            // 创建上下文并分析
            QueryContext context = QueryContext.builder()
                    .dataSourceId(dataSourceId)
                    .nlQuery(preprocessedQuery)
                    .build();


            // 分析提取意图和实体
            Map<String, Object> analysis = new HashMap<>();

            // 这里应该调用意图检测器，但为简化实现，直接构建一个基本分析结果
            analysis.put("intent", "查询");
            analysis.put("confidence", 0.8);
            analysis.put("entities", Map.of(
                "tables", List.of("未检测到具体表名"),
                "conditions", List.of("未检测到具体条件")
            ));
            analysis.put("suggestions", List.of(
                "尝试提供更具体的表名",
                "尝试明确查询条件"
            ));

            return analysis;
        } catch (Exception e) {
            logger.error("分析查询失败: {}", e.getMessage(), e);
            throw new RuntimeException("分析查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取查询历史
     */
    @Override
    @Transactional(readOnly = true)
    public List<QueryHistory> getQueryHistory(String dataSourceId, int limit) {
        logger.info("获取查询历史: {}, 限制: {}", dataSourceId, limit);
        return queryHistoryRepository.findByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }

    /**
     * 保存查询
     */
    @Override
    @Transactional
    public SavedQuery saveQuery(String queryId, String name, String description, boolean isShared) {
        logger.info("保存查询: {}, 名称: {}", queryId, name);

        // 1. 获取查询历史
        QueryHistory history = queryHistoryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("查询历史不存在: " + queryId));

        // 2. 创建保存的查询
        SavedQuery savedQuery = new SavedQuery();
        savedQuery.setId(UUID.randomUUID().toString());
        savedQuery.setDataSourceId(history.getDataSourceId());
        savedQuery.setName(name);
        savedQuery.setDescription(description != null ? description : "");
        savedQuery.setSql(history.getExecutedSql());
        savedQuery.setParameterDefinitions(history.getParameters());
        savedQuery.setDefaultParameters(history.getParameters());
        savedQuery.setIsShared(isShared);
        savedQuery.setCreatedAt(LocalDateTime.now());
        savedQuery.setUpdatedAt(LocalDateTime.now());

        // 3. 保存到数据库
        SavedQuery saved = savedQueryRepository.save(savedQuery);

        // 4. 更新查询历史，标记为已保存
        history.setIsSaved(true);
        queryHistoryRepository.save(history);

        return saved;
    }

    /**
     * 执行已保存的查询
     */
    @Override
    @Transactional
    public NLQueryResult executeSavedQuery(String queryId, Map<String, Object> parameters) {
        logger.info("执行已保存的查询: {}", queryId);

        try {
            // 1. 获取保存的查询
            SavedQuery savedQuery = savedQueryRepository.findById(queryId)
                    .orElseThrow(() -> new RuntimeException("查询不存在: " + queryId));

            // 2. 获取数据源
            DataSource dataSource = dataSourceService.getDataSourceById(savedQuery.getDataSourceId())
                    .orElseThrow(() -> new RuntimeException("数据源不存在: " + savedQuery.getDataSourceId()));

            // 3. 执行SQL查询
            QueryResult result = queryExecutor.execute(savedQuery.getSql(), dataSource);

            // 4. 记录查询历史
            QueryHistory history = new QueryHistory();
            history.setId(UUID.randomUUID().toString());
            history.setDataSourceId(savedQuery.getDataSourceId());
            history.setOriginalQuery("已保存的查询: " + savedQuery.getName());
            history.setExecutedSql(savedQuery.getSql());
            history.setParameters(parameters);
            history.setQueryType("SAVED");
            history.setStatus("成功");
            history.setExecutionTime(result.getDuration());
            history.setRowCount(result.getTotalRows());
            history.setExecutedAt(LocalDateTime.now());
            history.setIsSaved(true);
            queryHistoryRepository.save(history);

            // 5. 更新查询使用次数
            savedQuery.setExecutionCount(savedQuery.getExecutionCount() + 1);
            savedQuery.setLastExecutedAt(LocalDateTime.now());
            savedQueryRepository.save(savedQuery);

            // 6. 构建返回结果
            return buildNLQueryResult(history, result);

        } catch (Exception e) {
            logger.error("执行已保存的查询时发生错误", e);
            throw new RuntimeException("执行已保存的查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存查询历史
     */
    private QueryHistory saveQueryHistory(String dataSourceId, String query,
                                         SqlConversionResult conversionResult,
                                         QueryResult result) {
        QueryHistory history = new QueryHistory();
        history.setId(UUID.randomUUID().toString());
        history.setDataSourceId(dataSourceId);
        history.setOriginalQuery(query);
        history.setExecutedSql(conversionResult.getSql());
        history.setParameters(conversionResult.getParameters());
        history.setQueryType("NL");
        history.setStatus("成功");
        history.setExecutionTime(result.getDuration());
        history.setRowCount(result.getTotalRows());
        history.setExecutedAt(LocalDateTime.now());
        history.setIsSaved(false);

        return queryHistoryRepository.save(history);
    }

    /**
     * 保存失败的查询历史
     */
    private QueryHistory saveFailedQueryHistory(String dataSourceId, String query, String errorMessage) {
        QueryHistory history = new QueryHistory();
        history.setId(UUID.randomUUID().toString());
        history.setDataSourceId(dataSourceId);
        history.setOriginalQuery(query);
        history.setQueryType("NL");
        history.setStatus("失败");
        history.setErrorMessage(errorMessage);
        history.setExecutionTime(0L);
        history.setRowCount(0L);
        history.setExecutedAt(LocalDateTime.now());
        history.setIsSaved(false);

        return queryHistoryRepository.save(history);
    }

    /**
     * 构建NL查询结果
     */
    private NLQueryResult buildNLQueryResult(QueryHistory history, QueryResult result, SqlConversionResult conversionResult) {
        NLQueryResult nlResult = new NLQueryResult();
        nlResult.setQueryId(history.getId());
        nlResult.setOriginalQuery(history.getOriginalQuery());
        nlResult.setGeneratedSql(history.getExecutedSql());
        nlResult.setData(result.getRows());
        nlResult.setColumns(result.getColumns());
        nlResult.setColumnTypes(result.getColumnTypes());
        nlResult.setTotalRows(result.getTotalRows());
        nlResult.setExecutionTime(result.getDuration());
        nlResult.setStatus("success");
        nlResult.setConfidence(conversionResult.getConfidence());

        return nlResult;
    }

    /**
     * 构建保存查询结果
     */
    private NLQueryResult buildSavedQueryResult(QueryHistory history, QueryResult result) {
        NLQueryResult nlResult = new NLQueryResult();
        nlResult.setQueryId(history.getId());
        nlResult.setOriginalQuery(history.getOriginalQuery());
        nlResult.setGeneratedSql(history.getExecutedSql());
        nlResult.setData(result.getRows());
        nlResult.setColumns(result.getColumns());
        nlResult.setColumnTypes(result.getColumnTypes());
        nlResult.setTotalRows(result.getTotalRows());
        nlResult.setExecutionTime(result.getDuration());
        nlResult.setStatus("success");
        nlResult.setConfidence(1.0); // 保存的查询置信度为1

        return nlResult;
    }
}