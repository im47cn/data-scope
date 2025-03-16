package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.metadata.TableRelationship;
import com.insightdata.domain.repository.TableRelationshipRepository;
import com.insightdata.infrastructure.persistence.entity.TableRelationshipEntity;

/**
 * 表关系仓库实现类
 */
@Repository
public class TableRelationshipRepositoryImpl implements TableRelationshipRepository {

    @Autowired
    private JpaTableRelationshipRepository jpaRepository;

    @Override
    public TableRelationship save(TableRelationship relationship) {
        TableRelationshipEntity entity = TableRelationshipEntity.fromDomain(relationship);
        TableRelationshipEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<TableRelationship> findById(Long id) {
        return jpaRepository.findById(id)
                .map(TableRelationshipEntity::toDomain);
    }

    @Override
    public List<TableRelationship> findByDataSourceId(Long dataSourceId) {
        return jpaRepository.findByDataSourceId(dataSourceId).stream()
                .map(TableRelationshipEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TableRelationship> findByDataSourceIdAndTable(Long dataSourceId, String tableName) {
        return jpaRepository.findByDataSourceIdAndTable(dataSourceId, tableName).stream()
                .map(TableRelationshipEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<TableRelationship> findByDataSourceIdAndTables(Long dataSourceId, String sourceTable, String targetTable) {
        return jpaRepository.findByDataSourceIdAndTables(dataSourceId, sourceTable, targetTable).stream()
                .map(TableRelationshipEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByDataSourceId(Long dataSourceId) {
        jpaRepository.deleteByDataSourceId(dataSourceId);
    }
}