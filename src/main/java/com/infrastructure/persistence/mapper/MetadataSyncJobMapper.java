package com.infrastructure.persistence.mapper;

import com.common.enums.SyncStatus;
import com.infrastructure.persistence.entity.MetadataSyncJobEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 元数据同步作业Mapper接口
 */
@Mapper
public interface MetadataSyncJobMapper {
    
    /**
     * 插入同步作业
     * 
     * @param entity 同步作业实体
     * @return 影响的行数
     */
    int insert(MetadataSyncJobEntity entity);
    
    /**
     * 更新同步作业
     * 
     * @param entity 同步作业实体
     * @return 影响的行数
     */
    int update(MetadataSyncJobEntity entity);
    
    /**
     * 根据ID查询同步作业
     * 
     * @param id 同步作业ID
     * @return 同步作业实体
     */
    MetadataSyncJobEntity selectById(@Param("id") String id);
    
    /**
     * 根据数据源ID查询同步作业列表
     * 
     * @param dataSourceId 数据源ID
     * @return 同步作业实体列表
     */
    List<MetadataSyncJobEntity> selectByDataSourceId(@Param("dataSourceId") Long dataSourceId);
    
    /**
     * 根据数据源ID和状态查询同步作业列表
     * 
     * @param dataSourceId 数据源ID
     * @param status 同步状态
     * @return 同步作业实体列表
     */
    List<MetadataSyncJobEntity> selectByDataSourceIdAndStatus(
            @Param("dataSourceId") Long dataSourceId, 
            @Param("status") SyncStatus status);
    
    /**
     * 根据状态查询同步作业列表
     * 
     * @param status 同步状态
     * @return 同步作业实体列表
     */
    List<MetadataSyncJobEntity> selectByStatus(@Param("status") SyncStatus status);
    
    /**
     * 查询数据源最新的同步作业
     * 
     * @param dataSourceId 数据源ID
     * @return 同步作业实体
     */
    MetadataSyncJobEntity selectLatestByDataSourceId(@Param("dataSourceId") Long dataSourceId);
    
    /**
     * 根据ID删除同步作业
     * 
     * @param id 同步作业ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") String id);
    
    /**
     * 根据数据源ID删除同步作业
     * 
     * @param dataSourceId 数据源ID
     * @return 影响的行数
     */
    int deleteByDataSourceId(@Param("dataSourceId") Long dataSourceId);
}