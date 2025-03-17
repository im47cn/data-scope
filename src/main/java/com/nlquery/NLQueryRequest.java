package com.nlquery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自然语言查询请求
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NLQueryRequest {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * 查询参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 查询标签
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 是否使用缓存
     */
    @Builder.Default
    private boolean useCache = true;
    
    /**
     * 缓存过期时间(秒)
     */
    @Builder.Default
    private int cacheExpireSeconds = 300;
    
    /**
     * 是否返回总行数
     */
    @Builder.Default
    private boolean fetchTotalRows = false;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 查询超时时间(秒)
     */
    private Integer queryTimeout;
    
    /**
     * 创建一个简单的查询请求
     */
    public static NLQueryRequest simple(Long dataSourceId, String query) {
        return NLQueryRequest.builder()
                .dataSourceId(dataSourceId)
                .query(query)
                .build();
    }
    
    /**
     * 创建一个带参数的查询请求
     */
    public static NLQueryRequest withParameters(Long dataSourceId, String query, Map<String, Object> parameters) {
        return NLQueryRequest.builder()
                .dataSourceId(dataSourceId)
                .query(query)
                .parameters(parameters)
                .build();
    }
    
    /**
     * 添加参数
     */
    public NLQueryRequest addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * 添加标签
     */
    public NLQueryRequest addTag(String tag) {
        tags.add(tag);
        return this;
    }
}