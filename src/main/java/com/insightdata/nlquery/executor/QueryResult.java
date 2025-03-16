package com.insightdata.nlquery.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 查询结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryResult {
    
    /**
     * 列名列表
     */
    @Builder.Default
    private List<String> columnLabels = new ArrayList<>();
    
    /**
     * 数据行列表
     */
    @Builder.Default
    private List<Map<String, Object>> rows = new ArrayList<>();
    
    /**
     * 总行数
     */
    private Long totalRows;
    
    /**
     * 执行时长(毫秒)
     */
    private long duration;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 警告信息
     */
    @Builder.Default
    private List<String> warnings = new ArrayList<>();
    
    /**
     * 影响行数
     */
    private int affectedRows;
    
    /**
     * 是否来自缓存
     */
    private boolean fromCache;
    
    /**
     * 缓存过期时间
     */
    private long cacheExpireTime;
    
    /**
     * 元数据
     */
    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();
    
    /**
     * 创建一个成功的查询结果
     */
    public static QueryResult success(List<String> columnLabels, List<Map<String, Object>> rows) {
        return QueryResult.builder()
                .columnLabels(columnLabels)
                .rows(rows)
                .success(true)
                .build();
    }
    
    /**
     * 创建一个失败的查询结果
     */
    public static QueryResult failed(String errorMessage) {
        return QueryResult.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
    
    /**
     * 创建一个带警告的查询结果
     */
    public static QueryResult withWarnings(List<String> columnLabels, List<Map<String, Object>> rows, List<String> warnings) {
        return QueryResult.builder()
                .columnLabels(columnLabels)
                .rows(rows)
                .success(true)
                .warnings(warnings)
                .build();
    }
    
    /**
     * 添加元数据
     */
    public QueryResult addMetadata(String key, Object value) {
        metadata.put(key, value);
        return this;
    }
    
    /**
     * 添加警告
     */
    public QueryResult addWarning(String warning) {
        warnings.add(warning);
        return this;
    }
    
    /**
     * 获取指定列的值列表
     */
    public List<Object> getColumnValues(String columnLabel) {
        List<Object> values = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            values.add(row.get(columnLabel));
        }
        return values;
    }
    
    /**
     * 获取第一行数据
     */
    public Map<String, Object> getFirstRow() {
        return rows.isEmpty() ? null : rows.get(0);
    }
    
    /**
     * 获取单个值
     */
    public Object getSingleValue() {
        Map<String, Object> firstRow = getFirstRow();
        if (firstRow == null || firstRow.isEmpty()) {
            return null;
        }
        return firstRow.values().iterator().next();
    }
}