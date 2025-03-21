package com.insightdata.domain.querybuilder.repository;

import java.util.List;
import java.util.Optional;

import com.insightdata.domain.querybuilder.model.QueryModel;

/**
 * Repository interface for managing {@link QueryModel} entities.
 */
public interface QueryModelRepository {

    QueryModel save(QueryModel queryModel);

    Optional<QueryModel> findById(String id);

    List<QueryModel> findAll();

    void deleteById(String id);

    List<QueryModel> findByNameContaining(String name);

    boolean existsByName(String name);
}