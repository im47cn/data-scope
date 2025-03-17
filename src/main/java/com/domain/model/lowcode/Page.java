package com.domain.model.lowcode;

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
 * 页面模型
 * 定义低代码平台中的页面及其配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    
    /**
     * 页面ID
     */
    private String id;
    
    /**
     * 所属应用ID
     */
    private Long appId;
    
    /**
     * 页面标题
     */
    private String title;
    
    /**
     * 页面名称
     * 用于在代码中引用
     */
    private String name;
    
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
     * 用于路由
     */
    private String path;
    
    /**
     * 是否为首页
     */
    private boolean isHome;
    
    /**
     * 页面布局类型
     * 例如：fixed, fluid, responsive等
     */
    private String layoutType;
    
    /**
     * 页面模板
     * 可用于快速创建相似页面
     */
    private String template;
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 是否需要认证
     */
    private boolean requireAuth;
    
    /**
     * 是否缓存
     * 控制页面是否缓存，避免重复渲染
     */
    private boolean cached;
    
    /**
     * 页面组件列表
     */
    @Builder.Default
    private List<Component> components = new ArrayList<>();
    
    /**
     * 布局配置
     * 定义组件在页面中的位置和布局
     */
    @Builder.Default
    private Map<String, Object> layoutConfig = new HashMap<>();
    
    /**
     * 页面样式
     */
    @Builder.Default
    private Map<String, Object> style = new HashMap<>();
    
    /**
     * 页面主题
     * 可覆盖应用级主题设置
     */
    private String theme;
    
    /**
     * 页面脚本
     * 用于定义页面级的自定义脚本
     */
    private String script;
    
    /**
     * 页面样式表
     * 用于定义页面级的自定义样式
     */
    private String stylesheet;
    
    /**
     * 页面状态
     * 用于存储页面级的状态数据
     */
    @Builder.Default
    private Map<String, Object> state = new HashMap<>();
    
    /**
     * 页面参数
     * 定义页面可接收的参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 页面事件
     * 如：onLoad, onUnload等
     */
    @Builder.Default
    private Map<String, Object> events = new HashMap<>();
    
    /**
     * 页面生命周期钩子
     * 如：beforeCreate, created, mounted等
     */
    @Builder.Default
    private Map<String, Object> lifecycleHooks = new HashMap<>();
    
    /**
     * 响应式配置
     * 不同设备尺寸下的显示配置
     */
    @Builder.Default
    private Map<String, Object> responsiveSettings = new HashMap<>();
    
    /**
     * 权限配置
     * 定义页面的访问权限
     */
    @Builder.Default
    private Map<String, Object> permissions = new HashMap<>();
    
    /**
     * 关联查询ID列表
     */
    @Builder.Default
    private List<Long> queryIds = new ArrayList<>();
    
    /**
     * SEO设置
     */
    @Builder.Default
    private Map<String, Object> seoSettings = new HashMap<>();
    
    /**
     * 自定义配置项
     */
    @Builder.Default
    private Map<String, Object> customConfig = new HashMap<>();
    
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