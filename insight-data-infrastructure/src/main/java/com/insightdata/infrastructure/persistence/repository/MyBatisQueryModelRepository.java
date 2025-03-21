package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.domain.querybuilder.model.QueryModel;
import com.insightdata.domain.querybuilder.repository.QueryModelRepository;
import com.insightdata.infrastructure.persistence.entity.QueryModelEntity;
import com.insightdata.infrastructure.persistence.mapper.QueryModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MyBatisQueryModelRepository implements QueryModelRepository {

    @Autowired
    private QueryModelMapper queryModelMapper;

    @Override
    public QueryModel save(QueryModel queryModel) {
        QueryModelEntity entity = toEntity(queryModel);
        if (entity.getId() == null) {
            queryModelMapper.insert(entity);
        } else {
            queryModelMapper.update(entity);
        }
        return toModel(entity);
    }

    @Override
    public Optional<QueryModel> findById(String id) {
        QueryModelEntity entity = queryModelMapper.findById(id);
        return Optional.ofNullable(toModel(entity));
    }

    @Override
    public List<QueryModel> findAll() {
        return queryModelMapper.findAll().stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        queryModelMapper.deleteById(id);
    }

    @Override
    public List<QueryModel> findByNameContaining(String name) {
        return queryModelMapper.findByNameContaining(name).stream().map(this::toModel).collect(Collectors.toList());
    }

    @Override
    public List<QueryModel> findAll(int page, int size) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public List<QueryModel> saveAll(List<QueryModel> models) {
        return List.of();
    }

    @Override
    public void deleteAllById(List<String> ids) {

    }

    @Override
    public List<QueryModel> findByCreateTimeBetween(long startTime, long endTime) {
        return List.of();
    }

    @Override
    public List<QueryModel> findByUpdateTimeBetween(long startTime, long endTime) {
        return List.of();
    }

    @Override
    public List<QueryModel> findByCreator(String creator) {
        return List.of();
    }

    @Override
    public List<QueryModel> findByTags(List<String> tags) {
        return List.of();
    }

    @Override
    public List<QueryModel> findRecentlyUsed(int limit) {
        return List.of();
    }

    @Override
    public List<QueryModel> findMostUsed(int limit) {
        return List.of();
    }

    @Override
    public void incrementUsageCount(String id) {

    }

    @Override
    public void updateLastUsedTime(String id, long timestamp) {

    }

    @Override
    public boolean existsById(String id) {
        return queryModelMapper.existsById(id);
    }

    private QueryModelEntity toEntity(QueryModel model) {
       if (model == null) {
           return null;
       }
        QueryModelEntity entity = new QueryModelEntity();
        entity.setId(model.getId());
        entity.setName(model.getName());
        entity.setDescription("");
//        entity.setDataSources(model.getDataSources() != null ? model.getDataSources().toString() : null); // Placeholder: Convert to JSON string
        entity.setTables(model.getTables() != null ? model.getTables().toString() : null); // Placeholder: Convert to JSON string
        entity.setFields(model.getFields() != null ? model.getFields().toString() : null); // Placeholder: Convert to JSON string
        entity.setJoins(model.getJoins() != null ? model.getJoins().toString() : null); // Placeholder: Convert to JSON string
//        entity.setRootFilter(model.getRootFilter() != null ? model.getRootFilter().toString() : null); // Placeholder: Convert to JSON string
        entity.setGroupBy(model.getGroupBy() != null ? model.getGroupBy().toString() : null); // Placeholder: Convert to JSON string
        entity.setOrderBy(model.getOrderBy() != null ? model.getOrderBy().toString() : null); // Placeholder: Convert to JSON string
        entity.setParameters(model.getParameters() != null ? model.getParameters().toString() : null); // Placeholder: Convert to JSON string
//        entity.setOptions(model.getOptions() != null ? model.getOptions().toString() : null); // Placeholder: Convert to JSON string
//        entity.setCreatedAt(model.getCreatedAt());
//        entity.setUpdatedAt(model.getUpdatedAt());
//        entity.setCreatedBy(model.getCreatedBy());
//        entity.setUpdatedBy(model.getUpdatedBy());
//        entity.setPublic(model.isPublic());
//        entity.setTags(model.getTags() != null ? model.getTags().toString() : null); // Placeholder: Convert to JSON string
//        entity.setStatus(model.getStatus());
        return entity;
    }

    private QueryModel toModel(QueryModelEntity entity){
        if (entity == null) {
            return null;
        }
        QueryModel model = new QueryModel();
        model.setId(entity.getId());
        model.setName(entity.getName());

        // TODO: Convert JSON strings back to complex objects using a JSON library like Jackson
        // model.setDataSources(convertFromJson(entity.getDataSources(), List.class));
        // ... (Repeat for other fields)
        return model;
    }
}