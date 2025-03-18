package com.insightdata.facade.metadata;

import com.insightdata.facade.metadata.enums.SyncStatus;
import com.insightdata.facade.metadata.enums.SyncType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 元数据同步作业DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MetadataSyncJobDTO {
    
    /**
     * 作业ID
     */
    private String id;
    
    /**
     * 数据源ID
     */
    private String dataSourceId;
    
    /**
     * 同步类型
     */
    private SyncType type;
    
    /**
     * 同步状态
     */
    private SyncStatus status;
    
    /**
     * 进度（0-100）
     */
    private Integer progress;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
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
    
    /**
     * 同步类型显示名称
     */
    private String typeDisplayName;
    
    /**
     * 同步状态显示名称
     */
    private String statusDisplayName;
    
    /**
     * 执行时长（毫秒）
     */
    private Long executionDuration;
    
    /**
     * 从领域模型创建DTO
     * 
     * @param syncJob 元数据同步作业
     * @return DTO对象
     */
    public static MetadataSyncJobDTO fromDomain(MetadataSyncJob syncJob) {
        if (syncJob == null) {
            return null;
        }
        
        MetadataSyncJobDTO dto = new MetadataSyncJobDTO();
        dto.setId(syncJob.getId());
        dto.setDataSourceId(syncJob.getDataSourceId());
        dto.setType(syncJob.getType());
        dto.setStatus(syncJob.getStatus());
        dto.setProgress(syncJob.getProgress());
        dto.setStartTime(syncJob.getStartTime());
        dto.setEndTime(syncJob.getEndTime());
        dto.setErrorMessage(syncJob.getErrorMessage());
        dto.setCreatedAt(syncJob.getCreatedAt());
        dto.setUpdatedAt(syncJob.getUpdatedAt());
        
        // 设置显示名称
        if (syncJob.getType() != null) {
            dto.setTypeDisplayName(syncJob.getType().getDisplayName());
        }
        if (syncJob.getStatus() != null) {
            dto.setStatusDisplayName(syncJob.getStatus().getDisplayName());
        }
        
        // 计算执行时长
        if (syncJob.getStartTime() != null) {
            LocalDateTime endTimeForCalc = syncJob.getEndTime() != null ? 
                    syncJob.getEndTime() : LocalDateTime.now();
            long durationMillis = java.time.Duration.between(syncJob.getStartTime(), endTimeForCalc).toMillis();
            dto.setExecutionDuration(durationMillis);
        }
        
        return dto;
    }

}