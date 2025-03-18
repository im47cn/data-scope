package com.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.exception.InsightDataException;
import com.domain.model.query.NLQueryResult;
import com.domain.model.query.QueryHistory;
import com.domain.model.query.SavedQuery;
import com.domain.service.NLQueryService;
import com.nlquery.converter.NLToSqlConverter;
import com.nlquery.executor.QueryExecutor;
import com.nlquery.intent.IntentDetector;
import com.nlquery.preprocess.TextPreprocessor;

/**
 * 自然语言查询服务 - 应用层
 * 协调领域服务完成用例，处理事务边界
 */
@Service
public class NLQueryApplicationService {

    private static final Logger logger = LoggerFactory.getLogger(NLQueryApplicationService.class);

    private final NLQueryService nlQueryService;
    private final TextPreprocessor textPreprocessor;
    private final IntentDetector intentDetector;
    private final NLToSqlConverter nlToSqlConverter;
    private final QueryExecutor queryExecutor;

    public NLQueryApplicationService(NLQueryService nlQueryService, TextPreprocessor textPreprocessor,
                                      IntentDetector intentDetector, NLToSqlConverter nlToSqlConverter,
                                      QueryExecutor queryExecutor) {
        this.nlQueryService = nlQueryService;
        this.textPreprocessor = textPreprocessor;
        this.intentDetector = intentDetector;
        this.nlToSqlConverter = nlToSqlConverter;
        this.queryExecutor = queryExecutor;
    }

    /**
     * 执行自然语言查询
     */
    @Transactional
    public NLQueryResult executeQuery(String dataSourceId, String query, Map<String, Object> parameters) {
        try {
            logger.info("执行自然语言查询, 数据源: {}, 查询: {}", dataSourceId, query);

            if (parameters == null) {
                parameters = new HashMap<>();
            }

            return nlQueryService.executeQuery(dataSourceId, query, parameters);
        } catch (Exception e) {
            logger.error("执行自然语言查询失败", e);
            throw new InsightDataException("执行自然语言查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 分析自然语言查询意图和实体
     */
    @Transactional(readOnly = true)
    public Map<String, Object> analyzeQuery(String dataSourceId, String query) {
        try {
            logger.info("分析自然语言查询, 数据源: {}, 查询: {}", dataSourceId, query);
            return nlQueryService.analyzeQuery(dataSourceId, query);
        } catch (Exception e) {
            logger.error("分析自然语言查询失败", e);
            throw new InsightDataException("分析自然语言查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取查询历史
     */
    @Transactional(readOnly = true)
    public List<QueryHistory> getQueryHistory(String dataSourceId, int page, int size) {
        try {
            logger.info("获取查询历史, 数据源: {}, 页码: {}, 大小: {}", dataSourceId, page, size);
            return nlQueryService.getQueryHistory(dataSourceId, page * size);
        } catch (Exception e) {
            logger.error("获取查询历史失败", e);
            throw new InsightDataException("获取查询历史失败: " + e.getMessage(), e);
        }
    }

    /**
     * 保存查询
     */
    @Transactional
    public SavedQuery saveQuery(String queryId, String name, String description, boolean isShared) {
        try {
            logger.info("保存查询, 查询历史ID: {}, 名称: {}", queryId, name);
            return nlQueryService.saveQuery(queryId, name, description, isShared);
        } catch (Exception e) {
            logger.error("保存查询失败", e);
            throw new InsightDataException("保存查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行已保存的查询
     */
    @Transactional
    public NLQueryResult executeSavedQuery(String queryId, Map<String, Object> parameters) {
        try {
            logger.info("执行已保存的查询, 查询ID: {}", queryId);
            if (parameters == null) {
                parameters = new HashMap<>();
            }
            return nlQueryService.executeSavedQuery(queryId, parameters);
        } catch (Exception e) {
            logger.error("执行已保存的查询失败", e);
            throw new InsightDataException("执行已保存的查询失败: " + e.getMessage(), e);
        }
    }
}