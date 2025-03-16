package com.insightdata.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.model.query.NLQueryRequest;
import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.model.query.QueryResult;
import com.insightdata.domain.model.query.SavedQuery;
import com.insightdata.domain.repository.QueryHistoryRepository;
import com.insightdata.domain.repository.SavedQueryRepository;
import com.insightdata.domain.service.DataSourceService;
import com.insightdata.domain.service.NLQueryService;
import com.insightdata.nlquery.converter.NLToSqlConverter;
import com.insightdata.nlquery.converter.NLToSqlConverter.SqlConversionResult;
import com.insightdata.nlquery.executor.QueryExecutor;
import com.insightdata.nlquery.preprocess.PreprocessContext;
import com.insightdata.nlquery.preprocess.PreprocessedText;
import com.insightdata.nlquery.preprocess.TextPreprocessor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 自然语言查询服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NLQueryServiceImpl implements NLQueryService {

    private final DataSourceService dataSourceService;
    private final TextPreprocessor textPreprocessor;
    private final NLToSqlConverter nlToSqlConverter;
    private final QueryExecutor queryExecutor;
    private final QueryHistoryRepository queryHistoryRepository;
    private final SavedQueryRepository savedQueryRepository;

    @Override
    public QueryResult executeQuery(NLQueryRequest request) {
        log.info("执行自然语言查询: {}", request.getQuery());
        
        try {
            // 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(request.getQuery(), new PreprocessContext());
            
            // 获取数据源模式信息
            SchemaInfo schemaInfo = dataSourceService.getSchemaInfo(request.getDataSourceId());
            
            // 转换为SQL
            SqlConversionResult conversionResult = nlToSqlConverter.convertToSql(preprocessedText, schemaInfo);
            
            // 执行SQL查询
            QueryResult result = queryExecutor.executeQuery(
                    request.getDataSourceId(),
                    conversionResult.getSql(),
                    conversionResult.getParameters());
            
            // 设置SQL
            result.setSql(conversionResult.getSql());
            
            // 保存查询历史
            saveQueryHistory(request, conversionResult, result);
            
            return result;
            
        } catch (Exception e) {
            log.error("执行自然语言查询时发生错误", e);
            
            QueryResult result = new QueryResult();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            
            return result;
        }
    }

    @Override
    public List<QueryHistory> getQueryHistory(Long dataSourceId) {
        log.info("获取查询历史: {}", dataSourceId);
        return queryHistoryRepository.findByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }

    @Override
    public Long saveQuery(String name, NLQueryRequest request, QueryResult result) {
        log.info("保存查询: {}", name);
        
        try {
            SavedQuery savedQuery = new SavedQuery();
            savedQuery.setName(name);
            savedQuery.setDataSourceId(request.getDataSourceId());
            savedQuery.setNaturalLanguageQuery(request.getQuery());
            savedQuery.setGeneratedSql(result.getSql());
            savedQuery.setParameters(request.getParameters());
            savedQuery.setCreatedAt(LocalDateTime.now());
            savedQuery.setUpdatedAt(LocalDateTime.now());
            savedQuery.setPublic(false);
            
            SavedQuery saved = savedQueryRepository.save(savedQuery);
            return saved.getId();
        } catch (Exception e) {
            log.error("保存查询时发生错误", e);
            return null;
        }
    }

    @Override
    public List<SavedQuery> getSavedQueries(Long dataSourceId) {
        log.info("获取保存的查询: {}", dataSourceId);
        return savedQueryRepository.findByDataSourceIdOrderByUpdatedAtDesc(dataSourceId);
    }

    @Override
    public SavedQuery getSavedQuery(Long id) {
        log.info("获取保存的查询: {}", id);
        return savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("查询不存在: " + id));
    }

    @Override
    public void deleteSavedQuery(Long id) {
        log.info("删除保存的查询: {}", id);
        savedQueryRepository.deleteById(id);
    }

    @Override
    public SavedQuery updateSavedQuery(Long id, String name, String description, boolean isPublic) {
        log.info("更新保存的查询: {}", id);
        
        SavedQuery savedQuery = savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("查询不存在: " + id));
        
        savedQuery.setName(name);
        savedQuery.setDescription(description);
        savedQuery.setPublic(isPublic);
        savedQuery.setUpdatedAt(LocalDateTime.now());
        
        return savedQueryRepository.save(savedQuery);
    }

    /**
     * 执行保存的查询
     */
    public QueryResult executeQueryById(Long queryId, Map<String, Object> parameters) {
        log.info("执行保存的查询: {}", queryId);
        
        try {
            // 获取保存的查询
            SavedQuery savedQuery = savedQueryRepository.findById(queryId)
                    .orElseThrow(() -> new IllegalArgumentException("查询不存在: " + queryId));
            
            // 执行SQL查询
            QueryResult result = queryExecutor.executeQuery(
                    savedQuery.getDataSourceId(),
                    savedQuery.getGeneratedSql(),
                    parameters);
            
            // 设置SQL
            result.setSql(savedQuery.getGeneratedSql());
            
            return result;
            
        } catch (Exception e) {
            log.error("执行保存的查询时发生错误", e);
            
            QueryResult result = new QueryResult();
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            
            return result;
        }
    }

    /**
     * 保存查询历史
     */
    private void saveQueryHistory(NLQueryRequest request, SqlConversionResult conversionResult, QueryResult result) {
        try {
            QueryHistory history = new QueryHistory();
            history.setDataSourceId(request.getDataSourceId());
            history.setNaturalLanguageQuery(request.getQuery());
            history.setExecutedSql(conversionResult.getSql());
            // 设置状态
            history.setStatus(result.isSuccess() ? "SUCCESS" : "FAILED");
            history.setErrorMessage(result.getErrorMessage());
            history.setExecutionTimeMs(result.getExecutionTime());
            history.setCreatedAt(LocalDateTime.now());
            
            queryHistoryRepository.save(history);
        } catch (Exception e) {
            log.error("保存查询历史时发生错误", e);
        }
    }
}
