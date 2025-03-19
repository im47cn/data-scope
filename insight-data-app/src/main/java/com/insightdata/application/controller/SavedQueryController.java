package com.insightdata.application.controller;

import com.insightdata.application.convertor.SavedQueryConvertor;
import com.insightdata.application.service.impl.SavedQueryService;
import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.facade.query.SavedQueryDTO;
import com.insightdata.facade.query.UpdateSavedQueryDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/query/templates")
@RequiredArgsConstructor
public class SavedQueryController {

    private final SavedQueryService savedQueryService;
    private final SavedQueryConvertor savedQueryMapper;

    /**
     * 创建查询模板
     */
    @PostMapping
    public ResponseEntity<SavedQueryDTO> createTemplate(
            @Valid @RequestBody SavedQueryDTO templateDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        SavedQuery template = savedQueryMapper.toEntity(templateDTO);
        SavedQuery created = savedQueryService.createTemplate(template, userDetails.getUsername());
        return ResponseEntity.ok(savedQueryMapper.toDTO(created));
    }

    /**
     * 更新查询模板
     */
    @PutMapping("/{id}")
    public ResponseEntity<SavedQueryDTO> updateTemplate(
            @PathVariable String id,
            @Valid @RequestBody UpdateSavedQueryDTO templateDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        SavedQuery template = savedQueryMapper.toEntity(templateDTO);
        SavedQuery updated = savedQueryService.updateTemplate(id, template, userDetails.getUsername());
        return ResponseEntity.ok(savedQueryMapper.toDTO(updated));
    }

    /**
     * 删除查询模板
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        savedQueryService.deleteTemplate(id, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    /**
     * 查找数据源的所有模板
     */
    @GetMapping("/{dataSourceId}")
    public ResponseEntity<List<SavedQueryDTO>> findTemplates(
            @PathVariable String dataSourceId,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        List<SavedQuery> templates = savedQueryService.findTemplates(dataSourceId, userDetails.getUsername());
        return ResponseEntity.ok(savedQueryMapper.toDTOList(templates));
    }

    /**
     * 按标签查找模板
     */
    @GetMapping
    public ResponseEntity<List<SavedQueryDTO>> findTemplatesByTags(
            @RequestParam List<String> tags
    ) {
        List<SavedQuery> templates = savedQueryService.findTemplatesByTags(tags);
        return ResponseEntity.ok(savedQueryMapper.toDTOList(templates));
    }

    /**
     * 搜索模板
     */
    @GetMapping("/search")
    public ResponseEntity<List<SavedQueryDTO>> searchTemplates(
            @RequestParam String keyword
    ) {
        List<SavedQuery> templates = savedQueryService.searchTemplates(keyword);
        return ResponseEntity.ok(savedQueryMapper.toDTOList(templates));
    }

    /**
     * 记录模板使用
     */
    @PostMapping("/{id}/usage")
    public ResponseEntity<Void> recordTemplateUsage(
            @PathVariable String id,
            @RequestParam long executionTime,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (savedQueryService.hasAccess(id, userDetails.getUsername())) {
            savedQueryService.useTemplate(id, executionTime);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 获取单个模板
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<SavedQueryDTO> getTemplate(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        if (savedQueryService.hasAccess(id, userDetails.getUsername())) {
            SavedQuery template = savedQueryService.findTemplates(id, userDetails.getUsername())
                    .stream()
                    .findFirst()
                    .orElse(null);
            return template != null ? 
                ResponseEntity.ok(savedQueryMapper.toDTO(template)) : 
                ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }
}