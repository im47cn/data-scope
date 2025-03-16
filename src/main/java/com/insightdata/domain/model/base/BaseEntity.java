package com.insightdata.domain.model.base;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 基础实体类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity {
    
    /**
     * ID
     */
    protected Long id;
    
    /**
     * 创建时间
     */
    protected LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    protected LocalDateTime updatedAt;
    
    /**
     * 创建人ID
     */
    protected Long createdBy;
    
    /**
     * 更新人ID
     */
    protected Long updatedBy;
    
    /**
     * 是否删除
     */
    protected boolean deleted;
    
    /**
     * 版本号
     */
    protected Integer version;
    
    /**
     * 备注
     */
    protected String remark;
    
    /**
     * 排序号
     */
    protected Integer sortOrder;
    
    /**
     * 租户ID
     */
    protected Long tenantId;
}