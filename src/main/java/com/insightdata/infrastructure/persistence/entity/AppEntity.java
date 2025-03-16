package com.insightdata.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 应用实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppEntity {
    
    /**
     * 应用ID
     */
    private Long id;
    
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
     * 发布状态：0-未发布，1-已发布
     */
    private Integer publishStatus;
    
    /**
     * 发布时间
     */
    private LocalDateTime publishedAt;
    
    /**
     * 主题
     */
    private Map<String, String> theme;
    
    /**
     * 样式配置
     */
    private Map<String, String> styleConfig;
    
    /**
     * 设置
     */
    private Map<String, String> settings;
    
    /**
     * 权限设置
     */
    private Map<String, String> permissions;
    
    /**
     * 路由配置
     */
    private Map<String, String> routes;
    
    /**
     * 菜单配置
     */
    private Map<String, String> menus;
    
    /**
     * 全局状态
     */
    private Map<String, String> globalState;
    
    /**
     * 关联的查询ID列表
     */
    private List<Long> queryIds;
    
    /**
     * 关联的数据源ID列表
     */
    private List<Long> dataSourceIds;
    
    /**
     * 自定义配置
     */
    private Map<String, String> customConfig;
    
    /**
     * 创建人
     */
    private Long createdBy;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新人
     */
    private Long updatedBy;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}