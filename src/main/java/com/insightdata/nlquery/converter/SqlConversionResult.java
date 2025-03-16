package com.insightdata.nlquery.converter;

import com.insightdata.nlquery.entity.EntityTag;
import com.insightdata.nlquery.intent.QueryIntent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL转换结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SqlConversionResult {
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 查询意图
     */
    private QueryIntent queryIntent;
    
    /**
     * 提取的实体
     */
    @Builder.Default
    private List<EntityTag> extractedEntities = new ArrayList<>();
    
    /**
     * 参数映射
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 警告信息
     */
    @Builder.Default
    private List<String> warnings = new ArrayList<>();
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 是否成功
     */
    private boolean success;
    
    /**
     * 置信度分数(0-1)
     */
    private double confidence;
    
    /**
     * 执行时间(毫秒)
     */
    private long executionTime;
    
    /**
     * 解释说明
     */
    @Builder.Default
    private List<String> explanations = new ArrayList<>();
    
    /**
     * 备选SQL
     */
    @Builder.Default
    private List<String> alternativeSqls = new ArrayList<>();
}