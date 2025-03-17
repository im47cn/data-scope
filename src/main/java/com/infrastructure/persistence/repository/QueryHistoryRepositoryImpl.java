package com.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.domain.model.query.QueryHistory;
import com.domain.repository.QueryHistoryRepository;
import com.infrastructure.persistence.mapper.QueryHistoryMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QueryHistoryRepositoryImpl implements QueryHistoryRepository {

    private final QueryHistoryMapper queryHistoryMapper;

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
    public List<QueryHistory> findByDataSourceId(String dataSourceId) {
        return queryHistoryMapper.selectByDataSourceId(dataSourceId);
    }

    @Override
    public List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(String dataSourceId) {
        return queryHistoryMapper.selectByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }

    @Override
    public void deleteById(String id) {
        queryHistoryMapper.deleteById(id);
    }

    @Override
    public void deleteByDataSourceId(String dataSourceId) {
        queryHistoryMapper.deleteByDataSourceId(dataSourceId);
    }

    @Override
    public List<QueryHistory> findByDataSourceIdAndTimeRange(String dataSourceId, String startTime, String endTime) {
        return queryHistoryMapper.selectByDataSourceIdAndTimeRange(dataSourceId, startTime, endTime);
    }

    @Override
    public List<QueryHistory> findByDataSourceIdAndSqlPattern(String dataSourceId, String sqlPattern) {
        return queryHistoryMapper.selectByDataSourceIdAndSqlPattern(dataSourceId, sqlPattern);
    }

    @Override
    public List<QueryHistory> findByDataSourceIdAndSuccess(String dataSourceId, Boolean success) {
        return queryHistoryMapper.selectByDataSourceIdAndSuccess(dataSourceId, success);
    }
}