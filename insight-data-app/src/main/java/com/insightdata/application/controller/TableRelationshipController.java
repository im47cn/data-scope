package com.insightdata.application.controller;

import java.util.List;

import com.insightdata.domain.metadata.model.TableRelationship;
import com.insightdata.domain.service.TableRelationshipService;
import com.insightdata.facade.metadata.TableRelationshipDTO;
import com.insightdata.infrastructure.persistence.mapper.TableRelationshipMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/table-relationships")
@RequiredArgsConstructor
public class TableRelationshipController {

    private static final Logger logger = LoggerFactory.getLogger(TableRelationshipController.class);

    private final TableRelationshipService tableRelationshipService;
    private final TableRelationshipMapper tableRelationshipMapper;

    @PostMapping
    public ResponseEntity<TableRelationshipDTO> createRelationship(
            @Valid @RequestBody TableRelationshipDTO relationshipDTO) {
        logger.info("Creating new table relationship: {}", relationshipDTO);
        TableRelationship relationship = tableRelationshipMapper.toEntity(relationshipDTO);
        TableRelationship createdRelationship = tableRelationshipService.createRelationship(relationship);
        return ResponseEntity.ok(tableRelationshipMapper.toDTO(createdRelationship));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableRelationshipDTO> updateRelationship(
            @PathVariable String id,
            @Valid @RequestBody TableRelationshipDTO relationshipDTO) {
        logger.info("Updating table relationship with ID: {}", id);
        TableRelationship relationship = tableRelationshipMapper.toEntity(relationshipDTO);
        relationship.setId(id); // Ensure ID is set from the path variable
        TableRelationship updatedRelationship = tableRelationshipService.updateRelationship(relationship);
        return ResponseEntity.ok(tableRelationshipMapper.toDTO(updatedRelationship));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable String id) {
        logger.info("Deleting table relationship with ID: {}", id);
        tableRelationshipService.deleteRelationship(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TableRelationshipDTO>> getAllRelationships(
            @RequestParam String dataSourceId) {
        logger.info("Getting all table relationships for data source ID: {}", dataSourceId);
        List<TableRelationship> relationships = tableRelationshipService.findByDataSourceId(dataSourceId);
        return ResponseEntity.ok(tableRelationshipMapper.toDTOList(relationships));
    }

    @GetMapping("/search")
    public ResponseEntity<List<TableRelationshipDTO>> searchRelationships(
        @RequestParam(required = false) String sourceTable,
        @RequestParam(required = false) String targetTable,
        @RequestParam(required = false) String relationType) {
        logger.info("Searching table relationships with sourceTable={}, targetTable={}, relationType={}",
                sourceTable, targetTable, relationType);
        List<TableRelationship> relationships = tableRelationshipService.searchRelationships(
                sourceTable, targetTable, relationType);
        return ResponseEntity.ok(tableRelationshipMapper.toDTOList(relationships));
    }

    @GetMapping("/suggest")
    public ResponseEntity<List<TableRelationshipDTO>> suggestRelationships(
            @RequestParam String dataSourceId,
            @RequestParam String tableName) {
        logger.info("Suggesting table relationships for data source ID: {} and table: {}", dataSourceId, tableName);
        List<TableRelationship> relationships = tableRelationshipService.suggestRelationships(dataSourceId, tableName);
        return ResponseEntity.ok(tableRelationshipMapper.toDTOList(relationships));
    }

    @PostMapping("/learn/metadata")
    public ResponseEntity<Void> learnFromMetadata(@RequestParam String dataSourceId) {
        logger.info("Learning table relationships from metadata for data source ID: {}", dataSourceId);
        try {
            tableRelationshipService.learnFromMetadata(dataSourceId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("从元数据学习表关系失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/learn/query-history")
    public ResponseEntity<Void> learnFromQueryHistory(@RequestParam String dataSourceId) {
        logger.info("Learning table relationships from query history for data source ID: {}", dataSourceId);
        try {
            tableRelationshipService.learnFromQueryHistory(dataSourceId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("从查询历史学习表关系失败", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/feedback")
    public ResponseEntity<Void> provideFeedback(
            @PathVariable String id,
            @RequestParam String sourceColumn,
            @RequestParam String targetColumn,
            @RequestParam String type)
    {
        logger.info("Received feedback for table relationship with ID: {}. SourceColumn={}, TargetColumn={}, Type={}",
                id, sourceColumn, targetColumn, type);
        tableRelationshipService.addUserFeedback(id, sourceColumn, targetColumn, type);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/adjust-weight")
    public ResponseEntity<Void> adjustWeight(
            @PathVariable String id,
            @RequestParam double weightDelta)
    {
        logger.info("Adjusting weight for table relationship with ID: {}. WeightDelta={}", id, weightDelta);
        tableRelationshipService.adjustConfidence(id, weightDelta);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/table/{tableName}")
    public ResponseEntity<List<TableRelationshipDTO>> getRelationshipsForTable(
            @PathVariable String tableName,
            @RequestParam(defaultValue = "0.5") double confidence)
    {
        logger.info("Getting table relationships for table: {}, with confidence: {}", tableName, confidence);
        List<TableRelationship> relationships = tableRelationshipService.findRelatedTables(tableName, confidence);
        return ResponseEntity.ok(tableRelationshipMapper.toDTOList(relationships));
    }
}