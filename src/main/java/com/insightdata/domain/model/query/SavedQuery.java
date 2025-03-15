package com.insightdata.domain.model.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 保存的查询
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQuery {
    
    /**
     * 保存的查询ID
     */
    private Long id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 自然语言查询
     */
    private String naturalLanguageQuery;
    
    /**
     * 生成的SQL
     */
    private String generatedSql;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 创建者ID
     */
    private Long createdBy;
    
    /**
     * 是否公开
     */
    private boolean isPublic;
    
    /**
     * 描述
     */
    private String description;
}
