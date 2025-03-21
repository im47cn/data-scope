package com.insightdata.domain.querybuilder.service;

import com.insightdata.domain.querybuilder.model.QueryModel;
import com.insightdata.facade.querybuilder.QueryModelDto;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for managing query models.
 */
public interface QueryModelService {

    QueryModel createModel(QueryModelDto dto);

    QueryModel updateModel(String id, QueryModelDto dto);

    Optional<QueryModel> getModel(String id);

    void deleteModel(String id);

    List<QueryModel> getAllModels();

    List<QueryModel> searchModels(String keyword);
}