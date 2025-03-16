package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.common.enums.SyncType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * 元数据同步作业实体类
 */
@Entity
@Table(name = "metadata_sync_job")
public class MetadataSyncJobEntity {
    
    @Id
    @Column(name = "id", nullable = false)
    private String id;
    
    @Column(name = "data_source_id", nullable = false)
    private Long dataSourceId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SyncType type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SyncStatus status;
    
    @Column(name = "progress", nullable = false)
    private Integer progress;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public MetadataSyncJobEntity() {
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
    
    public static MetadataSyncJobEntityBuilder builder() {
        return new MetadataSyncJobEntityBuilder();
    }
    
    public static class MetadataSyncJobEntityBuilder {
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
        
        MetadataSyncJobEntityBuilder() {
        }
        
        public MetadataSyncJobEntityBuilder id(String id) {
            this.id = id;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder dataSourceId(Long dataSourceId) {
            this.dataSourceId = dataSourceId;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder type(SyncType type) {
            this.type = type;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder status(SyncStatus status) {
            this.status = status;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder progress(Integer progress) {
            this.progress = progress;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder errorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }
        
        public MetadataSyncJobEntityBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }
        
        public MetadataSyncJobEntity build() {
            MetadataSyncJobEntity entity = new MetadataSyncJobEntity();
            entity.setId(this.id);
            entity.setDataSourceId(this.dataSourceId);
            entity.setType(this.type);
            entity.setStatus(this.status);
            entity.setProgress(this.progress);
            entity.setStartTime(this.startTime);
            entity.setEndTime(this.endTime);
            entity.setErrorMessage(this.errorMessage);
            entity.setCreatedAt(this.createdAt);
            entity.setUpdatedAt(this.updatedAt);
            return entity;
        }
    }
}