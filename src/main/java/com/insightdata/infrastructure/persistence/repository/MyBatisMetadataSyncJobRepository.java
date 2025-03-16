package com.insightdata.infrastructure.persistence.repository;

import java.util.List;

import com.insightdata.common.enums.SyncStatus;
import com.insightdata.infrastructure.persistence.entity.MetadataSyncJobEntity;

/**
 * 元数据同步作业MyBatis仓储接口
 */
public interface MyBatisMetadataSyncJobRepository {
    
    /**
     * 根据ID查询元数据同步作业
     *
     * @param id 作业ID
     * @return 元数据同步作业实体
     */
    MetadataSyncJobEntity findById(String id);
    
    /**
     * 保存元数据同步作业
     *
     * @param entity 元数据同步作业实体
     * @return 保存后的元数据同步作业实体
     */
    MetadataSyncJobEntity save(MetadataSyncJobEntity entity);
    
    /**
     * 删除元数据同步作业
     *
     * @param id 作业ID
     */
    void deleteById(String id);
    
    /**
     * 根据数据源ID查询同步作业
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByDataSourceId(Long dataSourceId);
    
    /**
     * 根据数据源ID和状态查询同步作业
     *
     * @param dataSourceId 数据源ID
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByDataSourceIdAndStatus(Long dataSourceId, SyncStatus status);
    
    /**
     * 根据状态查询同步作业
     *
     * @param status 同步状态
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findByStatus(SyncStatus status);
    
    /**
     * 获取数据源最近的同步作业
     *
     * @param dataSourceId 数据源ID
     * @return 同步作业
     */
    List<MetadataSyncJobEntity> findByDataSourceIdOrderByCreatedAtDesc(Long dataSourceId);
    
    /**
     * 删除数据源的所有同步作业
     *
     * @param dataSourceId 数据源ID
     */
    void deleteByDataSourceId(Long dataSourceId);
    
    /**
     * 查询所有元数据同步作业
     *
     * @return 同步作业列表
     */
    List<MetadataSyncJobEntity> findAll();
}