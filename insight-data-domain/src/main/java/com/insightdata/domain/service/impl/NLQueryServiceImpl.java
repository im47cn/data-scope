package com.insightdata.domain.service.impl;

import com.insightdata.domain.metadata.model.DataSource;
import com.insightdata.domain.metadata.model.SchemaInfo;
import com.insightdata.domain.query.model.QueryHistory;
import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.domain.repository.QueryHistoryRepository;
import com.insightdata.domain.repository.SavedQueryRepository;
import com.insightdata.domain.service.DataSourceService;
import com.insightdata.domain.service.NLQueryService;
import com.insightdata.domain.nlquery.NLQueryRequest;
import com.insightdata.domain.nlquery.converter.NLToSqlConverter;
import com.insightdata.domain.nlquery.converter.SqlConversionResult;
import com.insightdata.domain.nlquery.executor.QueryExecutor;
import com.insightdata.domain.nlquery.executor.QueryResult;
import com.insightdata.domain.nlquery.preprocess.PreprocessedText;
import com.insightdata.domain.nlquery.preprocess.TextPreprocessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 自然语言查询服务实现
 */
@Service
public class NLQueryServiceImpl implements NLQueryService {

    private static final Logger log = LoggerFactory.getLogger(NLQueryServiceImpl.class);

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
    public QueryResult executeQuery(NLQueryRequest request) {
        try {
            log.info("执行自然语言查询: {}", request.getQuery());

            // 1. 获取数据源信息
            DataSource dataSource = dataSourceService.getDataSourceById(request.getDataSourceId()).get();
            SchemaInfo schemaInfo = dataSourceService.getSchemaInfo(request.getDataSourceId(), dataSource.getName());

            // 2. 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(request.getQuery());

            // 3. 转换为SQL
            SqlConversionResult conversionResult = nlToSqlConverter.convert(preprocessedText, schemaInfo);

            // 4. 执行查询
            QueryResult result = queryExecutor.execute(conversionResult.getSql(), dataSource);

            // 5. 保存查询历史
            saveQueryHistory(request, conversionResult, result);

            return result;

        } catch (Exception e) {
            log.error("执行自然语言查询时发生错误", e);
            throw new RuntimeException("执行自然语言查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取查询历史
     */
    @Override
    public List<QueryHistory> getQueryHistory(String dataSourceId) {
        log.info("获取查询历史: {}", dataSourceId);
        return queryHistoryRepository.findByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }

    /**
     * 保存查询
     */
    @Override
    public String saveQuery(String name, NLQueryRequest request, SqlConversionResult result) {
        log.info("保存查询: {}", name);
        try {
            SavedQuery savedQuery = new SavedQuery();
            savedQuery.setName(name);
            savedQuery.setDataSourceId(request.getDataSourceId());
            savedQuery.setQuery(request.getQuery());
            savedQuery.setDescription("");
            savedQuery.setTags(request.getTags());
            savedQuery.setCreatedAt(LocalDateTime.now());
            savedQuery.setUpdatedAt(LocalDateTime.now());

            savedQueryRepository.save(savedQuery);
            return savedQuery.getId();
        } catch (Exception e) {
            log.error("保存查询时发生错误", e);
            throw new RuntimeException("保存查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取保存的查询列表
     */
    @Override
    public List<SavedQuery> getSavedQueries(String dataSourceId) {
        log.info("获取保存的查询: {}", dataSourceId);
        return savedQueryRepository.findByDataSourceId(dataSourceId);
    }

    /**
     * 获取保存的查询
     */
    @Override
    public SavedQuery getSavedQuery(String id) {
        log.info("获取保存的查询: {}", id);
        return savedQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("查询不存在"));
    }

    /**
     * 删除保存的查询
     */
    @Override
    public void deleteSavedQuery(String id) {
        log.info("删除保存的查询: {}", id);
        savedQueryRepository.deleteById(id);
    }

    /**
     * 更新保存的查询
     */
    @Override
    public SavedQuery updateSavedQuery(String id, String name, String description, boolean isPublic) {
        log.info("更新保存的查询: {}", id);
        SavedQuery savedQuery = savedQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("查询不存在"));

        savedQuery.setName(name);
        savedQuery.setDescription(description);
        savedQuery.setIsPublic(isPublic);
        savedQuery.setUpdatedAt(LocalDateTime.now());

        return savedQueryRepository.save(savedQuery);
    }

    /**
     * 执行保存的查询
     */
    @Override
    public QueryResult executeSavedQuery(String queryId) {
        log.info("执行保存的查询: {}", queryId);
        try {
            // 1. 获取保存的查询
            SavedQuery savedQuery = savedQueryRepository.findById(queryId)
                    .orElseThrow(() -> new RuntimeException("查询不存在"));

            // 2. 构建查询请求
            NLQueryRequest request = new NLQueryRequest();
            request.setDataSourceId(savedQuery.getDataSourceId());
            request.setQuery(savedQuery.getQuery());

            // 3. 执行查询
            return executeQuery(request);

        } catch (Exception e) {
            log.error("执行保存的查询时发生错误", e);
            throw new RuntimeException("执行保存的查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存查询历史
     */
    private void saveQueryHistory(NLQueryRequest request, SqlConversionResult conversionResult, QueryResult result) {
        try {
            QueryHistory history = new QueryHistory();
            history.setDataSourceId(request.getDataSourceId());
            history.setQuery(request.getQuery());
            history.setSql(conversionResult.getSql());
            history.setExecutedAt(LocalDateTime.now());
            history.setDuration(result.getDuration());
            history.setResultCount(result.getTotalRows());
            history.setSuccess(result.isSuccess());
            history.setErrorMessage(result.getErrorMessage());

            queryHistoryRepository.save(history);

        } catch (Exception e) {
            log.error("保存查询历史时发生错误", e);
        }
    }
}
