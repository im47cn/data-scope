package com.insightdata.nlquery.intent;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 意图识别上下文
 */
@Data
public class IntentRecognitionContext {
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 会话ID
     */
    private String sessionId;
    
    /**
     * 领域
     */
    private String domain;
    
    /**
     * 上下文参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 是否启用模糊匹配
     */
    private boolean fuzzyMatch = true;
    
    /**
     * 最小匹配阈值(0-1)
     */
    private double minMatchThreshold = 0.7;
    
    /**
     * 最大匹配数量
     */
    private int maxMatchCount = 10;
    
    public IntentRecognitionContext() {
        this.parameters = new HashMap<>();
    }
    
    /**
     * 添加参数
     */
    public void addParameter(String key, Object value) {
        this.parameters.put(key, value);
    }
    
    /**
     * 获取参数
     */
    public Object getParameter(String key) {
        return this.parameters.get(key);
    }
    
    /**
     * 获取参数
     */
    public <T> T getParameter(String key, Class<T> type) {
        Object value = this.parameters.get(key);
        if (value != null && type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
    
    /**
     * 获取字符串参数
     */
    public String getStringParameter(String key) {
        return getParameter(key, String.class);
    }
    
    /**
     * 获取整数参数
     */
    public Integer getIntegerParameter(String key) {
        return getParameter(key, Integer.class);
    }
    
    /**
     * 获取长整数参数
     */
    public Long getLongParameter(String key) {
        return getParameter(key, Long.class);
    }
    
    /**
     * 获取布尔参数
     */
    public Boolean getBooleanParameter(String key) {
        return getParameter(key, Boolean.class);
    }
    
    /**
     * 获取双精度参数
     */
    public Double getDoubleParameter(String key) {
        return getParameter(key, Double.class);
    }
}
