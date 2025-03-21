package com.insightdata.infrastructure.persistence.entity;

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
 * App实体模型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity {
    
    /**
     * 应用ID
     */
    private String id;
    
    /**
     * 应用编码
     */
    private String code;
    
    /**
     * 应用名称
     */
    private String name;
    
    /**
     * 应用描述
     */
    private String description;
    
    /**
     * 应用图标
     */
    private String icon;
    
    /**
     * 应用类型
     */
    private String type;
    
    /**
     * 应用版本
     */
    private String version;
    
    /**
     * 首页ID
     */
    private Long homePageId;
    
    /**
     * 发布状态
     * 0-未发布 1-已发布
     */
    private Integer publishStatus;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;
    
    /**
     * 应用主题
     */
    @Builder.Default
    private Map<String, String> theme = new HashMap<>();
    
    /**
     * 应用样式配置
     */
    @Builder.Default
    private Map<String, String> styleConfig = new HashMap<>();
    
    /**
     * 全局设置
     */
    @Builder.Default
    private Map<String, String> settings = new HashMap<>();
    
    /**
     * 权限配置
     */
    @Builder.Default
    private Map<String, String> permissions = new HashMap<>();
    
    /**
     * 路由配置
     */
    @Builder.Default
    private Map<String, String> routes = new HashMap<>();
    
    /**
     * 菜单配置
     */
    @Builder.Default
    private Map<String, String> menus = new HashMap<>();
    
    /**
     * 全局状态
     */
    @Builder.Default
    private Map<String, String> globalState = new HashMap<>();
    
    /**
     * 应用关联的查询ID列表
     */
    @Builder.Default
    private List<Long> queryIds = new ArrayList<>();
    
    /**
     * 应用关联的数据源ID列表
     */
    @Builder.Default
    private List<Long> dataSourceIds = new ArrayList<>();
    
    /**
     * 自定义配置项
     */
    @Builder.Default
    private Map<String, String> customConfig = new HashMap<>();
    
    /**
     * 创建人ID
     */
    private Long createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新人ID
     */
    private Long updatedBy;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}