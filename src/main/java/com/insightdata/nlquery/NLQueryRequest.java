package com.insightdata.nlquery;

import java.util.Map;

import lombok.Data;

/**
 * 自然语言查询请求
 */
@Data
public class NLQueryRequest {

    /**
     * 数据源ID
     */
    private Long dataSourceId;

    /**
     * 自然语言查询内容
     */
    private String query;

    /**
     * 查询参数
     * 可用于传递上下文信息或限制条件
     */
    private Map<String, Object> parameters;
}