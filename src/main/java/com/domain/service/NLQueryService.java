package com.domain.service;

import com.domain.model.SavedQuery;
import com.domain.model.query.QueryHistory;
import com.nlquery.NLQueryRequest;
import com.nlquery.converter.SqlConversionResult;

import java.util.List;

/**
 * 自然语言查询服务接口
 */
public interface NLQueryService {
    
    /**
     * 执行查询
     */
    Object executeQuery(NLQueryRequest request);
    
    /**
     * 获取查询历史
     */
    List<QueryHistory> getQueryHistory(Long dataSourceId);
    
    /**
     * 保存查询
     */
    Long saveQuery(String name, NLQueryRequest request, SqlConversionResult result);
    
    /**
     * 获取保存的查询列表
     */
    List<SavedQuery> getSavedQueries(Long dataSourceId);
    
    /**
     * 获取保存的查询详情
     */
    SavedQuery getSavedQuery(Long id);
    
    /**
     * 删除保存的查询
     */
    void deleteSavedQuery(Long id);
    
    /**
     * 更新保存的查询
     */
    SavedQuery updateSavedQuery(Long id, String name, String description, boolean isPublic);
    
    /**
     * 执行保存的查询
     */
    Object executeSavedQuery(Long id);
}
