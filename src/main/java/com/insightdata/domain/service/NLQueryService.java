package com.insightdata.domain.service;

import java.util.List;
import java.util.Map;

import com.insightdata.domain.model.query.NLQueryRequest;
import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.model.query.QueryResult;
import com.insightdata.domain.model.query.SavedQuery;

/**
 * 自然语言查询服务接口
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
    List<QueryHistory> getQueryHistory(Long dataSourceId);
    
    /**
     * 保存查询
     *
     * @param name 查询名称
     * @param request 查询请求
     * @param result 查询结果
     * @return 保存的查询ID
     */
    Long saveQuery(String name, NLQueryRequest request, QueryResult result);
    
    /**
     * 获取保存的查询
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    List<SavedQuery> getSavedQueries(Long dataSourceId);
    
    /**
     * 获取保存的查询
     *
     * @param id 查询ID
     * @return 保存的查询
     */
    SavedQuery getSavedQuery(Long id);
    
    /**
     * 删除保存的查询
     *
     * @param id 查询ID
     */
    void deleteSavedQuery(Long id);
    
    /**
     * 更新保存的查询
     *
     * @param id 查询ID
     * @param name 查询名称
     * @param description 描述
     * @param isPublic 是否公开
     * @return 更新后的保存的查询
     */
    SavedQuery updateSavedQuery(Long id, String name, String description, boolean isPublic);
    
    /**
     * 通过ID执行已保存的查询
     *
     * @param id 保存的查询ID
     * @param parameters 查询参数
     * @return 查询结果
     */
    QueryResult executeQueryById(Long id, Map<String, Object> parameters);
}
