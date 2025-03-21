package com.insightdata.facade.querybuilder;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * QueryBuilder REST API interface
 * Defines the REST endpoints for query model management
 */
@RequestMapping("/api/v1/querybuilder")
public interface QueryBuilderApi {

    /**
     * Create a new query model
     */
    @PostMapping("/models")
    QueryBuilderResponse<QueryModelDto> createQueryModel(@RequestBody QueryModelDto queryModel);

    /**
     * Get a query model by ID
     */
    @GetMapping("/models/{id}")
    QueryBuilderResponse<QueryModelDto> getQueryModel(@PathVariable("id") String id);

    /**
     * Update an existing query model
     */
    @PutMapping("/models/{id}")
    QueryBuilderResponse<QueryModelDto> updateQueryModel(@PathVariable("id") String id, @RequestBody QueryModelDto queryModel);

    /**
     * Delete a query model
     */
    @DeleteMapping("/models/{id}")
    QueryBuilderResponse<Void> deleteQueryModel(@PathVariable("id") String id);

    /**
     * List query models with pagination and filtering
     */
    @GetMapping("/models")
    QueryBuilderResponse<List<QueryModelDto>> listQueryModels(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "tags", required = false) List<String> tags);

    /**
     * Execute a query model with parameters
     */
    @PostMapping("/models/{id}/execute")
    QueryBuilderResponse<Map<String, Object>> executeQueryModel(
            @PathVariable("id") String id,
            @RequestBody Map<String, Object> parameters);

    /**
     * Save query model as template
     */
    @PostMapping("/models/{id}/template")
    QueryBuilderResponse<QueryModelDto> saveAsTemplate(
            @PathVariable("id") String id,
            @RequestParam("templateName") String templateName);

    /**
     * Create query model from template
     */
    @PostMapping("/templates/{templateId}/create")
    QueryBuilderResponse<QueryModelDto> createFromTemplate(
            @PathVariable("templateId") String templateId,
            @RequestBody Map<String, Object> parameters);

    /**
     * Get query model version history
     */
    @GetMapping("/models/{id}/versions")
    QueryBuilderResponse<List<QueryModelDto>> getModelVersions(@PathVariable("id") String id);

    /**
     * Revert to a specific version
     */
    @PostMapping("/models/{id}/versions/{versionId}/revert")
    QueryBuilderResponse<QueryModelDto> revertToVersion(
            @PathVariable("id") String id,
            @PathVariable("versionId") String versionId);

    /**
     * Validate query model
     */
    @PostMapping("/models/validate")
    QueryBuilderResponse<Map<String, Object>> validateQueryModel(@RequestBody QueryModelDto queryModel);
}