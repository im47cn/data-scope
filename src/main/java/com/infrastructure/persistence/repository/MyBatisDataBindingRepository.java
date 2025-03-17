package com.infrastructure.persistence.repository;

import com.domain.model.lowcode.DataBinding;
import com.domain.repository.DataBindingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisDataBindingRepository implements DataBindingRepository {
    @Override
    public DataBinding save(DataBinding dataBinding) {
        return null;
    }

    @Override
    public Optional<DataBinding> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<DataBinding> findByComponentId(String componentId) {
        return List.of();
    }

    @Override
    public List<DataBinding> findByQueryId(String queryId) {
        return List.of();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<DataBinding> findAll() {
        return List.of();
    }

    @Override
    public void deleteByComponentId(String componentId) {

    }

    @Override
    public void deleteByQueryId(String queryId) {

    }
}
