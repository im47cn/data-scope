package com.infrastructure.persistence.repository;

import com.domain.model.metadata.SchemaInfo;
import com.domain.repository.SchemaInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisSchemaInfoRepository implements SchemaInfoRepository {

    @Override
    public SchemaInfo save(SchemaInfo schemaInfo) {
        return null;
    }

    @Override
    public Optional<SchemaInfo> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<SchemaInfo> findByDataSourceId(Long dataSourceId) {
        return List.of();
    }

    @Override
    public void delete(SchemaInfo schemaInfo) {

    }
}
