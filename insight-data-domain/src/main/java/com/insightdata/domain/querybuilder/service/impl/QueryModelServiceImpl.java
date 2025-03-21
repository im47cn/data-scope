package com.insightdata.domain.querybuilder.service.impl;

import com.insightdata.domain.querybuilder.model.QueryModel;
import com.insightdata.domain.querybuilder.service.QueryModelService;
import com.insightdata.facade.querybuilder.QueryModelDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link QueryModelService} interface.
 */
@Service
public class QueryModelServiceImpl implements QueryModelService {

    @Override
    public QueryModel createModel(QueryModelDto dto) {
        // TODO: Implement
        return null;
    }

    @Override
    public QueryModel updateModel(String id, QueryModelDto dto) {
        // TODO: Implement
        return null;
    }

    @Override
    public Optional<QueryModel> getModel(String id) {
        // TODO: Implement
        return Optional.empty();
    }

    @Override
    public void deleteModel(String id) {
        // TODO: Implement

    }

    @Override
    public List<QueryModel> getAllModels() {
        // TODO: Implement
        return null;
    }

    @Override
    public List<QueryModel> searchModels(String keyword) {
        // TODO: Implement
        return null;
    }
}