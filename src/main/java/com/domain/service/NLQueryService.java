package com.domain.service;

import java.util.List;

import com.facade.dto.NLQueryRequest;
import com.facade.dto.NLQueryResponse;
import com.facade.dto.QueryHistoryDTO;
import com.facade.dto.SavedQueryDTO;

/**
 * 自然语言查询服务接口
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
     * 获取查询历史列表
     * 
     * @param dataSourceId 数据源ID
     * @param page 页码
     * @param size 每页大小
     * @return 查询历史列表
     */
    List<QueryHistoryDTO> getQueryHistory(String dataSourceId, int page, int size);
    
    /**
     * 获取查询历史详情
     * 
     * @param id 查询历史ID
     * @return 查询历史详情
     */
    QueryHistoryDTO getQueryHistoryById(String id);
    
    /**
     * 重新执行历史查询
     * 
     * @param queryId 查询历史ID
     * @return 查询响应
     */
    NLQueryResponse rerunQuery(String queryId);
    
    /**
     * 保存查询
     * 
     * @param queryId 查询历史ID
     * @param savedQueryDTO 保存查询DTO
     * @return 保存的查询ID
     */
    String saveQuery(String queryId, SavedQueryDTO savedQueryDTO);
    
    /**
     * 获取已保存的查询列表
     * 
     * @param dataSourceId 数据源ID
     * @return 已保存的查询列表
     */
    List<SavedQueryDTO> getSavedQueries(String dataSourceId);
    
    /**
     * 获取已保存的查询详情
     * 
     * @param id 已保存的查询ID
     * @return 已保存的查询详情
     */
    SavedQueryDTO getSavedQuery(String id);
    
    /**
     * 更新已保存的查询
     * 
     * @param id 已保存的查询ID
     * @param savedQueryDTO 更新信息
     * @return 更新后的查询信息
     */
    SavedQueryDTO updateSavedQuery(String id, SavedQueryDTO savedQueryDTO);
    
    /**
     * 删除已保存的查询
     * 
     * @param id 已保存的查询ID
     */
    void deleteSavedQuery(String id);
    
    /**
     * 执行已保存的查询
     * 
     * @param queryId 已保存的查询ID
     * @param parameters 查询参数
     * @return 查询响应
     */
    NLQueryResponse executeSavedQuery(String queryId, Object parameters);
    
    /**
     * 解释SQL语句
     * 
     * @param sql SQL语句
     * @return SQL解释
     */
    String explainSql(String sql);
}
