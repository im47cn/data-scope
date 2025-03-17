package com.domain.model.lowcode;

import com.common.enums.ComponentType;
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
 * 组件配置模型
 * 定义低代码平台中的组件配置信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponentConfig {
    
    /**
     * 组件ID
     */
    private String id;
    
    /**
     * 页面ID
     */
    private Long pageId;
    
    /**
     * 父组件ID
     */
    private Long parentId;
    
    /**
     * 组件名称/标识
     */
    private String name;
    
    /**
     * 组件类型
     */
    private ComponentType type;
    
    /**
     * 组件标题
     */
    private String title;
    
    /**
     * 组件描述
     */
    private String description;
    
    /**
     * 组件图标
     */
    private String icon;
    
    /**
     * 组件位置
     * {x: 0, y: 0, width: 100, height: 100}
     */
    @Builder.Default
    private Map<String, Integer> position = new HashMap<>();
    
    /**
     * 组件样式
     */
    @Builder.Default
    private Map<String, Object> style = new HashMap<>();
    
    /**
     * 组件属性
     */
    @Builder.Default
    private Map<String, Object> props = new HashMap<>();
    
    /**
     * 组件事件
     */
    @Builder.Default
    private Map<String, Object> events = new HashMap<>();
    
    /**
     * 组件数据
     */
    @Builder.Default
    private Map<String, Object> data = new HashMap<>();
    
    /**
     * 组件插槽配置
     */
    @Builder.Default
    private Map<String, Object> slots = new HashMap<>();
    
    /**
     * 组件条件渲染表达式
     */
    private String conditional;
    
    /**
     * 组件权限配置
     */
    @Builder.Default
    private Map<String, Object> permissions = new HashMap<>();
    
    /**
     * 组件验证规则
     */
    @Builder.Default
    private List<Map<String, Object>> validationRules = new ArrayList<>();
    
    /**
     * 子组件
     */
    @Builder.Default
    private List<ComponentConfig> children = new ArrayList<>();
    
    /**
     * 数据绑定列表
     */
    @Builder.Default
    private List<DataBinding> dataBindings = new ArrayList<>();
    
    /**
     * 布局栅格列占比，24列栅格系统
     */
    private Integer span;
    
    /**
     * 组件自定义脚本
     */
    private String script;
    
    /**
     * 组件自定义样式表
     */
    private String stylesheet;
    
    /**
     * 组件创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 组件更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 组件创建者
     */
    private String createdBy;
    
    /**
     * 组件更新者
     */
    private String updatedBy;
    
    /**
     * 组件状态：DRAFT, PUBLISHED, ARCHIVED
     */
    private String status;
    
    /**
     * 排序索引
     */
    private Integer sortIndex;
    
    /**
     * 是否可拖动
     */
    private boolean draggable;
    
    /**
     * 是否可调整大小
     */
    private boolean resizable;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 是否锁定
     */
    private boolean locked;
}