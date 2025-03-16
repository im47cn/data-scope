package com.insightdata.infrastructure.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.metadata.TableRelationship;
import com.insightdata.domain.repository.TableRelationshipRepository;
import com.insightdata.infrastructure.persistence.converter.TableRelationshipConverter;
import com.insightdata.infrastructure.persistence.mapper.TableRelationshipMapper;

@Repository
public class TableRelationshipRepositoryImpl implements TableRelationshipRepository {
    
    private final TableRelationshipMapper tableRelationshipMapper;
    private final TableRelationshipConverter converter;
    
    public TableRelationshipRepositoryImpl(TableRelationshipMapper tableRelationshipMapper,
                                         TableRelationshipConverter converter) {
        this.tableRelationshipMapper = tableRelationshipMapper;
        this.converter = converter;
    }
    
    @Override
    public TableRelationship save(TableRelationship relationship) {
        var entity = converter.toEntity(relationship);
        LocalDateTime now = LocalDateTime.now();
        
        if (entity.getId() == null) {
            entity.setCreatedAt(now);
            entity.setUpdatedAt(now);
            tableRelationshipMapper.insert(entity);
        } else {
            entity.setUpdatedAt(now);
            tableRelationshipMapper.update(entity);
        }
        
        return converter.toDomain(entity);
    }
    
    @Override
    public List<TableRelationship> saveAll(List<TableRelationship> relationships) {
        return relationships.stream()
                .map(this::save)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<TableRelationship> findById(Long id) {
        return Optional.ofNullable(tableRelationshipMapper.selectById(id))
                .map(converter::toDomain);
    }
    
    @Override
    public List<TableRelationship> findByDataSourceId(Long dataSourceId) {
        return tableRelationshipMapper.selectByDataSourceIdAndTableName(dataSourceId, null)
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TableRelationship> findByDataSourceIdAndTable(Long dataSourceId, String tableName) {
        return tableRelationshipMapper.selectByDataSourceIdAndTableName(dataSourceId, tableName)
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TableRelationship> findByDataSourceIdAndTables(Long dataSourceId, String sourceTable, String targetTable) {
        return tableRelationshipMapper.selectByDataSourceIdAndTableName(dataSourceId, sourceTable)
                .stream()
                .map(converter::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        tableRelationshipMapper.deleteById(id);
    }
    
    @Override
    public void deleteByDataSourceId(Long dataSourceId) {
        tableRelationshipMapper.deleteByDataSourceIdAndSource(dataSourceId, null);
    }
    
    @Override
    public void deleteByDataSourceIdAndSource(Long dataSourceId, TableRelationship.RelationshipSource source) {
        tableRelationshipMapper.deleteByDataSourceIdAndSource(dataSourceId, source.name());
    }
    
    @Override
    public void incrementFrequency(Long id) {
        tableRelationshipMapper.incrementFrequency(id);
    }
}