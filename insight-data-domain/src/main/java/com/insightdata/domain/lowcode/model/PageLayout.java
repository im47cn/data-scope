package com.insightdata.domain.lowcode.model;

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
 * 页面布局模型
 * 定义低代码平台的页面布局信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageLayout {
    
    /**
     * 页面ID
     */
    private String id;
    
    /**
     * 应用ID
     */
    private Long appId;
    
    /**
     * 页面名称/标识
     */
    private String name;
    
    /**
     * 页面标题
     */
    private String title;
    
    /**
     * 页面描述
     */
    private String description;
    
    /**
     * 页面图标
     */
    private String icon;
    
    /**
     * 页面路径
     */
    private String path;
    
    /**
     * 页面布局类型：FIXED, RESPONSIVE, GRID, FLEX
     */
    private String layoutType;
    
    /**
     * 页面组件列表
     */
    @Builder.Default
    private List<ComponentConfig> components = new ArrayList<>();
    
    /**
     * 页面样式
     */
    @Builder.Default
    private Map<String, Object> style = new HashMap<>();
    
    /**
     * 页面主题
     */
    @Builder.Default
    private Map<String, Object> theme = new HashMap<>();
    
    /**
     * 页面变量
     */
    @Builder.Default
    private Map<String, Object> variables = new HashMap<>();
    
    /**
     * 页面模板ID
     */
    private Long templateId;
    
    /**
     * 页面事件
     */
    @Builder.Default
    private Map<String, Object> events = new HashMap<>();
    
    /**
     * 页面权限配置
     */
    @Builder.Default
    private Map<String, Object> permissions = new HashMap<>();
    
    /**
     * 页面脚本
     */
    private String script;
    
    /**
     * 页面样式表
     */
    private String stylesheet;
    
    /**
     * 页面创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 页面更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 页面创建者
     */
    private String createdBy;
    
    /**
     * 页面更新者
     */
    private String updatedBy;
    
    /**
     * 页面状态：DRAFT, PUBLISHED, ARCHIVED
     */
    private String status;
    
    /**
     * 排序索引
     */
    private Integer sortIndex;
    
    /**
     * 是否首页
     */
    private boolean isHomePage;
    
    /**
     * 是否启用
     */
    private boolean enabled;
}