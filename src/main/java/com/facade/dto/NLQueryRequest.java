package com.facade.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自然语言查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 自然语言查询
     */
    private String query;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 上下文ID（用于会话连续性）
     */
    private String contextId;
    
    /**
     * 是否包含SQL解释
     */
    private Boolean includeSqlExplanation;
    
    /**
     * 是否包含查询计划
     */
    private Boolean includeQueryPlan;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 标签
     */
    private List<String> tags;
    
    /**
     * 是否保存为常用查询
     */
    private Boolean saveAsQuery;
    
    /**
     * 保存的查询名称（如果saveAsQuery为true）
     */
    private String savedQueryName;
    
    /**
     * 保存的查询描述（如果saveAsQuery为true）
     */
    private String savedQueryDescription;
    
    /**
     * 是否执行历史查询
     */
    private Boolean isHistoryExecution;
}