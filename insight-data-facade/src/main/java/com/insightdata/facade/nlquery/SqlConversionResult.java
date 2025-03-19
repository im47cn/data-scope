package com.insightdata.facade.nlquery;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * SQL转换结果DTO
 * 自然语言转SQL的结果对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlConversionResult {
    /**
     * 生成的SQL语句
     */
    private String sql;
    
    /**
     * SQL参数映射
     */
    private Map<String, Object> parameters;
    
    /**
     * 转换置信度（0-1之间的值）
     */
    private Double confidence;
    
    /**
     * SQL的解释信息
     */
    private List<String> explanations;
    
    /**
     * 备选SQL语句
     */
    private List<String> alternativeSqls;
    
    /**
     * 转换是否成功
     */
    private boolean success;
    
    /**
     * 错误信息（如果转换失败）
     */
    private String errorMessage;
    
    /**
     * 执行时间（毫秒）
     */
    private Long executionTime;
    
    /**
     * 获取不可变的参数Map
     * 
     * @return 不可变Map视图，或null（如果原始值为null）
     */
    public Map<String, Object> getParameters() {
        return parameters != null ? Collections.unmodifiableMap(parameters) : null;
    }
    
    /**
     * 获取不可变的解释列表
     * 
     * @return 不可变List视图，或null（如果原始值为null）
     */
    public List<String> getExplanations() {
        return explanations != null ? Collections.unmodifiableList(explanations) : null;
    }
    
    /**
     * 获取不可变的备选SQL列表
     * 
     * @return 不可变List视图，或null（如果原始值为null）
     */
    public List<String> getAlternativeSqls() {
        return alternativeSqls != null ? Collections.unmodifiableList(alternativeSqls) : null;
    }
}