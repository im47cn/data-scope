package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import com.insightdata.domain.repository.QueryHistoryRepository;
import com.insightdata.infrastructure.persistence.mapper.QueryHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.insightdata.domain.query.model.QueryHistory;


@Repository
public class MyBatisQueryHistoryRepository implements QueryHistoryRepository {

    @Autowired
    private QueryHistoryMapper queryHistoryMapper;

    @Override
    public QueryHistory save(QueryHistory queryHistory) {
        if (queryHistory.getId() == null) {
            queryHistoryMapper.insert(queryHistory);
        } else {
            queryHistoryMapper.update(queryHistory);
        }
        return queryHistory;
    }

    @Override
    public Optional<QueryHistory> findById(String id) {
        return Optional.ofNullable(queryHistoryMapper.selectById(id));
    }

    @Override
    public List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId) {
        return queryHistoryMapper.selectByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }
}