package com.domain.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.domain.model.DataSource;
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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NLQueryServiceImpl implements NLQueryService {

    private final NLToSqlConverter nlToSqlConverter;
    private final QueryExecutor queryExecutor;
    private final QueryHistoryRepository queryHistoryRepository;
    private final SavedQueryRepository savedQueryRepository;
    private final DataSourceService dataSourceService;

    @Override
    @Transactional
    public QueryResult executeQuery(NLQueryRequest request) {
        // 1. 获取数据源
        DataSource dataSource = dataSourceService.getDataSourceById(request.getDataSourceId())
                .orElseThrow(() -> new IllegalArgumentException("Data source not found: " + request.getDataSourceId()));
        
        // 2. 转换自然语言为SQL
        QueryContext context = QueryContext.builder()
                .dataSource(dataSource)
                .request(request)
                .build();
        
        SqlConversionResult conversionResult = nlToSqlConverter.convert(request);
        
        // 3. 执行SQL查询
        QueryResult result = queryExecutor.execute(conversionResult.getSql(), dataSource);
        
        // 4. 保存查询历史
        QueryHistory history = QueryHistory.builder()
                .id(UUID.randomUUID().toString())
                .dataSourceId(request.getDataSourceId())
                .query(request.getQuery())
                .sql(conversionResult.getSql())
                .executedAt(LocalDateTime.now())
                .duration(result.getDuration())
                .resultCount(result.getTotalRows())
                .success(result.isSuccess())
                .errorMessage(result.getErrorMessage())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        queryHistoryRepository.save(history);
        
        return result;
    }

    @Override
    public List<QueryHistory> getQueryHistory(String dataSourceId) {
        return queryHistoryRepository.findByDataSourceId(dataSourceId);
    }

    @Override
    @Transactional
    public String saveQuery(String name, NLQueryRequest request, SqlConversionResult result) {
        String queryId = UUID.randomUUID().toString();
        SavedQuery savedQuery = SavedQuery.builder()
                .id(queryId)
                .name(name)
                .dataSourceId(request.getDataSourceId())
                .query(request.getQuery())
                .sql(result.getSql())
                .parameters(result.getParameters())
                .tags(request.getTags())
                .isPublic(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        savedQueryRepository.save(savedQuery);
        return queryId;
    }

    @Override
    public List<SavedQuery> getSavedQueries(String dataSourceId) {
        return savedQueryRepository.findByDataSourceId(dataSourceId);
    }

    @Override
    public SavedQuery getSavedQuery(String id) {
        return savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Saved query not found: " + id));
    }

    @Override
    @Transactional
    public void deleteSavedQuery(String id) {
        savedQueryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public SavedQuery updateSavedQuery(String id, String name, String description, boolean isPublic) {
        SavedQuery savedQuery = getSavedQuery(id);
        savedQuery.setName(name);
        savedQuery.setDescription(description);
        savedQuery.setIsPublic(isPublic);
        savedQuery.setUpdatedAt(LocalDateTime.now());
        return savedQueryRepository.save(savedQuery);
    }

    @Override
    public Object executeSavedQuery(String id) {
        SavedQuery savedQuery = getSavedQuery(id);
        DataSource dataSource = dataSourceService.getDataSourceById(savedQuery.getDataSourceId())
                .orElseThrow(() -> new IllegalArgumentException("Data source not found: " + savedQuery.getDataSourceId()));
        return queryExecutor.execute(savedQuery.getSql(), dataSource).getRows();
    }
}