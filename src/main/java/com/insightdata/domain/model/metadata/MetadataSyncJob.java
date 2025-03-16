package com.insightdata.domain.model.metadata;

import java.time.LocalDateTime;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.common.enums.SyncType;

/**
 * 元数据同步作业
 */
public class MetadataSyncJob {
    /**
     * 作业ID
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
    
    public MetadataSyncJob() {
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Long getDataSourceId() {
        return dataSourceId;
    }
    
    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
    
    public SyncType getType() {
        return type;
    }
    
    public void setType(SyncType type) {
        this.type = type;
    }
    
    public SyncStatus getStatus() {
        return status;
    }
    
    public void setStatus(SyncStatus status) {
        this.status = status;
    }
    
    public Integer getProgress() {
        return progress;
    }
    
    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public static MetadataSyncJobBuilder builder() {
        return new MetadataSyncJobBuilder();
    }
    
    public static class MetadataSyncJobBuilder {
        private String id;
        private Long dataSourceId;
        private SyncType type;
        private SyncStatus status;
        private Integer progress;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String errorMessage;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        MetadataSyncJobBuilder() {
        }
        
        public MetadataSyncJobBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public MetadataSyncJobBuilder dataSourceId(Long dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }
        
        public MetadataSyncJobBuilder type(SyncType type) {
            this.type = type;
            return this;
        }
        
        public MetadataSyncJobBuilder status(SyncStatus status) {
            this.status = status;
            return this;
        }
        
        public MetadataSyncJobBuilder progress(Integer progress) {
            this.progress = progress;
            return this;
        }
        
        public MetadataSyncJobBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }
        
        public MetadataSyncJobBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }
        
        public MetadataSyncJobBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public MetadataSyncJobBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public MetadataSyncJobBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public MetadataSyncJob build() {
            MetadataSyncJob syncJob = new MetadataSyncJob();
            syncJob.setId(this.id);
            syncJob.setDataSourceId(this.dataSourceId);
            syncJob.setType(this.type);
            syncJob.setStatus(this.status);
            syncJob.setProgress(this.progress);
            syncJob.setStartTime(this.startTime);
            syncJob.setEndTime(this.endTime);
            syncJob.setErrorMessage(this.errorMessage);
            syncJob.setCreatedAt(this.createdAt);
            syncJob.setUpdatedAt(this.updatedAt);
            return syncJob;
        }
    }
}