package com.insightdata.domain.model.lowcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用配置模型
 * 定义低代码平台的应用配置信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppConfig {
    
    /**
     * 应用ID
     */
    private Long id;
    
    /**
     * 应用名称
     */
    private String name;
    
    /**
     * 应用标题
     */
    private String title;
    
    /**
     * 应用描述
     */
    private String description;
    
    /**
     * 应用图标
     */
    private String icon;
    
    /**
     * 应用版本
     */
    private String version;
    
    /**
     * 主题配置
     */
    @Builder.Default
    private Map<String, Object> theme = new HashMap<>();
    
    /**
     * 全局样式
     */
    @Builder.Default
    private Map<String, Object> globalStyles = new HashMap<>();
    
    /**
     * 全局组件配置
     */
    @Builder.Default
    private Map<String, Object> globalComponents = new HashMap<>();
    
    /**
     * 应用页面列表
     */
    @Builder.Default
    private List<PageLayout> pages = new ArrayList<>();
    
    /**
     * 应用导航配置
     */
    @Builder.Default
    private Map<String, Object> navigation = new HashMap<>();
    
    /**
     * 应用权限配置
     */
    @Builder.Default
    private Map<String, Object> permissions = new HashMap<>();
    
    /**
     * 应用设置
     */
    @Builder.Default
    private Map<String, Object> settings = new HashMap<>();
    
    /**
     * 应用创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 应用更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 应用创建者
     */
    private String createdBy;
    
    /**
     * 应用更新者
     */
    private String updatedBy;
    
    /**
     * 应用状态：DRAFT, PUBLISHED, ARCHIVED
     */
    private String status;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 应用分类
     */
    private String category;
    
    /**
     * 应用标签
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
}