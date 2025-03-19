package com.insightdata.application.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.insightdata.domain.exception.InsightDataException;
import com.insightdata.domain.query.model.NLQueryRequest;
import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.facade.nlquery.NLQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.insightdata.domain.query.model.NLQueryResult;
import com.insightdata.domain.query.model.QueryHistory;
import com.insightdata.domain.nlquery.converter.NLToSqlConverter;
import com.insightdata.domain.nlquery.executor.QueryExecutor;
import com.insightdata.domain.nlquery.intent.IntentDetector;
import com.insightdata.domain.nlquery.preprocess.TextPreprocessor;

/**
 * 自然语言查询服务 - 应用层
 * 协调领域服务完成用例，处理事务边界
 */
@Slf4j
@Service
public class NLQueryApplicationService implements com.insightdata.application.service.NLQueryApplicationService {

    @Autowired
    private com.insightdata.application.service.NLQueryApplicationService nlQueryService;

    @Autowired
    private TextPreprocessor textPreprocessor;

    @Autowired
    private IntentDetector intentDetector;

    @Autowired
    private NLToSqlConverter nlToSqlConverter;

    @Autowired
    private QueryExecutor queryExecutor;

    /**
     * 执行自然语言查询
     */
    @Transactional
    public NLQueryResult executeQuery(String dataSourceId, String query, Map<String, Object> parameters) {
        try {
            log.info("执行自然语言查询, 数据源: {}, 查询: {}", dataSourceId, query);

            if (parameters == null) {
                parameters = new HashMap<>();
            }

            return nlQueryService.executeQuery(dataSourceId, query, parameters);
        } catch (Exception e) {
            log.error("执行自然语言查询失败", e);
            throw new InsightDataException("执行自然语言查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public NLQueryResponse executeQuery(NLQueryRequest request) {
        return null;
    }

    /**
     * 分析自然语言查询意图和实体
     */
    @Transactional(readOnly = true)
    public Map<String, Object> analyzeQuery(String dataSourceId, String query) {
        try {
            log.info("分析自然语言查询, 数据源: {}, 查询: {}", dataSourceId, query);
            return nlQueryService.analyzeQuery(dataSourceId, query);
        } catch (Exception e) {
            log.error("分析自然语言查询失败", e);
            throw new InsightDataException("分析自然语言查询失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<QueryHistory> getQueryHistory(String dataSourceId, int limit) {
        return List.of();
    }

    /**
     * 获取查询历史
     */
    @Transactional(readOnly = true)
    public List<QueryHistory> getQueryHistory(String dataSourceId, int page, int size) {
        try {
            log.info("获取查询历史, 数据源: {}, 页码: {}, 大小: {}", dataSourceId, page, size);
            return nlQueryService.getQueryHistory(dataSourceId, page * size);
        } catch (Exception e) {
            log.error("获取查询历史失败", e);
            throw new InsightDataException("获取查询历史失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存查询
     */
    @Transactional
    public SavedQuery saveQuery(String queryId, String name, String description, boolean isShared) {
        try {
            log.info("保存查询, 查询历史ID: {}, 名称: {}", queryId, name);
            return nlQueryService.saveQuery(queryId, name, description, isShared);
        } catch (Exception e) {
            log.error("保存查询失败", e);
            throw new InsightDataException("保存查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行已保存的查询
     */
    @Transactional
    public NLQueryResult executeSavedQuery(String queryId, Map<String, Object> parameters) {
        try {
            log.info("执行已保存的查询, 查询ID: {}", queryId);
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            return nlQueryService.executeSavedQuery(queryId, parameters);
        } catch (Exception e) {
            log.error("执行已保存的查询失败", e);
            throw new InsightDataException("执行已保存的查询失败: " + e.getMessage(), e);
        }
    }
}