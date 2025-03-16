package com.insightdata.facade.rest;

import java.util.List;

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

import com.insightdata.domain.model.query.NLQueryRequest;
import com.insightdata.domain.model.query.QueryHistory;
import com.insightdata.domain.model.query.QueryResult;
import com.insightdata.domain.model.query.SavedQuery;
import com.insightdata.domain.service.NLQueryService;
import com.insightdata.facade.rest.dto.SaveQueryRequestDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * 自然语言查询控制器
 */
@RestController
@RequestMapping("/nl-queries")
@Slf4j
public class NLQueryController {
    
    private final NLQueryService nlQueryService;
    
    // 手动添加构造函数
    public NLQueryController(NLQueryService nlQueryService) {
        this.nlQueryService = nlQueryService;
    }
    
    /**
     * 执行自然语言查询
     *
     * @param request 查询请求
     * @return 查询结果
     */
    @PostMapping
    public ResponseEntity<QueryResult> executeQuery(@RequestBody NLQueryRequest request) {
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
    public ResponseEntity<Long> saveQuery(@RequestBody SaveQueryRequestDTO requestDTO) {
        Long id = nlQueryService.saveQuery(requestDTO.getName(), requestDTO.getRequest(), requestDTO.getResult());
        return ResponseEntity.ok(id);
    }
    
    /**
     * 获取保存的查询
     *
     * @param dataSourceId 数据源ID
     * @return 保存的查询列表
     */
    @GetMapping("/saved")
    public ResponseEntity<List<SavedQuery>> getSavedQueries(@RequestParam Long dataSourceId) {
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
        SavedQuery savedQuery = nlQueryService.getSavedQuery(id);
        if (savedQuery == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(savedQuery);
    }
    
    /**
     * 删除保存的查询
     *
     * @param id 查询ID
     * @return 无内容响应
     */
    @DeleteMapping("/saved/{id}")
    public ResponseEntity<Void> deleteSavedQuery(@PathVariable Long id) {
        nlQueryService.deleteSavedQuery(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * 更新保存的查询
     *
     * @param id 查询ID
     * @param name 查询名称
     * @param description 描述
     * @param isPublic 是否公开
     * @return 更新后的保存的查询
     */
    @PutMapping("/saved/{id}")
    public ResponseEntity<SavedQuery> updateSavedQuery(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false, defaultValue = "false") boolean isPublic) {
        SavedQuery savedQuery = nlQueryService.updateSavedQuery(id, name, description, isPublic);
        return ResponseEntity.ok(savedQuery);
    }
}
