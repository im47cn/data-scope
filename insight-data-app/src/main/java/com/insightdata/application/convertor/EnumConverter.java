package com.insightdata.application.convertor;

import com.insightdata.domain.metadata.model.MetadataSyncJob;
import com.insightdata.facade.metadata.enums.SyncStatus;
import com.insightdata.facade.metadata.enums.SyncType;

/**
 * 枚举类型转换工具类
 */
public class EnumConverter {

    /**
     * 将facade包中的SyncStatus转换为domain包中的SyncStatus
     *
     * @param status facade包中的SyncStatus
     * @return domain包中的SyncStatus
     */
    public static com.insightdata.domain.metadata.enums.SyncStatus toDomainSyncStatus(SyncStatus status) {
        if (status == null) {
            return null;
        }
        
        switch (status) {
            case PENDING:
                return com.insightdata.domain.metadata.enums.SyncStatus.PENDING;
            case RUNNING:
                return com.insightdata.domain.metadata.enums.SyncStatus.RUNNING;
            case COMPLETED:
                return com.insightdata.domain.metadata.enums.SyncStatus.COMPLETED;
            case FAILED:
                return com.insightdata.domain.metadata.enums.SyncStatus.FAILED;
            case CANCELLED:
                return com.insightdata.domain.metadata.enums.SyncStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Unknown SyncStatus: " + status);
        }
    }

    /**
     * 将domain包中的SyncStatus转换为facade包中的SyncStatus
     *
     * @param status domain包中的SyncStatus
     * @return facade包中的SyncStatus
     */
    public static SyncStatus toFacadeSyncStatus(com.insightdata.domain.metadata.enums.SyncStatus status) {
        if (status == null) {
            return null;
        }
        
        switch (status) {
            case PENDING:
                return SyncStatus.PENDING;
            case RUNNING:
                return SyncStatus.RUNNING;
            case COMPLETED:
                return SyncStatus.COMPLETED;
            case FAILED:
                return SyncStatus.FAILED;
            case CANCELLED:
                return SyncStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Unknown SyncStatus: " + status);
        }
    }

    /**
     * 将facade包中的SyncType转换为domain包中的SyncType
     *
     * @param type facade包中的SyncType
     * @return domain包中的SyncType
     */
    public static com.insightdata.domain.metadata.enums.SyncType toDomainSyncType(SyncType type) {
        if (type == null) {
            return null;
        }
        
        switch (type) {
            case FULL:
                return com.insightdata.domain.metadata.enums.SyncType.FULL;
            case INCREMENTAL:
                return com.insightdata.domain.metadata.enums.SyncType.INCREMENTAL;
            case METADATA_ONLY:
                return com.insightdata.domain.metadata.enums.SyncType.METADATA_ONLY;
            default:
                throw new IllegalArgumentException("Unknown SyncType: " + type);
        }
    }

    /**
     * 将domain包中的SyncType转换为facade包中的SyncType
     *
     * @param type domain包中的SyncType
     * @return facade包中的SyncType
     */
    public static SyncType toFacadeSyncType(com.insightdata.domain.metadata.enums.SyncType type) {
        if (type == null) {
            return null;
        }
        
        switch (type) {
            case FULL:
                return SyncType.FULL;
            case INCREMENTAL:
                return SyncType.INCREMENTAL;
            case METADATA_ONLY:
                return SyncType.METADATA_ONLY;
            default:
                throw new IllegalArgumentException("Unknown SyncType: " + type);
        }
    }
    
    /**
     * 将domain包中的MetadataSyncJob转换为facade包中的MetadataSyncJob
     * 
     * @param domainJob domain包中的MetadataSyncJob
     * @return facade包中的MetadataSyncJob
     */
    public static MetadataSyncJob toFacadeMetadataSyncJob(com.insightdata.domain.metadata.model.MetadataSyncJob domainJob) {
        if (domainJob == null) {
            return null;
        }
        
        MetadataSyncJob facadeJob = new MetadataSyncJob();
        facadeJob.setId(domainJob.getId());
        facadeJob.setDataSourceId(domainJob.getDataSourceId());
        facadeJob.setType(domainJob.getType());
        facadeJob.setStatus(domainJob.getStatus());
        facadeJob.setStartTime(domainJob.getStartTime());
        facadeJob.setEndTime(domainJob.getEndTime());
        facadeJob.setProgress(domainJob.getProgress());
        facadeJob.setTotalItems(domainJob.getTotalItems());
        facadeJob.setProcessedItems(domainJob.getProcessedItems());
        facadeJob.setParameters(domainJob.getParameters());
        facadeJob.setErrorMessage(domainJob.getErrorMessage());
        facadeJob.setCreatedAt(domainJob.getCreatedAt());
        facadeJob.setUpdatedAt(domainJob.getUpdatedAt());
        
        return facadeJob;
    }
    
    /**
     * 将facade包中的MetadataSyncJob转换为domain包中的MetadataSyncJob
     * 
     * @param facadeJob facade包中的MetadataSyncJob
     * @return domain包中的MetadataSyncJob
     */
    public static com.insightdata.domain.metadata.model.MetadataSyncJob toDomainMetadataSyncJob(MetadataSyncJob facadeJob) {
        if (facadeJob == null) {
            return null;
        }
        
        com.insightdata.domain.metadata.model.MetadataSyncJob domainJob = new com.insightdata.domain.metadata.model.MetadataSyncJob();
        domainJob.setId(facadeJob.getId());
        domainJob.setDataSourceId(facadeJob.getDataSourceId());
        domainJob.setType(facadeJob.getType());
        domainJob.setStatus(facadeJob.getStatus());
        domainJob.setStartTime(facadeJob.getStartTime());
        domainJob.setEndTime(facadeJob.getEndTime());
        domainJob.setProgress(facadeJob.getProgress());
        domainJob.setTotalItems(facadeJob.getTotalItems());
        domainJob.setProcessedItems(facadeJob.getProcessedItems());
        domainJob.setParameters(facadeJob.getParameters());
        domainJob.setErrorMessage(facadeJob.getErrorMessage());
        domainJob.setCreatedAt(facadeJob.getCreatedAt());
        domainJob.setUpdatedAt(facadeJob.getUpdatedAt());
        
        return domainJob;
    }
}