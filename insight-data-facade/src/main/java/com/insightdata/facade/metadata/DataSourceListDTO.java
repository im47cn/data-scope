package com.insightdata.facade.metadata;

import java.time.LocalDateTime;

import com.insightdata.facade.metadata.enums.DataSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据源列表DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataSourceListDTO {
    
    /**
     * 数据源ID
     */
    private String id;
    
    /**
     * 数据源名称
     */
    private String name;
    
    /**
     * 数据源类型
     */
    private DataSourceType type;
    
    /**
     * 是否启用
     */
    private Boolean enabled;
    
    /**
     * 描述信息
     */
    private String description;
    
    /**
     * 最后同步时间
     */
    private LocalDateTime lastSyncTime;
    
    /**
     * 最后连接时间
     */
    private LocalDateTime lastConnectedAt;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 表和视图数量
     */
    private Integer tableCount;
    
    /**
     * 标签
     */
    private String[] tags;
}