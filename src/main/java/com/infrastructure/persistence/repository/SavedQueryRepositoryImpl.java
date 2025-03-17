package com.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.domain.model.query.SavedQuery;
import com.domain.repository.SavedQueryRepository;
import com.infrastructure.persistence.mapper.SavedQueryMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SavedQueryRepositoryImpl implements SavedQueryRepository {

    private final SavedQueryMapper savedQueryMapper;

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
    public List<SavedQuery> findByDataSourceId(String dataSourceId) {
        return savedQueryMapper.selectByDataSourceId(dataSourceId);
    }

    @Override
    public void deleteById(String id) {
        savedQueryMapper.deleteById(id);
    }

    @Override
    public void deleteByDataSourceId(String dataSourceId) {
        savedQueryMapper.deleteByDataSourceId(dataSourceId);
    }

    @Override
    public List<SavedQuery> findByNameLike(String name) {
        return savedQueryMapper.selectByNameLike(name);
    }

    @Override
    public List<SavedQuery> findByTags(List<String> tags) {
        return savedQueryMapper.selectByTags(tags);
    }

    @Override
    public List<SavedQuery> findByIsPublic(Boolean isPublic) {
        return savedQueryMapper.selectByIsPublic(isPublic);
    }

    @Override
    public List<SavedQuery> findByDataSourceIdAndIsPublic(String dataSourceId, Boolean isPublic) {
        return savedQueryMapper.selectByDataSourceIdAndIsPublic(dataSourceId, isPublic);
    }
}