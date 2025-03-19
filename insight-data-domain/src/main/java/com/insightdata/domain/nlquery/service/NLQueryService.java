package com.insightdata.domain.nlquery.service;

import java.util.List;

import com.insightdata.domain.nlquery.NLQueryRequest;
import com.insightdata.domain.nlquery.converter.SqlConversionResult;
import com.insightdata.domain.nlquery.executor.QueryResult;
import com.insightdata.domain.query.model.QueryHistory;
import com.insightdata.domain.query.model.SavedQuery;

/**
 * 自然语言查询服务接口
 * 提供自然语言查询转换、执行、历史管理和查询保存功能
 */
public interface NLQueryService {
    
    /**
     * 执行自然语言查询
     *
     * @param request 查询请求
     * @return 查询结果
     */
    QueryResult executeQuery(NLQueryRequest request);
    
    /**
     * 获取查询历史
     *
     * @param dataSourceId 数据源ID
     * @return 查询历史列表
     */
    List<QueryHistory> getQueryHistory(String dataSourceId);
    
    /**
     * 保存查询
     *
     * @param name 查询名称
     * @param request 查询请求
     * @param result SQL转换结果
     * @return 保存后的查询ID
     */
    String saveQuery(String name, NLQueryRequest request, SqlConversionResult result);
    
    /**
     * 获取数据源的所有保存的查询
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    List<SavedQuery> getSavedQueries(String dataSourceId);
    
    /**
     * 获取特定保存的查询
     *
     * @param id 查询ID
     * @return 保存的查询
     */
    SavedQuery getSavedQuery(String id);
    
    /**
     * 删除保存的查询
     *
     * @param id 查询ID
     */
    void deleteSavedQuery(String id);
    
    /**
     * 更新保存的查询
     *
     * @param id 查询ID
     * @param name 查询名称
     * @param description 查询描述
     * @param isPublic 是否公开
     * @return 更新后的查询
     */
    SavedQuery updateSavedQuery(String id, String name, String description, boolean isPublic);
    
    /**
     * 执行保存的查询
     *
     * @param queryId 查询ID
     * @return 查询结果
     */
    QueryResult executeSavedQuery(String queryId);
}
