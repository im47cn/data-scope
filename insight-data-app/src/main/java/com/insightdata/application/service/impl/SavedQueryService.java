package com.insightdata.application.service.impl;

import com.insightdata.domain.query.model.SavedQuery;
import com.insightdata.domain.repository.SavedQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class SavedQueryService {

    @Autowired
    private SavedQueryRepository savedQueryRepository;

    /**
     * 创建查询模板
     */
    @Transactional
    public SavedQuery createTemplate(SavedQuery template, String userId) {
        template.setId(UUID.randomUUID().toString());
        template.setCreatedBy(userId);
        template.setCreatedAt(LocalDateTime.now());
        template.setUpdatedAt(LocalDateTime.now());
        template.setExecutionCount(0L);

        SavedQuery created = savedQueryRepository.save(template);
        log.info("Created query template: {}", template.getId());
        return created;
    }

    /**
     * 更新查询模板
     */
    @Transactional
    public SavedQuery updateTemplate(String id, SavedQuery template, String userId) {
        SavedQuery existing = savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));

        if (!existing.getCreatedBy().equals(userId) && !existing.getIsShared()) {
            throw new IllegalArgumentException("No permission to update template: " + id);
        }

        template.setId(id);
        template.setUpdatedAt(LocalDateTime.now());

        SavedQuery updated = savedQueryRepository.save(template);
        log.info("Updated query template: {}", id);
        return updated;
    }

    /**
     * 删除查询模板
     */
    @Transactional
    public void deleteTemplate(String id, String userId) {
        SavedQuery template = savedQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + id));

        if (!template.getCreatedBy().equals(userId) && !template.getIsShared()) {
            throw new IllegalArgumentException("No permission to delete template: " + id);
        }

        savedQueryRepository.deleteById(id);
        log.info("Deleted query template: {}", id);
    }

    /**
     * 查找模板
     */
    public List<SavedQuery> findTemplates(String dataSourceId, String userId) {
        return savedQueryRepository.findByDataSourceIdAndUserId(dataSourceId, userId);
    }

    /**
     * 按标签查找模板
     */
    public List<SavedQuery> findTemplatesByTags(List<String> tags) {
        return savedQueryRepository.findByTags(tags);
    }

    /**
     * 搜索模板
     */
    public List<SavedQuery> searchTemplates(String keyword) {
        return savedQueryRepository.findByNameLike("%" + keyword + "%");
    }

    /**
     * 使用模板
     */
    @Transactional
    public void useTemplate(String id, long executionTime) {
        savedQueryRepository.updateUsageStats(id, executionTime);
        log.info("Updated template usage stats: {}", id);
    }
    
    /**
     * 记录模板使用
     */
    @Transactional
    public void recordTemplateUsage(String id, long executionTime) {
        useTemplate(id, executionTime);
    }

    /**
     * 检查用户是否有权限访问模板
     */
    public boolean hasAccess(String id, String userId) {
        Optional<SavedQuery> template = savedQueryRepository.findById(id);
        return template.filter(t -> 
                (t.getIsShared() || t.getCreatedBy().equals(userId))).isPresent();
    }
    
    /**
     * 根据ID查找模板
     */
    public Optional<SavedQuery> findTemplateById(String id, String userId) {
        Optional<SavedQuery> template = savedQueryRepository.findById(id);
        return template.filter(t -> 
                (t.getIsShared() || t.getCreatedBy().equals(userId)));
    }
}