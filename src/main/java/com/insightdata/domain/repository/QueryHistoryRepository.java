package com.insightdata.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.query.QueryHistory;

/**
 * 查询历史仓库
 */
@Repository
public interface QueryHistoryRepository extends JpaRepository<QueryHistory, Long> {
    
    /**
     * 根据数据源ID查询历史记录
     *
     * @param dataSourceId 数据源ID
     * @return 查询历史列表
     */
    List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(Long dataSourceId);
}