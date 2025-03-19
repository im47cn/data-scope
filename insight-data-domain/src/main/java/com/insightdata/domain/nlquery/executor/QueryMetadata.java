package com.insightdata.domain.nlquery.executor;

import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * 查询元数据
 * 
 * 包含查询的附加信息和配置
 */
@Data
@Builder
public class QueryMetadata {
    /**
     * 查询的超时时间（毫秒）
     */
    private long timeout;
    
    /**
     * 最大返回行数
     */
    private int maxRows;
    
    /**
     * 查询标签
     */
    private String tag;
    
    /**
     * 查询的来源（例如：用户交互、定时任务等）
     */
    private String source;
    
    /**
     * 查询的附加参数
     */
    private Map<String, Object> parameters;
}