package com.insightdata.api.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 保存查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQueryDTO {
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 自然语言查询
     */
    private String query;
    
    /**
     * 生成的SQL
     */
    private String sql;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 是否公开
     */
    private boolean isPublic;
}