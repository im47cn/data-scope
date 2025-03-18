package com.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.domain.model.query.SavedQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.common.exception.InsightDataException;
import com.domain.repository.SavedQueryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavedQueryService {

    private final SavedQueryRepository savedQueryRepository;

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
        
        savedQueryRepository.insert(template);
        log.info("Created query template: {}", template.getId());
        
        return template;
    }

    /**
     * 更新查询模板
     */
    @Transactional
    public SavedQuery updateTemplate(String id, SavedQuery template, String userId) {
        SavedQuery existing = savedQueryRepository.selectById(id);
        if (existing == null) {
            throw new InsightDataException("模板不存在");
        }
        
        if (!existing.getCreatedBy().equals(userId) && !existing.getIsShared()) {
            throw new InsightDataException("无权修改此模板");
        }
        
        template.setId(id);
        template.setUpdatedAt(LocalDateTime.now());
        savedQueryRepository.update(template);
        
        log.info("Updated query template: {}", id);
        return template;
    }

    /**
     * 删除查询模板
     */
    @Transactional
    public void deleteTemplate(String id, String userId) {
        SavedQuery template = savedQueryRepository.selectById(id);
        if (template == null) {
            throw new InsightDataException("模板不存在");
        }
        
        if (!template.getCreatedBy().equals(userId) && !template.getIsShared()) {
            throw new InsightDataException("无权删除此模板");
        }
        
        savedQueryRepository.deleteById(id);
        log.info("Deleted query template: {}", id);
    }

    /**
     * 查找模板
     */
    public List<SavedQuery> findTemplates(String dataSourceId, String userId) {
        return savedQueryRepository.selectByDataSourceIdAndIsPublic(dataSourceId, true);
    }

    /**
     * 按标签查找模板
     */
    public List<SavedQuery> findTemplatesByTags(List<String> tags) {
        return savedQueryRepository.selectByTags(tags);
    }

    /**
     * 搜索模板
     */
    public List<SavedQuery> searchTemplates(String keyword) {
        return savedQueryRepository.selectByNameLike(keyword);
    }

    /**
     * 使用模板
     */
    @Transactional
    public void useTemplate(String id, long executionTime) {
        savedQueryRepository.incrementUsageCount(id);
        savedQueryRepository.updateExecutionStats(id, executionTime);
        log.info("Updated template usage stats: {}", id);
    }

    /**
     * 检查模板访问权限
     */
    public boolean hasAccess(String id, String userId) {
        SavedQuery template = savedQueryRepository.selectById(id);
        return template != null && 
               (template.getIsShared() || template.getCreatedBy().equals(userId));
    }
}