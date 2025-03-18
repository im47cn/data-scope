package com.insightdata.domain.repository;

import com.insightdata.domain.query.model.QueryHistory;

import java.util.List;
import java.util.Optional;

public interface QueryHistoryRepository {

    List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId);

    QueryHistory save(QueryHistory queryHistory);

    Optional<QueryHistory> findById(String id);
}