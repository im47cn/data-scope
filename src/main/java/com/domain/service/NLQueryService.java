package com.domain.service;

import java.util.List;

import com.domain.model.query.QueryHistory;
import com.domain.model.query.SavedQuery;
import com.nlquery.NLQueryRequest;
import com.nlquery.converter.SqlConversionResult;
import com.nlquery.executor.QueryResult;

/**
 * 自然语言查询服务接口
 */
public interface NLQueryService {
    
    /**
     * 执行查询
     */
    QueryResult executeQuery(NLQueryRequest request);
    
    /**
     * 获取查询历史
     */
    List<QueryHistory> getQueryHistory(String dataSourceId);
    
    /**
     * 保存查询
     */
    String saveQuery(String name, NLQueryRequest request, SqlConversionResult result);
    
    /**
     * 获取保存的查询列表
     */
    List<SavedQuery> getSavedQueries(String dataSourceId);
    
    /**
     * 获取保存的查询详情
     */
    SavedQuery getSavedQuery(String id);
    
    /**
     * 删除保存的查询
     */
    void deleteSavedQuery(String id);
    
    /**
     * 更新保存的查询
     */
    SavedQuery updateSavedQuery(String id, String name, String description, boolean isPublic);
    
    /**
     * 执行保存的查询
     */
    Object executeSavedQuery(String id);
}
