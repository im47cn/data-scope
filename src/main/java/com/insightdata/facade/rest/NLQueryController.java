package com.insightdata.facade.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insightdata.facade.rest.dto.NLQueryRequestDTO;
import com.insightdata.facade.rest.dto.SavedQueryDTO;
import com.insightdata.facade.rest.dto.UpdateSavedQueryDTO;
import com.insightdata.domain.model.query.NLQueryRequest;
import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.model.query.QueryResult;
import com.insightdata.domain.model.query.SavedQuery;
import com.insightdata.domain.service.NLQueryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 自然语言查询控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/nl-query")
@RequiredArgsConstructor
public class NLQueryController {

    private final NLQueryService nlQueryService;

    /**
     * 执行自然语言查询
     *
     * @param requestDTO 查询请求DTO
     * @return 查询结果
     */
    @PostMapping("/execute")
    public ResponseEntity<QueryResult> executeQuery(@RequestBody NLQueryRequestDTO requestDTO) {
        log.info("执行自然语言查询: {}", requestDTO.getQuery());
        
        NLQueryRequest request = new NLQueryRequest();
        request.setDataSourceId(requestDTO.getDataSourceId());
        request.setQuery(requestDTO.getQuery());
        request.setParameters(requestDTO.getParameters());
        
        QueryResult result = nlQueryService.executeQuery(request);
        return ResponseEntity.ok(result);
    }

    /**
     * 获取查询历史
     *
     * @param dataSourceId 数据源ID
     * @return 查询历史列表
     */
    @GetMapping("/history")
    public ResponseEntity<List<QueryHistory>> getQueryHistory(@RequestParam Long dataSourceId) {
        log.info("获取查询历史: {}", dataSourceId);
        
        List<QueryHistory> history = nlQueryService.getQueryHistory(dataSourceId);
        return ResponseEntity.ok(history);
    }

    /**
     * 保存查询
     *
     * @param requestDTO 保存查询请求DTO
     * @return 保存的查询ID
     */
    @PostMapping("/save")
    public ResponseEntity<Long> saveQuery(@RequestBody SavedQueryDTO requestDTO) {
        log.info("保存查询: {}", requestDTO.getName());
        
        NLQueryRequest request = new NLQueryRequest();
        request.setDataSourceId(requestDTO.getDataSourceId());
        request.setQuery(requestDTO.getQuery());
        request.setParameters(requestDTO.getParameters());
        
        QueryResult result = new QueryResult();
        result.setSql(requestDTO.getSql());
        
        Long savedQueryId = nlQueryService.saveQuery(requestDTO.getName(), request, result);
        return ResponseEntity.ok(savedQueryId);
    }

    /**
     * 获取保存的查询列表
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    @GetMapping("/saved")
    public ResponseEntity<List<SavedQuery>> getSavedQueries(@RequestParam Long dataSourceId) {
        log.info("获取保存的查询: {}", dataSourceId);
        
        List<SavedQuery> savedQueries = nlQueryService.getSavedQueries(dataSourceId);
        return ResponseEntity.ok(savedQueries);
    }

    /**
     * 获取保存的查询
     *
     * @param id 查询ID
     * @return 保存的查询
     */
    @GetMapping("/saved/{id}")
    public ResponseEntity<SavedQuery> getSavedQuery(@PathVariable Long id) {
        log.info("获取保存的查询: {}", id);
        
        SavedQuery savedQuery = nlQueryService.getSavedQuery(id);
        return ResponseEntity.ok(savedQuery);
    }

    /**
     * 删除保存的查询
     *
     * @param id 查询ID
     * @return 操作结果
     */
    @DeleteMapping("/saved/{id}")
    public ResponseEntity<Void> deleteSavedQuery(@PathVariable Long id) {
        log.info("删除保存的查询: {}", id);
        
        nlQueryService.deleteSavedQuery(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新保存的查询
     *
     * @param id 查询ID
     * @param requestDTO 更新请求DTO
     * @return 更新后的保存的查询
     */
    @PutMapping("/saved/{id}")
    public ResponseEntity<SavedQuery> updateSavedQuery(
            @PathVariable Long id,
            @RequestBody UpdateSavedQueryDTO requestDTO) {
        log.info("更新保存的查询: {}", id);
        
        SavedQuery savedQuery = nlQueryService.updateSavedQuery(
                id,
                requestDTO.getName(),
                requestDTO.getDescription(),
                requestDTO.isPublic());
        
        return ResponseEntity.ok(savedQuery);
    }

    /**
     * 执行保存的查询
     *
     * @param id 查询ID
     * @param parameters 查询参数
     * @return 查询结果
     */
    @PostMapping("/saved/{id}/execute")
    public ResponseEntity<QueryResult> executeSavedQuery(
            @PathVariable Long id,
            @RequestBody Map<String, Object> parameters) {
        log.info("执行保存的查询: {}", id);
        
        QueryResult result = nlQueryService.executeQueryById(id, parameters);
        return ResponseEntity.ok(result);
    }
}