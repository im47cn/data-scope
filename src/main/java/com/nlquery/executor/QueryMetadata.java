package com.nlquery.executor;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryMetadata {
    
    /**
     * 列名列表
     */
    private List<String> columnLabels;
    
    /**
     * 列类型列表
     */
    private List<String> columnTypes;
    
    /**
     * 表名列表
     */
    private List<String> tableNames;
    
    /**
     * 是否是只读查询
     */
    private boolean readOnly;
    
    /**
     * 是否包含聚合函数
     */
    private boolean hasAggregation;
    
    /**
     * 是否包含分组
     */
    private boolean hasGrouping;
    
    /**
     * 是否包含排序
     */
    private boolean hasOrdering;
    
    /**
     * 是否包含子查询
     */
    private boolean hasSubquery;
    
    /**
     * 是否包含连接
     */
    private boolean hasJoin;
    
    /**
     * 查询类型(SELECT/INSERT/UPDATE/DELETE等)
     */
    private String queryType;
    
    /**
     * 预计影响行数
     */
    private Long estimatedRows;
    
    /**
     * 执行计划
     */
    private String executionPlan;
}