package com.nlquery.preprocess;

import java.util.List;
import java.util.Map;

import com.domain.model.metadata.SchemaInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 预处理上下文类
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
    private String language;
    
    /**
     * 元数据
     */
    private SchemaInfo metadata;
    
    /**
     * 参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 标签
     */
    private List<String> tags;
    
    /**
     * 是否使用元数据
     */
    private Boolean useMetadata;
    
    /**
     * 是否使用模糊匹配
     */
    private Boolean useFuzzyMatching;
    
    /**
     * 最小置信度
     */
    private Double minConfidence;
    
    /**
     * 是否需要纠错
     */
    private Boolean needsCorrection;
    
    /**
     * 是否已纠错
     */
    private Boolean isCorrected;
    
    /**
     * 纠错消息
     */
    private String correctionMessage;
    
    /**
     * 获取数据源ID
     */
    public Long getDataSourceId() {
        return dataSourceId;
    }
    
    /**
     * 获取用户ID
     */
    public Long getUserId() {
        return userId;
    }
    
    /**
     * 获取会话ID
     */
    public String getSessionId() {
        return sessionId;
    }
    
    /**
     * 获取领域
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * 获取语言
     */
    public String getLanguage() {
        return language;
    }
    
    /**
     * 获取元数据
     */
    public SchemaInfo getMetadata() {
        return metadata;
    }
    
    /**
     * 设置元数据
     */
    public void setMetadata(SchemaInfo metadata) {
        this.metadata = metadata;
    }
    
    /**
     * 获取参数
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }
    
    /**
     * 获取标签
     */
    public List<String> getTags() {
        return tags;
    }
    
    /**
     * 是否使用元数据
     */
    public boolean isUseMetadata() {
        return Boolean.TRUE.equals(useMetadata);
    }
    
    /**
     * 是否使用模糊匹配
     */
    public boolean isUseFuzzyMatching() {
        return Boolean.TRUE.equals(useFuzzyMatching);
    }
    
    /**
     * 获取最小置信度
     */
    public double getMinConfidence() {
        return minConfidence != null ? minConfidence : 0.5;
    }
}
