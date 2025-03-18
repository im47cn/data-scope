package com.domain.repository;

import java.util.List;

import com.domain.model.query.SavedQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SavedQueryRepository {
    
    SavedQuery selectById(@Param("id") String id);
    
    List<SavedQuery> selectByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    void insert(SavedQuery savedQuery);
    
    void update(SavedQuery savedQuery);
    
    void deleteById(@Param("id") String id);
    
    void deleteByDataSourceId(@Param("dataSourceId") String dataSourceId);
    
    List<SavedQuery> selectByNameLike(@Param("name") String name);
    
    List<SavedQuery> selectByTags(@Param("tags") List<String> tags);
    
    List<SavedQuery> selectByIsPublic(@Param("isPublic") Boolean isPublic);
    
    List<SavedQuery> selectByDataSourceIdAndIsPublic(
        @Param("dataSourceId") String dataSourceId,
        @Param("isPublic") Boolean isPublic
    );
    
    void incrementUsageCount(@Param("id") String id);
    
    void updateExecutionStats(
        @Param("id") String id,
        @Param("executionTime") long executionTime
    );
}