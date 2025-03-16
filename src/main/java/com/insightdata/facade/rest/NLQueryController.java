package com.insightdata.facade.rest;

import com.insightdata.domain.model.SavedQuery;
import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.service.NLQueryService;
import com.insightdata.facade.rest.dto.NLQueryRequestDTO;
import com.insightdata.facade.rest.dto.SavedQueryDTO;
import com.insightdata.facade.rest.dto.UpdateSavedQueryDTO;
import com.insightdata.nlquery.NLQueryRequest;
import com.insightdata.nlquery.converter.SqlConversionResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 自然语言查询控制器
 */
@Slf4j
@RestController
@RequestMapping("/nl-query")
@RequiredArgsConstructor
@Tag(name = "自然语言查询", description = "自然语言查询相关接口")
public class NLQueryController {
    
    private final NLQueryService nlQueryService;
    
    @PostMapping("/execute")
    @Operation(summary = "执行查询", description = "执行自然语言查询")
    public ResponseEntity<?> executeQuery(@Valid @RequestBody NLQueryRequestDTO requestDTO) {
        log.info("执行自然语言查询: {}", requestDTO.getQuery());
        
        // 转换请求
        NLQueryRequest request = new NLQueryRequest();
        request.setDataSourceId(requestDTO.getDataSourceId());
        request.setQuery(requestDTO.getQuery());
        request.setParameters(requestDTO.getParameters());
        
        // 执行查询
        return ResponseEntity.ok(nlQueryService.executeQuery(request));
    }
    
    @GetMapping("/history/{dataSourceId}")
    @Operation(summary = "查询历史", description = "获取查询历史")
    public ResponseEntity<List<QueryHistory>> getQueryHistory(
            @Parameter(description = "数据源ID") @PathVariable Long dataSourceId) {
        log.info("获取查询历史: {}", dataSourceId);
        return ResponseEntity.ok(nlQueryService.getQueryHistory(dataSourceId));
    }
    
    @PostMapping("/save")
    @Operation(summary = "保存查询", description = "保存自然语言查询")
    public ResponseEntity<Long> saveQuery(@Valid @RequestBody SavedQueryDTO requestDTO) {
        log.info("保存查询: {}", requestDTO.getName());
        
        // 转换请求
        NLQueryRequest request = new NLQueryRequest();
        request.setDataSourceId(requestDTO.getDataSourceId());
        request.setQuery(requestDTO.getQuery());
        request.setParameters(requestDTO.getParameters());
        
        // 转换结果
        SqlConversionResult result = new SqlConversionResult();
        result.setSql(requestDTO.getSql());
        
        // 保存查询
        Long savedQueryId = nlQueryService.saveQuery(requestDTO.getName(), request, result);
        
        return ResponseEntity.ok(savedQueryId);
    }
    
    @GetMapping("/saved/{dataSourceId}")
    @Operation(summary = "保存的查询", description = "获取保存的查询列表")
    public ResponseEntity<List<SavedQuery>> getSavedQueries(
            @Parameter(description = "数据源ID") @PathVariable Long dataSourceId) {
        log.info("获取保存的查询: {}", dataSourceId);
        return ResponseEntity.ok(nlQueryService.getSavedQueries(dataSourceId));
    }
    
    @GetMapping("/saved/detail/{id}")
    @Operation(summary = "查询详情", description = "获取保存的查询详情")
    public ResponseEntity<SavedQuery> getSavedQuery(
            @Parameter(description = "查询ID") @PathVariable Long id) {
        log.info("获取保存的查询: {}", id);
        return ResponseEntity.ok(nlQueryService.getSavedQuery(id));
    }
    
    @DeleteMapping("/saved/{id}")
    @Operation(summary = "删除查询", description = "删除保存的查询")
    public ResponseEntity<Void> deleteSavedQuery(
            @Parameter(description = "查询ID") @PathVariable Long id) {
        log.info("删除保存的查询: {}", id);
        nlQueryService.deleteSavedQuery(id);
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/saved/{id}")
    @Operation(summary = "更新查询", description = "更新保存的查询")
    public ResponseEntity<SavedQuery> updateSavedQuery(
            @Parameter(description = "查询ID") @PathVariable Long id,
            @Valid @RequestBody UpdateSavedQueryDTO requestDTO) {
        log.info("更新保存的查询: {}", id);
        
        SavedQuery savedQuery = nlQueryService.updateSavedQuery(
                id,
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.isPublic());
        
        return ResponseEntity.ok(savedQuery);
    }
    
    @PostMapping("/saved/execute/{id}")
    @Operation(summary = "执行保存的查询", description = "执行保存的查询")
    public ResponseEntity<?> executeSavedQuery(
            @Parameter(description = "查询ID") @PathVariable Long id) {
        log.info("执行保存的查询: {}", id);
        return ResponseEntity.ok(nlQueryService.executeSavedQuery(id));
    }
}