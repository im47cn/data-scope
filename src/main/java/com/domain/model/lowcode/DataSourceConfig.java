package com.domain.model.lowcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置
 * 定义低代码组件的数据来源配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceConfig {
    
    /**
     * 数据来源类型：QUERY - SQL查询, API - 接口调用, STATIC - 静态数据
     */
    private String type;
    
    /**
     * 关联的查询ID
     */
    private Long queryId;
    
    /**
     * SQL语句 (当type=QUERY时使用)
     */
    private String sql;
    
    /**
     * API URL (当type=API时使用)
     */
    private String apiUrl;
    
    /**
     * API 请求方法 (当type=API时使用)
     */
    private String apiMethod;
    
    /**
     * API 请求头 (当type=API时使用)
     */
    @Builder.Default
    private Map<String, String> apiHeaders = new HashMap<>();
    
    /**
     * API 请求参数 (当type=API时使用)
     */
    @Builder.Default
    private Map<String, Object> apiParams = new HashMap<>();
    
    /**
     * 静态数据 (当type=STATIC时使用)
     */
    @Builder.Default
    private List<Map<String, Object>> staticData = new ArrayList<>();
    
    /**
     * 数据转换器配置
     * 定义如何转换原始数据
     */
    @Builder.Default
    private Map<String, Object> transformer = new HashMap<>();
    
    /**
     * 数据刷新间隔(秒)
     * 0表示不自动刷新
     */
    private int refreshInterval;
}