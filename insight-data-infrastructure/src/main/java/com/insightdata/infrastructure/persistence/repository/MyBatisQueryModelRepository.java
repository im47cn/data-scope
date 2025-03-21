package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.insightdata.domain.querybuilder.model.QueryModel;
import com.insightdata.domain.querybuilder.repository.QueryModelRepository;
import com.insightdata.infrastructure.persistence.mapper.QueryModelMapper;

@Repository
public class MyBatisQueryModelRepository implements QueryModelRepository {

    @Autowired
    private QueryModelMapper queryModelMapper;

    @Override
    public QueryModel save(QueryModel queryModel) {
        // TODO: Implement
        return null;
    }

    @Override
    public Optional<QueryModel> findById(String id) {
        // TODO: Implement
        return Optional.empty();
    }

    @Override
    public List<QueryModel> findAll() {
        // TODO: Implement
        return null;
    }

    @Override
    public void deleteById(String id) {
        // TODO: Implement

    }

    @Override
    public List<QueryModel> findByNameContaining(String name) {
        // TODO: Implement
        return null;
    }

    @Override
    public boolean existsByName(String name) {
        // TODO: Implement
        return false;
    }

    // TODO: Add methods to convert between QueryModel and QueryModelEntity
}