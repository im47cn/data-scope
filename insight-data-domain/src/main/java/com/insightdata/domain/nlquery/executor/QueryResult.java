package com.insightdata.domain.nlquery.executor;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 查询结果
 * 
 * 封装了SQL查询执行结果的数据结构
 */
@Data
@Builder
public class QueryResult {
    /**
     * 查询是否成功执行
     */
    private boolean success;
    
    /**
     * 错误信息（如果查询失败）
     */
    private String errorMessage;
    
    /**
     * 查询执行持续时间（毫秒）
     */
    private long duration;
    
    /**
     * 影响的行数
     */
    private int affectedRows;
    
    /**
     * 总行数（用于分页）
     */
    private int totalRows;
    
    /**
     * 列标签数组
     */
    private List<String> columnLabels;
    
    /**
     * 列数据类型数组
     */
    private List<String> columnTypes;
    
    /**
     * 结果行数据，每行表示为一个Map
     */
    private List<Map<String, Object>> rows;
    
    /**
     * 查询ID，用于跟踪和取消查询
     */
    private String queryId;
}