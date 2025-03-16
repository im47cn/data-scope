package com.insightdata.application.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insightdata.application.service.DataSourceService;
import com.insightdata.domain.model.DataSource;
import com.insightdata.domain.model.QueryHistory;
import com.insightdata.domain.model.SavedQuery;
import com.insightdata.domain.model.metadata.SchemaInfo;
import com.insightdata.domain.repository.QueryHistoryRepository;
import com.insightdata.domain.repository.SavedQueryRepository;
import com.insightdata.nlquery.NLQueryRequest;
import com.insightdata.nlquery.converter.NLToSqlConverter;
import com.insightdata.nlquery.converter.SqlConversionResult;
import com.insightdata.nlquery.executor.QueryExecutor;
import com.insightdata.nlquery.executor.QueryResult;
import com.insightdata.nlquery.preprocess.PreprocessedText;
import com.insightdata.nlquery.preprocess.TextPreprocessor;

/**
 * 自然语言查询服务实现
 */
@Service
public class NLQueryServiceImpl {

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
    public QueryResult executeQuery(NLQueryRequest request) {
        try {
            log.info("执行自然语言查询: {}", request.getQuery());
            
            // 1. 获取数据源信息
            DataSource dataSource = dataSourceService.getDataSource(request.getDataSourceId());
            SchemaInfo schemaInfo = dataSourceService.getSchemaInfo(request.getDataSourceId(), dataSource.getName());
            
            // 2. 预处理文本
            PreprocessedText preprocessedText = textPreprocessor.preprocess(request.getQuery());
            
            // 3. 转换为SQL
            SqlConversionResult conversionResult = nlToSqlConverter.convertToSql(preprocessedText, schemaInfo);
            
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
    public List<QueryHistory> getQueryHistory(Long dataSourceId) {
        log.info("获取查询历史: {}", dataSourceId);
        return queryHistoryRepository.findByDataSourceId(dataSourceId);
    }

    /**
     * 保存查询
     */
    public SavedQuery saveQuery(String name, NLQueryRequest request) {
        log.info("保存查询: {}", name);
        try {
            SavedQuery savedQuery = new SavedQuery();
            savedQuery.setName(name);
            savedQuery.setDataSourceId(request.getDataSourceId());
            savedQuery.setQuery(request.getQuery());
            savedQuery.setDescription(request.getDescription());
            savedQuery.setTags(request.getTags());
            savedQuery.setCreateTime(LocalDateTime.now());
            savedQuery.setUpdateTime(LocalDateTime.now());
            
            return savedQueryRepository.save(savedQuery);
            
        } catch (Exception e) {
            log.error("保存查询时发生错误", e);
            throw new RuntimeException("保存查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取保存的查询列表
     */
    public List<SavedQuery> getSavedQueries(Long dataSourceId) {
        log.info("获取保存的查询: {}", dataSourceId);
        return savedQueryRepository.findByDataSourceId(dataSourceId);
    }

    /**
     * 获取保存的查询
     */
    public SavedQuery getSavedQuery(Long id) {
        log.info("获取保存的查询: {}", id);
        return savedQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("查询不存在"));
    }

    /**
     * 删除保存的查询
     */
    public void deleteSavedQuery(Long id) {
        log.info("删除保存的查询: {}", id);
        savedQueryRepository.deleteById(id);
    }

    /**
     * 更新保存的查询
     */
    public SavedQuery updateSavedQuery(Long id, String name, String description, List<String> tags) {
        log.info("更新保存的查询: {}", id);
        SavedQuery savedQuery = savedQueryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("查询不存在"));
        
        savedQuery.setName(name);
        savedQuery.setDescription(description);
        savedQuery.setTags(tags);
        savedQuery.setUpdateTime(LocalDateTime.now());
        
        return savedQueryRepository.save(savedQuery);
    }

    /**
     * 执行保存的查询
     */
    public QueryResult executeSavedQuery(Long queryId) {
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
            history.setExecuteTime(LocalDateTime.now());
            history.setDuration(result.getDuration());
            history.setRowCount(result.getRowCount());
            history.setSuccess(result.isSuccess());
            history.setErrorMessage(result.getErrorMessage());
            
            queryHistoryRepository.save(history);
            
        } catch (Exception e) {
            log.error("保存查询历史时发生错误", e);
        }
    }
}
