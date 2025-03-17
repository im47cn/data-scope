package com.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 列名列表
     */
    private List<String> columns;
    
    /**
     * 数据行列表
     */
    private List<Map<String, Object>> data;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误消息
     */
    private String errorMessage;
}
