package com.insightdata.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新保存的查询请求DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSavedQueryDTO {
    
    /**
     * 查询名称
     */
    private String name;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 是否公开
     */
    private boolean isPublic;
}