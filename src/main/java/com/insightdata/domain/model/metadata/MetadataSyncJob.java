package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;
import java.util.Map;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.common.enums.SyncType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 元数据同步作业领域模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataSyncJob {
    
    /**
     * 同步作业ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 同步类型
     */
    private SyncType type;
    
    /**
     * 同步状态
     */
    private SyncStatus status;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 进度(0-100)
     */
    private Integer progress;
    
    /**
     * 总项目数
     */
    private Integer totalItems;
    
    /**
     * 已处理项目数
     */
    private Integer processedItems;
    
    /**
     * 同步参数
     */
    private Map<String, Object> parameters;
    
    /**
     * 错误信息
     */
    private String errorMessage;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}