package com.insightdata.infrastructure.persistence.mapper;

import com.insightdata.domain.model.query.SavedQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 保存的查询MyBatis映射器接口
 */
@Mapper
public interface SavedQueryMapper {
    
    /**
     * 插入新的保存查询
     * 
     * @param savedQuery 要保存的查询
     * @return 影响的行数
     */
    int insert(SavedQuery savedQuery);
    
    /**
     * 更新保存的查询
     * 
     * @param savedQuery 要更新的查询
     * @return 影响的行数
     */
    int update(SavedQuery savedQuery);
    
    /**
     * 根据ID查询保存的查询
     * 
     * @param id 查询ID
     * @return 保存的查询
     */
    SavedQuery selectById(@Param("id") Long id);
    
    /**
     * 查询所有保存的查询
     * 
     * @return 保存的查询列表
     */
    List<SavedQuery> selectAll();
    
    /**
     * 根据ID删除保存的查询
     * 
     * @param id 查询ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据数据源ID查询保存的查询，按更新时间降序排序
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    List<SavedQuery> selectByDataSourceIdOrderByUpdatedAtDesc(@Param("dataSourceId") Long dataSourceId);
    
    /**
     * 根据名称查询保存的查询
     *
     * @param name 查询名称
     * @return 保存的查询
     */
    SavedQuery selectByName(@Param("name") String name);
    
    /**
     * 根据数据源ID和是否公开查询保存的查询，按更新时间降序排序
     *
     * @param dataSourceId 数据源ID
     * @param isPublic 是否公开
     * @return 保存的查询列表
     */
    List<SavedQuery> selectByDataSourceIdAndIsPublicOrderByUpdatedAtDesc(
            @Param("dataSourceId") Long dataSourceId, 
            @Param("isPublic") boolean isPublic);
}