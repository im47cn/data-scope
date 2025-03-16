package com.insightdata.domain.model.query;

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
 * 查询条件配置
 * 定义查询界面中条件的配置信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryConditionConfig {
    
    /**
     * 条件ID
     */
    private Long id;
    
    /**
     * 关联的界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 参数名
     * API查询参数的名称
     */
    private String paramName;
    
    /**
     * 数据源中的表名
     */
    private String tableName;
    
    /**
     * 数据源中的列名
     */
    private String columnName;
    
    /**
     * 数据库字段类型
     * VARCHAR, INT, DATETIME等
     */
    private String dbColumnType;
    
    /**
     * 数据长度
     */
    private Integer dataLength;
    
    /**
     * 显示名称
     * 界面上展示的标签名称
     */
    private String displayName;
    
    /**
     * 显示顺序
     * 条件在界面上的显示顺序
     */
    private Integer order;
    
    /**
     * 组件类型
     * INPUT, SELECT, RADIO, CHECKBOX, DATEPICKER, RANGEPICKER等
     */
    private String componentType;
    
    /**
     * 组件属性
     * 组件的特定属性配置
     */
    @Builder.Default
    private Map<String, Object> componentProps = new HashMap<>();
    
    /**
     * 条件操作符
     * 如：EQ(等于), GT(大于), LT(小于), LIKE(包含), IN(在集合中)等
     */
    private String operator;
    
    /**
     * 是否支持多个操作符
     */
    private boolean multipleOperators;
    
    /**
     * 支持的操作符列表
     * 当multipleOperators为true时有效
     */
    @Builder.Default
    private List<String> supportedOperators = new ArrayList<>();
    
    /**
     * 默认操作符
     * 当支持多个操作符时的默认选择
     */
    private String defaultOperator;
    
    /**
     * 是否必填
     */
    private boolean required;
    
    /**
     * 校验规则
     * 对输入值的验证规则配置
     */
    @Builder.Default
    private List<Map<String, Object>> validationRules = new ArrayList<>();
    
    /**
     * 默认值
     * 条件的默认值
     */
    private String defaultValue;
    
    /**
     * 默认值表达式
     * 用于计算默认值的表达式
     */
    private String defaultValueExpression;
    
    /**
     * 默认值类型
     * STATIC(静态值), DYNAMIC(动态表达式), USER_LAST_USED(用户上次使用), SYSTEM_DEFAULT(系统默认值)
     */
    private String defaultValueType;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 显示模式
     * ALWAYS(始终显示), COLLAPSED(默认折叠), CONDITIONAL(条件显示)
     */
    private String displayMode;
    
    /**
     * 条件显示规则
     * 当displayMode为CONDITIONAL时的显示规则
     */
    @Builder.Default
    private Map<String, Object> displayRules = new HashMap<>();
    
    /**
     * 是否常用条件
     * 常用条件会在界面上默认展示，非常用条件会折叠到"更多条件"中
     */
    private boolean frequentlyUsed;
    
    /**
     * 使用频率
     * 记录该条件被用户使用的频率，用于智能判断是否为常用条件
     */
    private Integer useFrequency;
    
    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedAt;
    
    /**
     * 启用标签
     * 如果为true，则条件会以标签的形式展现
     */
    private boolean enableTag;
    
    /**
     * 标签样式
     * 标签的样式配置
     */
    @Builder.Default
    private Map<String, Object> tagStyle = new HashMap<>();
    
    /**
     * 启用清除
     * 是否显示清除按钮
     */
    private boolean enableClear;
    
    /**
     * 启用帮助提示
     * 是否显示帮助图标和提示信息
     */
    private boolean enableTooltip;
    
    /**
     * 帮助提示内容
     * 鼠标悬停时显示的帮助信息
     */
    private String tooltipContent;
    
    /**
     * 占位文本
     * 输入框的placeholder文本
     */
    private String placeholder;
    
    /**
     * 依赖条件
     * 该条件依赖的其他条件ID列表
     */
    @Builder.Default
    private List<Long> dependsOn = new ArrayList<>();
    
    /**
     * 依赖规则
     * 定义条件间依赖关系的规则
     */
    @Builder.Default
    private Map<String, Object> dependencyRules = new HashMap<>();
    
    /**
     * 级联规则
     * 该条件值变更时影响其他条件的规则
     */
    @Builder.Default
    private Map<String, Object> cascadeRules = new HashMap<>();
    
    /**
     * 只读条件
     * 是否是只读的，用户不能修改
     */
    private boolean readOnly;
    
    /**
     * 只读规则
     * 定义何时条件变为只读的规则
     */
    @Builder.Default
    private Map<String, Object> readOnlyRules = new HashMap<>();
    
    /**
     * 条件组名称
     * 用于将相关条件分组显示
     */
    private String groupName;
    
    /**
     * 组内顺序
     */
    private Integer groupOrder;
    
    /**
     * 字段映射
     * 参数名到数据库字段的映射关系
     */
    private String fieldMapping;
    
    /**
     * 值转换器
     * 从界面值到查询参数值的转换器
     */
    private String valueTransformer;
    
    /**
     * 是否传递空值
     * 当条件值为空时是否传递到API
     */
    private boolean passNullValue;
    
    /**
     * 前端绑定表达式
     * 用于前端绑定的表达式
     */
    private String bindingExpression;
    
    /**
     * 数据源类型
     * STATIC(静态数据), API(接口数据), DICTIONARY(字典数据), SQL(数据库查询)
     */
    private String dataSourceType;
    
    /**
     * 数据源配置
     * 下拉框等组件的数据源配置
     */
    @Builder.Default
    private Map<String, Object> dataSourceConfig = new HashMap<>();
    
    /**
     * 数据源缓存时间(秒)
     * 数据源缓存的有效时间
     */
    private Integer dataSourceCacheDuration;
    
    /**
     * 是否级联过滤
     * 针对下拉框等，是否基于其他条件值过滤选项
     */
    private boolean cascadeFilter;
    
    /**
     * 级联过滤配置
     * 定义如何基于其他条件过滤选项
     */
    @Builder.Default
    private Map<String, Object> cascadeFilterConfig = new HashMap<>();
    
    /**
     * 自动完成配置
     * 自动完成组件的特定配置
     */
    @Builder.Default
    private Map<String, Object> autocompleteConfig = new HashMap<>();
    
    /**
     * 前后缀配置
     * 条件输入框的前后缀配置
     */
    @Builder.Default
    private Map<String, Object> affixConfig = new HashMap<>();
    
    /**
     * 是否支持响应式
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
     * 条件布局
     * 在表单中的布局配置
     */
    @Builder.Default
    private Map<String, Object> layoutConfig = new HashMap<>();
    
    /**
     * 宽度
     * 条件在表单中的宽度(1-24)
     */
    private Integer span;
    
    /**
     * 是否支持快捷键
     */
    private boolean enableShortcut;
    
    /**
     * 快捷键配置
     */
    @Builder.Default
    private Map<String, Object> shortcutConfig = new HashMap<>();
    
    /**
     * 高级配置
     * 其他特殊配置
     */
    @Builder.Default
    private Map<String, Object> advancedConfig = new HashMap<>();
    
    /**
     * 权限控制
     * 用于控制哪些角色/用户可以使用此条件
     */
    @Builder.Default
    private Map<String, Object> permissionControl = new HashMap<>();
    
    /**
     * 条件项描述
     * 用于帮助文档和提示
     */
    private String description;
    
    /**
     * 是否为临时条件
     * 临时条件不会被保存为默认配置
     */
    private boolean temporary;
    
    /**
     * 自定义样式
     * 条件项的自定义CSS样式
     */
    @Builder.Default
    private Map<String, Object> customStyle = new HashMap<>();
    
    /**
     * 自定义类名
     * 条件项的自定义CSS类名
     */
    private String customClass;
    
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