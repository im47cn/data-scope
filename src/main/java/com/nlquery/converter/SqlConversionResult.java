package com.nlquery.converter;

import java.util.List;
import java.util.Map;

import com.nlquery.entity.EntityTag;
import com.nlquery.intent.QueryIntent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
     * SQL参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 提取的实体
     */
    private List<EntityTag> extractedEntities;
    
    /**
     * 查询意图
     */
    private QueryIntent queryIntent;
    
    /**
     * 转换是否成功
     */
    private boolean success;
    
    /**
     * 置信度
     */
    private double confidence;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * SQL解释
     */
    private List<String> explanations;
    
    /**
     * 备选SQL语句
     */
    private List<String> alternativeSqls;
}