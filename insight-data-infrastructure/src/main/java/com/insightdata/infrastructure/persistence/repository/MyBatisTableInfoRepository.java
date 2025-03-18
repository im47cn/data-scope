package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.domain.metadata.model.TableInfo;
import com.insightdata.domain.repository.TableInfoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MyBatisTableInfoRepository implements TableInfoRepository {

    @Override
    public TableInfo save(TableInfo tableInfo) {
        return null;
    }

    @Override
    public Optional<TableInfo> findById(String id) {
        return Optional.empty();
    }

    @Override
    public List<TableInfo> findByDataSourceId(Long dataSourceId) {
        return List.of();
    }

    @Override
    public List<TableInfo> findByDataSourceIdAndSchemaName(Long dataSourceId, String schemaName) {
        return List.of();
    }

    @Override
    public void delete(TableInfo tableInfo) {

    }
}
