package com.insightdata.domain.query.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 自然语言查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryResult {
    /**
     * 查询ID
     */
    private String queryId;
    
    /**
     * 原始查询
     */
    private String originalQuery;
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 列名列表
     */
    @Builder.Default
    private List<String> columns = new ArrayList<>();
    
    /**
     * 列类型列表
     */
    @Builder.Default
    private List<String> columnTypes = new ArrayList<>();
    
    /**
     * 数据行列表
     */
    @Builder.Default
    private List<Map<String, Object>> data = new ArrayList<>();
    
    /**
     * 总行数
     */
    private long totalRows;
    
    /**
     * 执行时间（毫秒）
     */
    private long executionTime;
    
    /**
     * 查询状态
     */
    private String status;
    
    /**
     * 置信度
     */
    private double confidence;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误消息
     */
    private String errorMessage;
}