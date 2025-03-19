package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.domain.lowcode.model.Page;
import com.insightdata.domain.lowcode.repository.PageRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisPageRepository implements PageRepository {
    @Override
    public Page save(Page page) {
        return null;
    }

    @Override
    public Optional<Page> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Page> findByAppId(String appId) {
        return List.of();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<Page> findAll() {
        return List.of();
    }
}
