package com.insightdata.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.query.SavedQuery;

/**
 * 保存的查询仓库
 */
@Repository
public interface SavedQueryRepository extends JpaRepository<SavedQuery, Long> {
    
    /**
     * 根据数据源ID查询保存的查询
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    List<SavedQuery> findByDataSourceIdOrderByUpdatedAtDesc(Long dataSourceId);
    
    /**
     * 根据名称查询保存的查询
     *
     * @param name 查询名称
     * @return 保存的查询
     */
    Optional<SavedQuery> findByName(String name);
    
    /**
     * 根据数据源ID和是否公开查询保存的查询
     *
     * @param dataSourceId 数据源ID
     * @param isPublic 是否公开
     * @return 保存的查询列表
     */
    List<SavedQuery> findByDataSourceIdAndIsPublicOrderByUpdatedAtDesc(Long dataSourceId, boolean isPublic);
}