package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public List<TableRelationship> saveAll(List<TableRelationship> relationships) {
        List<TableRelationshipEntity> entities = relationships.stream()
                .map(TableRelationshipEntity::fromDomain)
                .collect(Collectors.toList());
        
        List<TableRelationshipEntity> savedEntities = jpaRepository.saveAll(entities);
        
        return savedEntities.stream()
                .map(TableRelationshipEntity::toDomain)
                .collect(Collectors.toList());
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
    
    @Override
    @Transactional
    public void deleteByDataSourceIdAndSource(Long dataSourceId, TableRelationship.RelationshipSource source) {
        jpaRepository.deleteByDataSourceIdAndSource(dataSourceId, source);
    }
    
    @Override
    @Transactional
    public void incrementFrequency(Long id) {
        // 直接使用 JPQL 更新频率，避免手动获取和设置频率
        jpaRepository.incrementFrequencyById(id);
    }
}