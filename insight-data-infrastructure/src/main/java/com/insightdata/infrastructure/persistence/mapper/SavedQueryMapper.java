package com.insightdata.infrastructure.persistence.mapper;

import java.util.List;

import com.insightdata.domain.query.model.SavedQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SavedQueryMapper {
    /**
     * 插入新的查询模板
     */
    void insert(SavedQuery savedQuery);

    /**
     * 更新查询模板
     */
    void update(SavedQuery savedQuery);

    /**
     * 根据ID查询
     */
    SavedQuery selectById(@Param("id") String id);

    /**
     * 根据数据源ID和用户ID查询
     */
    List<SavedQuery> selectByDataSourceIdAndUserId(
            @Param("dataSourceId") String dataSourceId,
            @Param("userId") String userId);

    /**
     * 根据ID删除
     */
    void deleteById(@Param("id") String id);

    /**
     * 根据标签查询
     */
    List<SavedQuery> selectByTags(@Param("tags") List<String> tags);

    /**
     * 根据名称模糊查询
     */
    List<SavedQuery> selectByNameLike(@Param("name") String name);

    /**
     * 更新使用统计信息
     */
    void updateUsageStats(
            @Param("id") String id,
            @Param("executionTime") long executionTime);

    /**
     * 根据数据源ID查询
     */
    List<SavedQuery> selectByDataSourceId(@Param("dataSourceId") String dataSourceId);

    /**
     * 根据名称统计数量
     */
    int countByName(@Param("name") String name);

    /**
     * 根据数据源ID删除
     */
    void deleteByDataSourceId(@Param("dataSourceId") String dataSourceId);
}