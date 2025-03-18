package com.facade.rest.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 自然语言查询请求DTO
 */
@Data
public class NLQueryRequestDTO {
    
    /**
     * 数据源ID
     */
    @NotNull(message = "数据源ID不能为空")
    private String dataSourceId;
    
    /**
     * 查询语句
     */
    @NotBlank(message = "查询语句不能为空")
    private String query;
    
    /**
     * 查询参数
     */
    private Map<String, Object> parameters;
    
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