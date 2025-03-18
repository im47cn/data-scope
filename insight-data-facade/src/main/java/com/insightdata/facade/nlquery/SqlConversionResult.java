package com.insightdata.facade.nlquery;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自然语言转SQL查询结果DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlConversionResult {
    
    /**
     * 转换后的SQL语句
     */
    private String sql;
    
    /**
     * SQL参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 置信度，值范围0-1
     */
    private Double confidence;
    
    /**
     * SQL解释说明
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
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 提取的实体
     */
    private List<EntityTag> extractedEntities;
    
    /**
     * 查询意图
     */
    private QueryIntent queryIntent;
    
    /**
     * 执行耗时（毫秒）
     */
    private Long executionTime;
}