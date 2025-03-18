package com.domain.repository;

import com.domain.model.query.QueryHistory;
import java.util.List;
import java.util.Optional;

public interface QueryHistoryRepository {

    List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId);
    
    QueryHistory save(QueryHistory queryHistory);
    
    Optional<QueryHistory> findById(String id);
}