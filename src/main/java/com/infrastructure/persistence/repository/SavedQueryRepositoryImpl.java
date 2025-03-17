package com.infrastructure.persistence.repository;

import com.domain.model.query.SavedQuery;
import com.domain.repository.SavedQueryRepository;
import com.infrastructure.persistence.mapper.SavedQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 保存的查询仓库的MyBatis实现
 */
@Repository
@RequiredArgsConstructor
public class SavedQueryRepositoryImpl implements SavedQueryRepository {

    private final SavedQueryMapper savedQueryMapper;

    @Override
    public SavedQuery save(SavedQuery savedQuery) {
        if (savedQuery.getId() == null) {
            // 新增记录，设置创建时间和更新时间
            if (savedQuery.getCreatedAt() == null) {
                savedQuery.setCreatedAt(LocalDateTime.now());
            }
            if (savedQuery.getUpdatedAt() == null) {
                savedQuery.setUpdatedAt(LocalDateTime.now());
            }
            savedQueryMapper.insert(savedQuery);
        } else {
            // 更新记录，更新更新时间
            savedQuery.setUpdatedAt(LocalDateTime.now());
            savedQueryMapper.update(savedQuery);
        }
        return savedQuery;
    }

    @Override
    public Optional<SavedQuery> findById(String id) {
        return Optional.ofNullable(savedQueryMapper.selectById(id));
    }

    @Override
    public List<SavedQuery> findAll() {
        return savedQueryMapper.selectAll();
    }

    @Override
    public void deleteById(String id) {
        savedQueryMapper.deleteById(id);
    }

    @Override
    public List<SavedQuery> findByDataSourceId(String dataSourceId) {
        return savedQueryMapper.selectByDataSourceIdOrderByUpdatedAtDesc(dataSourceId);
    }

    @Override
    public Optional<SavedQuery> findByName(String name) {
        return Optional.ofNullable(savedQueryMapper.selectByName(name));
    }

}