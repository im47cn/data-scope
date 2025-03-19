package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.domain.metadata.repository.SavedQueryRepository;
import com.insightdata.infrastructure.persistence.mapper.SavedQueryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisSavedQueryRepository implements SavedQueryRepository {

    @Autowired
    private SavedQueryMapper savedQueryMapper;

    @Override
    public SavedQuery save(SavedQuery savedQuery) {
        if (savedQuery.getId() == null) {
            savedQueryMapper.insert(savedQuery);
        } else {
            savedQueryMapper.update(savedQuery);
        }
        return savedQuery;
    }

    @Override
    public Optional<SavedQuery> findById(String id) {
        return Optional.ofNullable(savedQueryMapper.selectById(id));
    }

    @Override
    public List<SavedQuery> findByDataSourceIdAndUserId(String dataSourceId, String userId) {
        return savedQueryMapper.selectByDataSourceIdAndUserId(dataSourceId, userId);
    }

    @Override
    public void deleteById(String id) {
        savedQueryMapper.deleteById(id);
    }

    @Override
    public List<SavedQuery> findByTags(List<String> tags) {
        return savedQueryMapper.selectByTags(tags);
    }

    @Override
    public List<SavedQuery> findByNameLike(String name) {
        return savedQueryMapper.selectByNameLike(name);
    }

    @Override
    public void updateUsageStats(String id, long executionTime) {
        savedQueryMapper.updateUsageStats(id, executionTime);
    }

    @Override
    public List<SavedQuery> findByDataSourceId(String dataSourceId) {
        return savedQueryMapper.selectByDataSourceId(dataSourceId);
    }

    @Override
    public boolean existsByName(String name) {
        return savedQueryMapper.countByName(name) > 0;
    }

    @Override
    public void deleteByDataSourceId(String dataSourceId) {
        savedQueryMapper.deleteByDataSourceId(dataSourceId);
    }
}