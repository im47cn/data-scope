package com.insightdata.facade.query;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新保存的查询请求DTO
 */
@Data
public class UpdateSavedQueryDTO {
    
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