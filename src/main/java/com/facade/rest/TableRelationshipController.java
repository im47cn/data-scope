package com.facade.rest;

import com.domain.model.metadata.SchemaInfo;
import com.domain.model.metadata.TableRelationship;
import com.domain.model.query.QueryHistory;
import com.domain.repository.QueryHistoryRepository;
import com.domain.service.DataSourceService;
import com.domain.service.TableRelationshipService;
import com.facade.rest.dto.TableRelationshipDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 表关系REST控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/table-relationships")
public class TableRelationshipController {

    @Autowired
    private TableRelationshipService tableRelationshipService;
    
    @Autowired
    private DataSourceService dataSourceService;
    
    @Autowired
    private QueryHistoryRepository queryHistoryRepository;
    
    /**
     * 获取数据源的所有表关系
     */
    @GetMapping
    public ResponseEntity<List<TableRelationshipDTO>> getAllTableRelationships(
            @RequestParam Long dataSourceId) {
        List<TableRelationship> relationships = tableRelationshipService.getAllTableRelationships(dataSourceId);
        List<TableRelationshipDTO> dtos = relationships.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * 获取指定表的所有关系
     */
    @GetMapping("/tables/{tableName}")
    public ResponseEntity<List<TableRelationshipDTO>> getTableRelationships(
            @RequestParam Long dataSourceId,
            @PathVariable String tableName) {
        List<TableRelationship> relationships = tableRelationshipService.getTableRelationships(dataSourceId, tableName);
        List<TableRelationshipDTO> dtos = relationships.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * 获取指定ID的表关系
     */
    @GetMapping("/{id}")
    public ResponseEntity<TableRelationshipDTO> getTableRelationship(@PathVariable Long id) {
        TableRelationship relationship = tableRelationshipService.findById(id);
        if (relationship == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(toDTO(relationship));
    }
    
    /**
     * 创建表关系
     */
    @PostMapping
    public ResponseEntity<TableRelationshipDTO> createTableRelationship(
            @RequestBody TableRelationshipDTO dto) {
        TableRelationship relationship = fromDTO(dto);
        relationship.setSource(TableRelationship.RelationshipSource.USER_FEEDBACK);
        TableRelationship savedRelationship = tableRelationshipService.learnFromUserFeedback(
                relationship.getDataSourceId(),
                relationship.getSourceTable(),
                relationship.getSourceColumn(),
                relationship.getTargetTable(),
                relationship.getTargetColumn(),
                relationship.getType());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(savedRelationship));
    }
    
    /**
     * 更新表关系
     */
    @PutMapping("/{id}")
    public ResponseEntity<TableRelationshipDTO> updateTableRelationship(
            @PathVariable Long id,
            @RequestBody TableRelationshipDTO dto) {
        // 检查是否存在
        TableRelationship existingRelationship = tableRelationshipService.findById(id);
        if (existingRelationship == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 更新关系
        TableRelationship relationship = fromDTO(dto);
        relationship.setId(id);
        relationship.setSource(TableRelationship.RelationshipSource.USER_FEEDBACK);
        TableRelationship updatedRelationship = tableRelationshipService.saveTableRelationship(relationship);
        return ResponseEntity.ok(toDTO(updatedRelationship));
    }
    
    /**
     * 删除表关系
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTableRelationship(@PathVariable Long id) {
        // 检查是否存在
        TableRelationship existingRelationship = tableRelationshipService.findById(id);
        if (existingRelationship == null) {
            return ResponseEntity.notFound().build();
        }
        
        // 删除关系
        tableRelationshipService.deleteTableRelationship(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 从元数据学习表关系
     */
    @PostMapping("/learn-from-metadata")
    public ResponseEntity<List<TableRelationshipDTO>> learnFromMetadata(
            @RequestParam Long dataSourceId) {
        try {
            // 获取数据源的模式信息
            SchemaInfo schemaInfo = dataSourceService.getSchemaInfo(dataSourceId);
            
            // 从元数据学习表关系
            List<TableRelationship> relationships = tableRelationshipService.learnFromMetadata(dataSourceId, schemaInfo);
            
            // 转换为DTO
            List<TableRelationshipDTO> dtos = relationships.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("从元数据学习表关系失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 从查询历史学习表关系
     */
    @PostMapping("/learn-from-query-history")
    public ResponseEntity<List<TableRelationshipDTO>> learnFromQueryHistory(
            @RequestParam Long dataSourceId) {
        try {
            // 获取数据源的查询历史
            List<QueryHistory> queryHistories = queryHistoryRepository.findByDataSourceIdOrderByCreatedAtDesc(dataSourceId);
            
            // 从查询历史学习表关系
            List<TableRelationship> relationships = tableRelationshipService.learnFromQueryHistory(dataSourceId, queryHistories);
            
            // 转换为DTO
            List<TableRelationshipDTO> dtos = relationships.stream()
                    .map(this::toDTO)
                    .collect(Collectors.toList());
            
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            log.error("从查询历史学习表关系失败", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    /**
     * 推荐表关系
     */
    @GetMapping("/recommendations")
    public ResponseEntity<List<TableRelationshipDTO>> recommendRelationships(
            @RequestParam Long dataSourceId,
            @RequestParam String tableName,
            @RequestParam(defaultValue = "5") int limit) {
        List<TableRelationship> relationships = tableRelationshipService.recommendRelationships(dataSourceId, tableName, limit);
        List<TableRelationshipDTO> dtos = relationships.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
    
    /**
     * 将领域模型转换为DTO
     */
    private TableRelationshipDTO toDTO(TableRelationship relationship) {
        TableRelationshipDTO dto = new TableRelationshipDTO();
        dto.setId(relationship.getId());
        dto.setDataSourceId(relationship.getDataSourceId());
        dto.setSourceTable(relationship.getSourceTable());
        dto.setSourceColumn(relationship.getSourceColumn());
        dto.setTargetTable(relationship.getTargetTable());
        dto.setTargetColumn(relationship.getTargetColumn());
        dto.setType(relationship.getType().name());
        dto.setSource(relationship.getSource().name());
        dto.setWeight(relationship.getWeight());
        dto.setFrequency(relationship.getFrequency());
        dto.setCreatedAt(relationship.getCreatedAt());
        dto.setUpdatedAt(relationship.getUpdatedAt());
        return dto;
    }
    
    /**
     * 将DTO转换为领域模型
     */
    private TableRelationship fromDTO(TableRelationshipDTO dto) {
        return TableRelationship.builder()
                .id(dto.getId())
                .dataSourceId(dto.getDataSourceId())
                .sourceTable(dto.getSourceTable())
                .sourceColumn(dto.getSourceColumn())
                .targetTable(dto.getTargetTable())
                .targetColumn(dto.getTargetColumn())
                .type(TableRelationship.RelationshipType.valueOf(dto.getType()))
                .source(dto.getSource() != null ? 
                        TableRelationship.RelationshipSource.valueOf(dto.getSource()) : 
                        TableRelationship.RelationshipSource.USER_FEEDBACK)
                .weight(dto.getWeight() != null ? dto.getWeight() : 1.0)
                .frequency(dto.getFrequency() != null ? dto.getFrequency() : 1)
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .build();
    }
}