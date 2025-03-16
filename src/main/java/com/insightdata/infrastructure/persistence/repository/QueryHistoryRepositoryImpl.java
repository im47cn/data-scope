package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.repository.QueryHistoryRepository;
import com.insightdata.infrastructure.persistence.mapper.QueryHistoryMapper;

import lombok.RequiredArgsConstructor;

/**
 * 查询历史仓库的MyBatis实现
 */
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
    public Optional<QueryHistory> findById(Long id) {
        return Optional.ofNullable(queryHistoryMapper.selectById(id));
    }

    @Override
    public List<QueryHistory> findAll() {
        return queryHistoryMapper.selectAll();
    }

    @Override
    public void deleteById(Long id) {
        queryHistoryMapper.deleteById(id);
    }

    @Override
    public List<QueryHistory> findByDataSourceIdOrderByCreatedAtDesc(Long dataSourceId) {
        return queryHistoryMapper.selectByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
    }
}