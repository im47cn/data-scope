package com.insightdata.domain.model.query;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 已保存的查询实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQuery {
    /**
     * 查询ID
     */
    private Long id;
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
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
     * 是否公开
     */
    private Boolean isPublic;
    
    /**
     * 创建人ID
     */
    private Long createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
