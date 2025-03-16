package com.insightdata.nlquery.intent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * 意图识别上下文
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
     * 上一次查询
     */
    private String previousQuery;
    
    /**
     * 上一次意图
     */
    private QueryIntent previousIntent;
    
    /**
     * 上下文参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 添加参数
     *
     * @param key 参数名
     * @param value 参数值
     */
    public void addParameter(String key, Object value) {
        if (parameters == null) {
            parameters = new HashMap<>();
        }
        parameters.put(key, value);
    }
    
    /**
     * 获取参数
     *
     * @param key 参数名
     * @return 参数值
     */
    public Object getParameter(String key) {
        if (parameters == null) {
            return null;
        }
        return parameters.get(key);
    }
}
