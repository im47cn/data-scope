package com.insightdata.nlquery.preprocess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预处理上下文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PreprocessContext {
    
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
     * 语言
     */
    @Builder.Default
    private String language = "zh-CN";
    
    /**
     * 是否使用模糊匹配
     */
    @Builder.Default
    private boolean useFuzzyMatching = true;
    
    /**
     * 最小置信度
     */
    @Builder.Default
    private double minConfidence = 0.6;
    
    /**
     * 是否使用元数据
     */
    @Builder.Default
    private boolean useMetadata = true;
    
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
     * 上下文参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 上下文标签
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 创建一个简单的预处理上下文
     */
    public static PreprocessContext simple(Long dataSourceId) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .build();
    }
    
    /**
     * 创建一个带用户信息的预处理上下文
     */
    public static PreprocessContext withUser(Long dataSourceId, Long userId) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .userId(userId)
                .build();
    }
    
    /**
     * 创建一个带会话信息的预处理上下文
     */
    public static PreprocessContext withSession(Long dataSourceId, String sessionId) {
        return PreprocessContext.builder()
                .dataSourceId(dataSourceId)
                .sessionId(sessionId)
                .build();
    }
    
    /**
     * 添加参数
     */
    public PreprocessContext addParameter(String key, Object value) {
        parameters.put(key, value);
        return this;
    }
    
    /**
     * 添加标签
     */
    public PreprocessContext addTag(String tag) {
        tags.add(tag);
        return this;
    }
    
    /**
     * 获取参数
     */
    public Object getParameter(String key) {
        return parameters.get(key);
    }
    
    /**
     * 获取参数，带默认值
     */
    public Object getParameter(String key, Object defaultValue) {
        return parameters.getOrDefault(key, defaultValue);
    }
    
    /**
     * 是否包含参数
     */
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
    
    /**
     * 移除参数
     */
    public PreprocessContext removeParameter(String key) {
        parameters.remove(key);
        return this;
    }
    
    /**
     * 清空参数
     */
    public PreprocessContext clearParameters() {
        parameters.clear();
        return this;
    }
    
    /**
     * 清空标签
     */
    public PreprocessContext clearTags() {
        tags.clear();
        return this;
    }
}
