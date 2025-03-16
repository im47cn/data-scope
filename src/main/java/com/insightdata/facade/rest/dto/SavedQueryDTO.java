package com.insightdata.facade.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 保存查询请求DTO
 */
@Data
public class SavedQueryDTO {
    
    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空")
    private Long dataSourceId;
    
    /**
     * 查询名称
     */
    @NotBlank(message = "查询名称不能为空")
    private String name;
    
    /**
     * 查询描述
     */
    private String description;
    
    /**
     * 查询语句
     */
    @NotBlank(message = "查询语句不能为空")
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
     * 查询标签
     */
    private List<String> tags = new ArrayList<>();
    
    /**
     * 是否公开
     */
    private boolean isPublic;
    
    /**
     * 是否使用缓存
     */
    private boolean useCache = true;
    
    /**
     * 缓存过期时间(秒)
     */
    private int cacheExpireSeconds = 300;
    
    /**
     * 最大返回行数
     */
    private Integer maxRows;
    
    /**
     * 查询超时时间(秒)
     */
    private Integer queryTimeout;
    
    /**
     * 是否返回总行数
     */
    private boolean fetchTotalRows = false;
}