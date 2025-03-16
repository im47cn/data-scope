package com.insightdata.domain.model.lowcode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组件模型
 * 定义低代码平台中可用的组件及其配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Component {
    
    /**
     * 组件ID
     */
    private Long id;
    
    /**
     * 组件类型
     * 例如：Button, Table, Form, Chart等
     */
    private String type;
    
    /**
     * 组件名称
     */
    private String name;
    
    /**
     * 组件标题
     * 显示给用户的名称
     */
    private String title;
    
    /**
     * 组件图标
     */
    private String icon;
    
    /**
     * 组件描述
     */
    private String description;
    
    /**
     * 是否是容器
     * 容器组件可以包含其他组件
     */
    private boolean isContainer;
    
    /**
     * 子组件列表
     * 当isContainer为true时有效
     */
    @Builder.Default
    private List<Component> children = new ArrayList<>();
    
    /**
     * 组件属性
     * 定义组件的各种属性，如按钮文本、颜色等
     */
    @Builder.Default
    private Map<String, Object> props = new HashMap<>();
    
    /**
     * 组件样式
     * CSS样式配置
     */
    @Builder.Default
    private Map<String, Object> style = new HashMap<>();
    
    /**
     * 数据绑定配置
     * 定义组件如何与数据源绑定
     */
    @Builder.Default
    private Map<String, Object> dataBindings = new HashMap<>();
    
    /**
     * 关联查询ID
     * 组件可能关联的查询ID
     */
    private Long queryId;
    
    /**
     * 事件处理器
     * 定义组件的各种事件及其处理函数
     */
    @Builder.Default
    private Map<String, Object> eventHandlers = new HashMap<>();
    
    /**
     * 验证规则
     * 用于表单组件的输入验证
     */
    @Builder.Default
    private Map<String, Object> validationRules = new HashMap<>();
    
    /**
     * 条件显示
     * 定义组件在何种条件下显示或隐藏
     */
    @Builder.Default
    private Map<String, Object> conditionalDisplay = new HashMap<>();
    
    /**
     * 权限配置
     * 定义组件的访问和操作权限
     */
    @Builder.Default
    private Map<String, Object> permissions = new HashMap<>();
    
    /**
     * 布局信息
     * 定义组件在容器中的位置和大小
     */
    @Builder.Default
    private Map<String, Object> layout = new HashMap<>();
    
    /**
     * 响应式配置
     * 不同设备尺寸下的显示配置
     */
    @Builder.Default
    private Map<String, Object> responsive = new HashMap<>();
    
    /**
     * 组件状态
     * 存储组件的状态数据
     */
    @Builder.Default
    private Map<String, Object> state = new HashMap<>();
    
    /**
     * 默认值
     * 组件的默认值配置
     */
    private Object defaultValue;
    
    /**
     * 自定义脚本
     * 用于定义组件级的自定义脚本
     */
    private String script;
    
    /**
     * 自定义样式表
     * 用于定义组件级的自定义样式
     */
    private String stylesheet;
    
    /**
     * 国际化配置
     * 组件的多语言支持配置
     */
    @Builder.Default
    private Map<String, Object> i18n = new HashMap<>();
    
    /**
     * 辅助功能配置
     * 组件的无障碍功能配置
     */
    @Builder.Default
    private Map<String, Object> a11y = new HashMap<>();
    
    /**
     * 是否可拖拽
     * 在设计时是否可以拖拽
     */
    private boolean draggable;
    
    /**
     * 是否可调整大小
     * 在设计时是否可以调整大小
     */
    private boolean resizable;
    
    /**
     * 自定义配置项
     */
    @Builder.Default
    private Map<String, Object> customConfig = new HashMap<>();
    
    /**
     * 显示顺序
     */
    private Integer displayOrder;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 是否启用
     */
    private boolean enabled;
}