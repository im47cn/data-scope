package com.domain.model.query;

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
 * 操作配置
 * 定义查询结果表格中的操作按钮配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationConfig {
    
    /**
     * 操作ID
     */
    private Long id;
    
    /**
     * 关联的界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 操作代码
     * 用于前端识别的操作唯一代码
     */
    private String code;
    
    /**
     * 操作名称
     * 显示在界面上的操作名称
     */
    private String name;
    
    /**
     * 操作类型
     * BUTTON, LINK, ICON, DROPDOWN_ITEM, POPCONFIRM等
     */
    private String type;
    
    /**
     * 操作图标
     * 图标名称，如：edit, delete, eye等
     */
    private String icon;
    
    /**
     * 操作样式
     * 操作按钮的样式：PRIMARY, DANGER, WARNING, SUCCESS, INFO, DEFAULT等
     */
    private String style;
    
    /**
     * 显示顺序
     * 在操作列中的显示顺序
     */
    private Integer order;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 显示模式
     * ALWAYS(始终显示), HOVER(悬停显示), MORE(更多菜单)
     */
    private String displayMode;
    
    /**
     * 显示条件
     * 定义在何种条件下显示此操作
     */
    @Builder.Default
    private Map<String, Object> displayCondition = new HashMap<>();
    
    /**
     * 是否禁用
     * 是否默认禁用此操作
     */
    private boolean disabled;
    
    /**
     * 禁用条件
     * 定义在何种条件下禁用此操作
     */
    @Builder.Default
    private Map<String, Object> disableCondition = new HashMap<>();
    
    /**
     * 操作提示
     * 鼠标悬停时的提示信息
     */
    private String tooltip;
    
    /**
     * 确认提示
     * 点击操作时是否需要确认
     */
    private boolean requireConfirm;
    
    /**
     * 确认提示标题
     */
    private String confirmTitle;
    
    /**
     * 确认提示内容
     */
    private String confirmContent;
    
    /**
     * 确认按钮文本
     */
    private String confirmOkText;
    
    /**
     * 取消按钮文本
     */
    private String confirmCancelText;
    
    /**
     * 操作权限
     * 执行此操作需要的权限代码
     */
    @Builder.Default
    private List<String> permissions = new ArrayList<>();
    
    /**
     * 操作角色
     * 可执行此操作的角色代码列表
     */
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    
    /**
     * 操作行为类型
     * LINK(链接跳转), API(调用接口), FUNCTION(执行函数), EVENT(触发事件)
     */
    private String actionType;
    
    /**
     * 链接地址
     * 当actionType为LINK时的跳转地址
     */
    private String linkUrl;
    
    /**
     * 链接参数
     * 跳转链接的参数映射规则
     */
    @Builder.Default
    private Map<String, String> linkParams = new HashMap<>();
    
    /**
     * 链接打开方式
     * _SELF(当前窗口), _BLANK(新窗口), MODAL(模态窗), DRAWER(抽屉)
     */
    private String linkTarget;
    
    /**
     * 模态窗/抽屉配置
     * 当linkTarget为MODAL或DRAWER时的相关配置
     */
    @Builder.Default
    private Map<String, Object> modalConfig = new HashMap<>();
    
    /**
     * API接口URL
     * 当actionType为API时的接口地址
     */
    private String apiUrl;
    
    /**
     * API请求方法
     * GET, POST, PUT, DELETE等
     */
    private String apiMethod;
    
    /**
     * API参数
     * 请求参数的映射规则
     */
    @Builder.Default
    private Map<String, String> apiParams = new HashMap<>();
    
    /**
     * API请求头
     * 自定义请求头
     */
    @Builder.Default
    private Map<String, String> apiHeaders = new HashMap<>();
    
    /**
     * API成功处理
     * 接口调用成功后的处理方式
     */
    @Builder.Default
    private Map<String, Object> apiSuccessAction = new HashMap<>();
    
    /**
     * API失败处理
     * 接口调用失败后的处理方式
     */
    @Builder.Default
    private Map<String, Object> apiFailAction = new HashMap<>();
    
    /**
     * 函数名称
     * 当actionType为FUNCTION时的执行函数名
     */
    private String functionName;
    
    /**
     * 函数参数
     * 函数参数的映射规则
     */
    @Builder.Default
    private Map<String, String> functionParams = new HashMap<>();
    
    /**
     * 函数内容
     * JavaScript函数体内容
     */
    private String functionBody;
    
    /**
     * 事件名称
     * 当actionType为EVENT时的事件名
     */
    private String eventName;
    
    /**
     * 事件数据
     * 事件数据的映射规则
     */
    @Builder.Default
    private Map<String, String> eventData = new HashMap<>();
    
    /**
     * 批量操作
     * 是否支持批量选择后操作
     */
    private boolean batchOperation;
    
    /**
     * 批量操作最小选择数
     */
    private Integer batchMinSelection;
    
    /**
     * 批量操作最大选择数
     */
    private Integer batchMaxSelection;
    
    /**
     * 批量操作提示
     * 批量操作时的提示信息
     */
    private String batchOperationTip;
    
    /**
     * 操作分组
     * 操作按钮所属的分组名称
     */
    private String groupName;
    
    /**
     * 操作分组顺序
     * 分组在操作列中的顺序
     */
    private Integer groupOrder;
    
    /**
     * 操作后刷新表格
     * 操作完成后是否刷新表格数据
     */
    private boolean refreshAfterAction;
    
    /**
     * 操作后提示信息
     * 操作成功后的提示信息
     */
    private String successMessage;
    
    /**
     * 操作失败提示信息
     * 操作失败后的提示信息
     */
    private String failMessage;
    
    /**
     * 是否记录操作日志
     */
    private boolean logOperation;
    
    /**
     * 操作日志描述
     * 记录到日志中的操作描述模板
     */
    private String logDescription;
    
    /**
     * 是否启用快捷键
     */
    private boolean enableShortcut;
    
    /**
     * 快捷键配置
     */
    @Builder.Default
    private Map<String, Object> shortcutConfig = new HashMap<>();
    
    /**
     * 是否响应式
     * 是否根据屏幕尺寸调整显示方式
     */
    private boolean responsive;
    
    /**
     * 响应式配置
     * 不同屏幕尺寸下的显示方式配置
     */
    @Builder.Default
    private Map<String, Object> responsiveConfig = new HashMap<>();
    
    /**
     * 权限检查器
     * 用于复杂权限逻辑的自定义检查器
     */
    private String permissionChecker;
    
    /**
     * 自定义样式
     * 按钮的自定义CSS样式
     */
    @Builder.Default
    private Map<String, Object> customStyle = new HashMap<>();
    
    /**
     * 自定义类名
     * 按钮的自定义CSS类名
     */
    private String customClass;
    
    /**
     * 操作描述
     * 用于帮助文档和提示
     */
    private String description;
    
    /**
     * 使用频率
     * 记录操作被使用的频率，用于智能排序
     */
    private Integer useFrequency;
    
    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedAt;
    
    /**
     * 自定义属性
     */
    @Builder.Default
    private Map<String, Object> customProperties = new HashMap<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 备注
     */
    private String remarks;
}