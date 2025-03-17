package com.infrastructure.persistence.repository;

import com.domain.model.lowcode.Component;
import com.domain.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisComponentRepository implements ComponentRepository {

    @Override
    public Component save(Component component) {
        return null;
    }

    @Override
    public Optional<Component> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<Component> findByPageId(String pageId) {
        return List.of();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public List<Component> findAll() {
        return List.of();
    }

    @Override
    public List<Component> findByType(String type) {
        return List.of();
    }

    @Override
    public void deleteByPageId(String pageId) {

    }
}
