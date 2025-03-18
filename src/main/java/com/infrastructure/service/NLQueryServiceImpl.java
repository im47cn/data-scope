package com.infrastructure.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.domain.model.metadata.DataSource;
import com.nlquery.executor.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.domain.model.query.QueryHistory;
import com.domain.model.query.SavedQuery;
import com.domain.repository.QueryHistoryRepository;
import com.domain.repository.SavedQueryRepository;
import com.domain.service.DataSourceService;
import com.domain.service.NLQueryService;
import com.facade.dto.NLQueryRequest;
import com.facade.dto.NLQueryResponse;
import com.facade.dto.QueryHistoryDTO;
import com.facade.dto.SavedQueryDTO;
import com.nlquery.QueryContext;
import com.nlquery.converter.SqlConversionResult;
import com.nlquery.converter.NLToSqlConverter;
import com.nlquery.executor.QueryExecutor;

import lombok.extern.slf4j.Slf4j;

/**
 * 自然语言查询服务实现
 */
@Slf4j
@Service
public class NLQueryServiceImpl implements NLQueryService {

    @Autowired
    private NLToSqlConverter nlToSqlConverter;

    @Autowired
    private QueryExecutor queryExecutor;

    @Autowired
    private QueryHistoryRepository queryHistoryRepository;

    @Autowired
    private SavedQueryRepository savedQueryRepository;

    @Autowired
    private DataSourceService dataSourceService;

    @Override
    @Transactional
    public NLQueryResponse executeQuery(NLQueryRequest request) {
        log.info("执行自然语言查询: {}", request.getQuery());

        // 创建查询上下文
        QueryContext context = new QueryContext();
//        context.setDataSourceId(request.getDataSourceId());
//        context.setOriginalQuery(request.getQuery());
        context.setMaxRows(request.getMaxRows());

        try {
            DataSource dataSource = dataSourceService.getDataSourceById(request.getDataSourceId()).orElse(null);

            // 将自然语言转换为SQL

            SqlConversionResult conversionResult = nlToSqlConverter.convert(com.nlquery.NLQueryRequest.builder()
                    .query(request.getQuery())
                    .dataSourceId(request.getDataSourceId())
                    .parameters(request.getParameters())
                    .tags(request.getTags())
                    .build());

            // 执行SQL查询
            QueryResult result = queryExecutor.execute(
                    conversionResult.getSql(),
                    dataSource);

            // 记录查询历史
            QueryHistory history = saveQueryHistory(request, conversionResult, result.getRows().size(), null);

            // 构建响应
            return buildQueryResponse(history, result.getRows(), conversionResult, request);

        } catch (Exception e) {
            log.error("执行自然语言查询失败", e);

            // 记录失败的查询历史
            QueryHistory history = saveQueryHistory(request, null, 0, e.getMessage());

            // 构建错误响应
            return buildErrorResponse(history, e);
        }
    }

    @Override
    public List<QueryHistoryDTO> getQueryHistory(String dataSourceId, int page, int size) {
        log.info("获取查询历史, 数据源ID: {}, 页码: {}, 大小: {}", dataSourceId, page, size);

        List<QueryHistory> historyList = null;
        if (dataSourceId != null && !dataSourceId.isEmpty()) {
            historyList = queryHistoryRepository.findByDataSourceIdOrderByCreatedAtDesc(
                    dataSourceId);
//            , page * size, size
//        } else {
//            historyList = queryHistoryRepository.findAllOrderByCreatedAtDesc(page * size, size);
        }

        return convertToHistoryDTOList(historyList);
    }

    @Override
    public QueryHistoryDTO getQueryHistoryById(String id) {
        log.info("获取查询历史详情, ID: {}", id);

        return queryHistoryRepository.findById(id)
                .map(this::convertToHistoryDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public String saveQuery(String queryId, SavedQueryDTO savedQueryDTO) {
        log.info("保存查询为常用查询, 查询历史ID: {}", queryId);

        // 获取查询历史
        QueryHistory history = queryHistoryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("查询历史不存在: " + queryId));

        // 创建保存的查询
        SavedQuery savedQuery = new SavedQuery();
        savedQuery.setId(UUID.randomUUID().toString());
        savedQuery.setDataSourceId(history.getDataSourceId());
        savedQuery.setName(savedQueryDTO.getName());
        savedQuery.setDescription(savedQueryDTO.getDescription());
        savedQuery.setSql(history.getExecutedSql());
        savedQuery.setParameterDefinitions(history.getParameters());
        savedQuery.setDefaultParameters(history.getParameters());
        savedQuery.setFolderPath(savedQueryDTO.getFolderPath());
        savedQuery.setIsShared(savedQueryDTO.getIsShared());
        savedQuery.setDisplayOrder(savedQueryDTO.getDisplayOrder());
        savedQuery.setCreatedBy("system"); // 应该从当前用户上下文获取
        savedQuery.setCreatedAt(LocalDateTime.now());
        savedQuery.setUpdatedAt(LocalDateTime.now());

        // 保存标签
        if (savedQueryDTO.getTags() != null) {
            savedQuery.setTags(savedQueryDTO.getTags());
        }

        // 保存到数据库
        SavedQuery saved = savedQueryRepository.save(savedQuery);

        // 更新查询历史，标记为已保存
        history.setIsSaved(true);
        queryHistoryRepository.save(history);

        return saved.getId();
    }

    @Override
    public List<SavedQueryDTO> getSavedQueries(String dataSourceId) {
        return List.of();
    }

    @Override
    public SavedQueryDTO getSavedQuery(String id) {
        return null;
    }

    @Override
    public SavedQueryDTO updateSavedQuery(String id, SavedQueryDTO savedQueryDTO) {
        return null;
    }

    @Override
    public void deleteSavedQuery(String id) {

    }

    @Override
    public NLQueryResponse executeSavedQuery(String queryId, Object parameters) {
        return null;
    }

    @Override
    public NLQueryResponse rerunQuery(String queryId) {
        log.info("重新执行历史查询, ID: {}", queryId);

        // 获取查询历史
        QueryHistory history = queryHistoryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("查询历史不存在: " + queryId));

        // 创建新的请求
        NLQueryRequest request = new NLQueryRequest();
        request.setDataSourceId(history.getDataSourceId());
        request.setQuery(history.getOriginalQuery());
        request.setMaxRows(1000); // 默认值，可以根据之前的配置调整

        // 执行查询
        return executeQuery(request);
    }

    @Override
    public String explainSql(String queryId) {
        log.info("解释SQL, 查询历史ID: {}", queryId);

        // 获取查询历史
        QueryHistory history = queryHistoryRepository.findById(queryId)
                .orElseThrow(() -> new IllegalArgumentException("查询历史不存在: " + queryId));

        // 这里应该有一个更复杂的SQL解释逻辑，简单实现如下
        StringBuilder explanation = new StringBuilder();
        explanation.append("SQL查询解释:\n\n");
        explanation.append(history.getExecutedSql());
        explanation.append("\n\n该SQL查询");

        if (history.getStatus().equals("成功")) {
            explanation.append("成功执行，返回了").append(history.getRowCount()).append("行数据。");
        } else {
            explanation.append("执行失败，错误信息: ").append(history.getErrorMessage());
        }

        explanation.append("\n\n执行时间: ").append(history.getExecutionTime()).append("毫秒。");

        // 在实际实现中，我们应该使用NLP模型来生成更详细的解释

        return explanation.toString();
    }

    /**
     * 保存查询历史
     */
    private QueryHistory saveQueryHistory(NLQueryRequest request, SqlConversionResult conversionResult,
                                          long rowCount, String errorMessage) {

        QueryHistory history = new QueryHistory();
        history.setId(UUID.randomUUID().toString());
        history.setDataSourceId(request.getDataSourceId());
        history.setOriginalQuery(request.getQuery());

        if (conversionResult != null) {
            history.setExecutedSql(conversionResult.getSql());
            history.setParameters(conversionResult.getParameters());
            history.setQueryType("NL");
            history.setStatus("成功");
        } else {
            history.setStatus("失败");
            history.setErrorMessage(errorMessage);
        }

        history.setExecutionTime(System.currentTimeMillis()); // 应该是实际执行时间
        history.setRowCount(rowCount);
        history.setUserId("system"); // 应该从当前用户上下文获取
        history.setUsername("系统用户"); // 应该从当前用户上下文获取
        history.setExecutedAt(LocalDateTime.now());
        history.setIsSaved(false);

        return queryHistoryRepository.save(history);
    }

    /**
     * 构建查询响应
     */
    private NLQueryResponse buildQueryResponse(QueryHistory history, List<Map<String, Object>> results,
                                               SqlConversionResult conversionResult, NLQueryRequest request) {

        NLQueryResponse response = new NLQueryResponse();
        response.setQueryId(history.getId());
        response.setOriginalQuery(history.getOriginalQuery());
        response.setGeneratedSql(history.getExecutedSql());
        response.setData(results);

        // 设置列元数据
        if (!results.isEmpty()) {
            response.setColumns(extractColumnMetadata(results.get(0)));
        } else {
            response.setColumns(Collections.emptyList());
        }

        response.setExecutionTime(history.getExecutionTime());
        response.setTotalRows((long) results.size());
        response.setReturnedRows(results.size());
        response.setConfidenceScore(conversionResult.getConfidence());
        response.setTruncated(results.size() >= request.getMaxRows());
        response.setStatus("成功");
        response.setTimestamp(LocalDateTime.now());
        response.setContextId(request.getContextId());

        // SQL解释（如果请求了）
        if (Boolean.TRUE.equals(request.getIncludeSqlExplanation())) {
            response.setSqlExplanation(explainSql(history.getId()));
        }

        return response;
    }

    /**
     * 构建错误响应
     */
    private NLQueryResponse buildErrorResponse(QueryHistory history, Exception e) {
        NLQueryResponse response = new NLQueryResponse();
        response.setQueryId(history.getId());
        response.setOriginalQuery(history.getOriginalQuery());
        response.setStatus("失败");
        response.setErrorMessage(e.getMessage());
        response.setData(Collections.emptyList());
        response.setColumns(Collections.emptyList());
        response.setTimestamp(LocalDateTime.now());
        return response;
    }

    /**
     * 提取列元数据
     */
    private List<NLQueryResponse.ColumnMetadata> extractColumnMetadata(Map<String, Object> firstRow) {
        List<NLQueryResponse.ColumnMetadata> columns = new ArrayList<>();

        for (String key : firstRow.keySet()) {
            NLQueryResponse.ColumnMetadata column = new NLQueryResponse.ColumnMetadata();
            column.setName(key);
            column.setDisplayName(formatDisplayName(key));

            // 推断数据类型
            Object value = firstRow.get(key);
            column.setDataType(inferDataType(value));

            // 这些信息在实际实现中应该从元数据中获取
            column.setIsPrimaryKey(false);
            column.setTableName("");
            column.setSchemaName("");
            column.setNullable(true);

            columns.add(column);
        }

        return columns;
    }

    /**
     * 格式化显示名
     */
    private String formatDisplayName(String columnName) {
        // 简单的格式化：将下划线替换为空格，每个单词首字母大写
        String[] words = columnName.split("_");
        StringBuilder displayName = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                displayName.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
            }
        }

        return displayName.toString().trim();
    }

    /**
     * 推断数据类型
     */
    private String inferDataType(Object value) {
        if (value == null) {
            return "UNKNOWN";
        } else if (value instanceof Integer) {
            return "INTEGER";
        } else if (value instanceof Long) {
            return "BIGINT";
        } else if (value instanceof Double || value instanceof Float) {
            return "DECIMAL";
        } else if (value instanceof Boolean) {
            return "BOOLEAN";
        } else if (value instanceof LocalDateTime || value.toString().matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.*")) {
            return "DATETIME";
        } else if (value.toString().matches("\\d{4}-\\d{2}-\\d{2}")) {
            return "DATE";
        } else {
            return "VARCHAR";
        }
    }

    /**
     * 将查询历史转换为DTO
     */
    private QueryHistoryDTO convertToHistoryDTO(QueryHistory history) {
        if (history == null) {
            return null;
        }

        QueryHistoryDTO dto = new QueryHistoryDTO();
        dto.setId(history.getId());
        dto.setDataSourceId(history.getDataSourceId());

        // 获取数据源名称
        try {
            dto.setDataSourceName(dataSourceService.getDataSourceById(history.getDataSourceId())
                    .map(ds -> ds.getName())
                    .orElse("未知数据源"));
        } catch (Exception e) {
            dto.setDataSourceName("未知数据源");
        }

        dto.setOriginalQuery(history.getOriginalQuery());
        dto.setExecutedSql(history.getExecutedSql());
        dto.setParameters(history.getParameters());
        dto.setExecutionTime(history.getExecutionTime());
        dto.setRowCount(history.getRowCount());
        dto.setStatus(history.getStatus());
        dto.setErrorMessage(history.getErrorMessage());
        dto.setQueryType(history.getQueryType());
        dto.setUserId(history.getUserId());
        dto.setUsername(history.getUsername());
        dto.setExecutedAt(history.getExecutedAt());
        dto.setIsSaved(history.getIsSaved());

        if (history.getTags() != null) {
            dto.setTags(history.getTags());
        }

        return dto;
    }

    /**
     * 将查询历史列表转换为DTO列表
     */
    private List<QueryHistoryDTO> convertToHistoryDTOList(List<QueryHistory> historyList) {
        if (historyList == null) {
            return Collections.emptyList();
        }

        List<QueryHistoryDTO> dtoList = new ArrayList<>();
        for (QueryHistory history : historyList) {
            dtoList.add(convertToHistoryDTO(history));
        }

        return dtoList;
    }

    /**
     * 将保存的查询转换为DTO
     */
    private SavedQueryDTO convertToSavedQueryDTO(SavedQuery savedQuery) {
        if (savedQuery == null) {
            return null;
        }

        SavedQueryDTO dto = new SavedQueryDTO();
        dto.setId(savedQuery.getId());
        dto.setDataSourceId(savedQuery.getDataSourceId());

        // 获取数据源名称
        try {
            dto.setDataSourceName(dataSourceService.getDataSourceById(savedQuery.getDataSourceId())
                    .map(ds -> ds.getName())
                    .orElse("未知数据源"));
        } catch (Exception e) {
            dto.setDataSourceName("未知数据源");
        }

        dto.setName(savedQuery.getName());
        dto.setDescription(savedQuery.getDescription());
        dto.setSql(savedQuery.getSql());
        dto.setParameterDefinitions(savedQuery.getParameterDefinitions());
        dto.setDefaultParameters(savedQuery.getDefaultParameters());
        dto.setFolderPath(savedQuery.getFolderPath());
        dto.setIsShared(savedQuery.getIsShared());
        dto.setDisplayOrder(savedQuery.getDisplayOrder());
        dto.setCreatedById(savedQuery.getCreatedBy());
        dto.setCreatedAt(savedQuery.getCreatedAt());
        dto.setUpdatedAt(savedQuery.getUpdatedAt());
        dto.setLastExecutedAt(savedQuery.getLastExecutedAt());
        dto.setExecutionCount(savedQuery.getExecutionCount());

        if (savedQuery.getTags() != null) {
            dto.setTags(savedQuery.getTags());
        }

        return dto;
    }
}