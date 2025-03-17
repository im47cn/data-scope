package com.facade.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.domain.service.NLQueryService;
import com.facade.dto.NLQueryRequest;
import com.facade.dto.NLQueryResponse;
import com.facade.dto.QueryHistoryDTO;
import com.facade.dto.SavedQueryDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 自然语言查询控制器
 */
@RestController
@RequestMapping("/api/v1/nl-query")
@Slf4j
public class NLQueryController {
    
    @Autowired
    private NLQueryService nlQueryService;
    
    /**
     * 执行自然语言查询
     * 
     * @param request 查询请求
     * @return 查询结果
     */
    @PostMapping("/execute")
    public ResponseEntity<NLQueryResponse> executeQuery(@RequestBody NLQueryRequest request) {
        log.info("接收到自然语言查询请求: {}", request.getQuery());
        NLQueryResponse response = nlQueryService.executeQuery(request);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 获取查询历史列表
     * 
     * @param dataSourceId 数据源ID
     * @param page 页码
     * @param size 每页大小
     * @return 查询历史列表
     */
    @GetMapping("/history")
    public ResponseEntity<List<QueryHistoryDTO>> getQueryHistory(
            @RequestParam String dataSourceId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<QueryHistoryDTO> history = nlQueryService.getQueryHistory(dataSourceId, page, size);
        return ResponseEntity.ok(history);
    }
    
    /**
     * 获取查询历史详情
     * 
     * @param id 查询历史ID
     * @return 查询历史详情
     */
    @GetMapping("/history/{id}")
    public ResponseEntity<QueryHistoryDTO> getQueryHistoryById(@PathVariable String id) {
        QueryHistoryDTO history = nlQueryService.getQueryHistoryById(id);
        if (history == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }
    
    /**
     * 重新执行历史查询
     * 
     * @param id 查询历史ID
     * @return 查询结果
     */
    @PostMapping("/rerun/{id}")
    public ResponseEntity<NLQueryResponse> rerunQuery(@PathVariable String id) {
        NLQueryResponse response = nlQueryService.rerunQuery(id);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 保存查询
     * 
     * @param id 查询历史ID
     * @param savedQuery 保存查询信息
     * @return 保存的查询ID
     */
    @PostMapping("/save/{id}")
    public ResponseEntity<String> saveQuery(
            @PathVariable String id,
            @RequestBody SavedQueryDTO savedQuery) {
        String savedId = nlQueryService.saveQuery(id, savedQuery);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedId);
    }
    
    /**
     * 获取已保存的查询列表
     * 
     * @param dataSourceId 数据源ID
     * @return 已保存的查询列表
     */
    @GetMapping("/saved")
    public ResponseEntity<List<SavedQueryDTO>> getSavedQueries(@RequestParam String dataSourceId) {
        List<SavedQueryDTO> queries = nlQueryService.getSavedQueries(dataSourceId);
        return ResponseEntity.ok(queries);
    }
    
    /**
     * 获取已保存的查询详情
     * 
     * @param id 已保存的查询ID
     * @return 已保存的查询详情
     */
    @GetMapping("/saved/{id}")
    public ResponseEntity<SavedQueryDTO> getSavedQuery(@PathVariable String id) {
        SavedQueryDTO query = nlQueryService.getSavedQuery(id);
        if (query == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(query);
    }
    
    /**
     * 更新已保存的查询
     * 
     * @param id 已保存的查询ID
     * @param savedQuery 更新信息
     * @return 更新后的查询信息
     */
    @PutMapping("/saved/{id}")
    public ResponseEntity<SavedQueryDTO> updateSavedQuery(
            @PathVariable String id,
            @RequestBody SavedQueryDTO savedQuery) {
        SavedQueryDTO updated = nlQueryService.updateSavedQuery(id, savedQuery);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * 删除已保存的查询
     * 
     * @param id 已保存的查询ID
     * @return 操作结果
     */
    @DeleteMapping("/saved/{id}")
    public ResponseEntity<Void> deleteSavedQuery(@PathVariable String id) {
        nlQueryService.deleteSavedQuery(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 执行已保存的查询
     * 
     * @param id 已保存的查询ID
     * @param parameters 查询参数
     * @return 查询结果
     */
    @PostMapping("/saved/{id}/execute")
    public ResponseEntity<NLQueryResponse> executeSavedQuery(
            @PathVariable String id,
            @RequestBody(required = false) Object parameters) {
        NLQueryResponse response = nlQueryService.executeSavedQuery(id, parameters);
        return ResponseEntity.ok(response);
    }
    
    /**
     * 解释SQL语句
     * 
     * @param sql SQL语句
     * @return SQL解释
     */
    @PostMapping("/explain")
    public ResponseEntity<String> explainSql(@RequestBody String sql) {
        String explanation = nlQueryService.explainSql(sql);
        return ResponseEntity.ok(explanation);
    }
}