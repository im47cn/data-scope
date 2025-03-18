package com.domain.service;

import java.util.List;
import java.util.Map;

import com.domain.model.query.NLQueryRequest;
import com.domain.model.query.NLQueryResult;
import com.domain.model.query.QueryHistory;
import com.domain.model.query.SavedQuery;
import com.facade.dto.NLQueryResponse;

/**
 * 自然语言查询服务接口 - 领域层
 * 定义核心业务逻辑，使用领域模型作为参数和返回值
 */
public interface NLQueryService {
    
    /**
     * 执行自然语言查询
     * 
     * @param request 查询请求
     * @return 查询响应
     */
    NLQueryResponse executeQuery(NLQueryRequest request);
    
    /**
     * 分析自然语言查询
     * 
     * @param dataSourceId 数据源ID
     * @param query 自然语言查询文本
     * @return 分析结果
     */
    Map<String, Object> analyzeQuery(String dataSourceId, String query);
    
    /**
     * 获取查询历史
     * 
     * @param dataSourceId 数据源ID
     * @param limit 返回数量限制
     * @return 查询历史列表
     */
    List<QueryHistory> getQueryHistory(String dataSourceId, int limit);
    
    /**
     * 保存查询
     * 
     * @param queryId 查询历史ID
     * @param name 查询名称
     * @param description 查询描述
     * @param isShared 是否共享
     * @return 保存的查询
     */
    SavedQuery saveQuery(String queryId, String name, String description, boolean isShared);
    
    /**
     * 执行已保存的查询
     * 
     * @param queryId 已保存的查询ID
     * @param parameters 查询参数
     * @return 查询结果
     */
    NLQueryResult executeSavedQuery(String queryId, Map<String, Object> parameters);
}
