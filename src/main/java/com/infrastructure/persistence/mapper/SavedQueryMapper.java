package com.infrastructure.persistence.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.domain.model.query.SavedQuery;

@Mapper
public interface SavedQueryMapper {
    
    /**
     * 插入保存的查询
     */
    void insert(SavedQuery savedQuery);
    
    /**
     * 更新保存的查询
     */
    void update(SavedQuery savedQuery);
    
    /**
     * 根据ID查询
     */
    SavedQuery selectById(@Param("id") String id);
    
    /**
     * 根据数据源ID查询
     */
    List<SavedQuery> selectByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    /**
     * 根据ID删除
     */
    void deleteById(@Param("id") String id);
    
    /**
     * 根据数据源ID删除
     */
    void deleteByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    /**
     * 根据名称模糊查询
     */
    List<SavedQuery> selectByNameLike(@Param("name") String name);
    
    /**
     * 根据标签查询
     */
    List<SavedQuery> selectByTags(@Param("tags") List<String> tags);
    
    /**
     * 根据是否公开查询
     */
    List<SavedQuery> selectByIsPublic(@Param("isPublic") Boolean isPublic);
    
    /**
     * 根据数据源ID和是否公开查询
     */
    List<SavedQuery> selectByDataSourceIdAndIsPublic(
            @Param("dataSourceId") String dataSourceId,
            @Param("isPublic") Boolean isPublic);
}