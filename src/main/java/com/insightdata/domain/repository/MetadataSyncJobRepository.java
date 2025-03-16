package com.insightdata.domain.repository;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.domain.model.metadata.MetadataSyncJob;

import java.util.List;
import java.util.Optional;

/**
 * 元数据同步作业仓储接口
 */
public interface MetadataSyncJobRepository {
    
    /**
     * 保存同步作业
     * 
     * @param syncJob 同步作业
     * @return 保存后的同步作业
     */
    MetadataSyncJob save(MetadataSyncJob syncJob);
    
    /**
     * 根据ID查询同步作业
     * 
     * @param id 同步作业ID
     * @return 同步作业(如果存在)
     */
    Optional<MetadataSyncJob> findById(String id);
    
    /**
     * 根据数据源ID查询同步作业
     * 
     * @param dataSourceId 数据源ID
     * @return 同步作业列表
     */
    List<MetadataSyncJob> findByDataSourceId(Long dataSourceId);
    
    /**
     * 根据数据源ID和状态查询同步作业
     * 
     * @param dataSourceId 数据源ID
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJob> findByDataSourceIdAndStatus(Long dataSourceId, SyncStatus status);
    
    /**
     * 根据状态查询同步作业
     * 
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJob> findByStatus(SyncStatus status);
    
    /**
     * 获取数据源最近的同步作业
     * 
     * @param dataSourceId 数据源ID
     * @return 同步作业(如果存在)
     */
    Optional<MetadataSyncJob> findLatestByDataSourceId(Long dataSourceId);
    
    /**
     * 根据ID删除同步作业
     * 
     * @param id 同步作业ID
     */
    void deleteById(String id);
    
    /**
     * 删除数据源的所有同步作业
     * 
     * @param dataSourceId 数据源ID
     */
    void deleteByDataSourceId(Long dataSourceId);
}