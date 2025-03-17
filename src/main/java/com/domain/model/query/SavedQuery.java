package com.domain.model.query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SavedQuery {
    
    /**
     * 查询ID
     */
    private String id;
    
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
    private String dataSourceId;
    
    /**
     * 查询语句
     */
    private String query;
    
    /**
     * SQL语句
     */
    private String sql;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 查询标签
     */
    private List<String> tags;
    
    /**
     * 是否公开
     */
    private Boolean isPublic;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
