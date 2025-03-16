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
 * 结果列配置
 * 定义查询结果中列的显示配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultColumnConfig {
    
    /**
     * 配置ID
     */
    private Long id;
    
    /**
     * 关联的界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 字段名
     * API返回的字段名
     */
    private String fieldName;
    
    /**
     * 字段类型
     * STRING, NUMBER, DATE, DATETIME, BOOLEAN等
     */
    private String fieldType;
    
    /**
     * 数据源中的表名
     */
    private String tableName;
    
    /**
     * 数据源中的列名
     */
    private String columnName;
    
    /**
     * 显示名称
     * 显示在表头的名称
     */
    private String displayName;
    
    /**
     * 显示顺序
     */
    private Integer order;
    
    /**
     * 是否可见
     */
    private boolean visible;
    
    /**
     * 是否为主键
     */
    private boolean primaryKey;
    
    /**
     * 列宽度
     * px或%
     */
    private String width;
    
    /**
     * 最小宽度
     */
    private String minWidth;
    
    /**
     * 最大宽度
     */
    private String maxWidth;
    
    /**
     * 对齐方式
     * LEFT, CENTER, RIGHT
     */
    private String align;
    
    /**
     * 固定位置
     * LEFT(左侧固定), RIGHT(右侧固定), null(不固定)
     */
    private String fixed;
    
    /**
     * 是否可排序
     */
    private boolean sortable;
    
    /**
     * 排序方向
     * ASC, DESC, null(不排序)
     */
    private String sortDirection;
    
    /**
     * 排序优先级
     * 多列排序时的优先级顺序
     */
    private Integer sortOrder;
    
    /**
     * 是否可筛选
     */
    private boolean filterable;
    
    /**
     * 筛选操作符
     * 允许的筛选操作符列表
     */
    @Builder.Default
    private List<String> filterOperators = new ArrayList<>();
    
    /**
     * 是否可搜索
     */
    private boolean searchable;
    
    /**
     * 是否为树形列
     * 树形表格时的展开列
     */
    private boolean treeColumn;
    
    /**
     * 是否可拖拽排序
     */
    private boolean draggable;
    
    /**
     * 是否可调整宽度
     */
    private boolean resizable;
    
    /**
     * 格式化类型
     * NONE, DATE, NUMBER, CURRENCY, PERCENT, CUSTOM等
     */
    private String formatType;
    
    /**
     * 格式化模式
     * 例如: "yyyy-MM-dd", "#,###.00"等
     */
    private String formatPattern;
    
    /**
     * 自定义格式化函数
     * JavaScript函数的字符串表示
     */
    private String formatFunction;
    
    /**
     * 前缀
     * 显示值的前缀文本
     */
    private String prefix;
    
    /**
     * 后缀
     * 显示值的后缀文本
     */
    private String suffix;
    
    /**
     * 小数位数
     */
    private Integer decimalPlaces;
    
    /**
     * 千分位分隔符
     */
    private String thousandsSeparator;
    
    /**
     * 小数点符号
     */
    private String decimalSymbol;
    
    /**
     * 空值显示
     * 值为空时显示的文本
     */
    private String emptyValue;
    
    /**
     * 是否支持导出
     */
    private boolean exportable;
    
    /**
     * 导出列名
     * 导出时的列名，默认使用displayName
     */
    private String exportName;
    
    /**
     * 是否支持打印
     */
    private boolean printable;
    
    /**
     * 工具提示
     * 鼠标悬停时的提示文本
     */
    private String tooltip;
    
    /**
     * 组件类型
     * TEXT, LINK, IMAGE, BUTTON, ICON, TAG, BADGE, PROGRESS, RATING等
     */
    private String componentType;
    
    /**
     * 组件属性
     * 渲染组件的特定属性
     */
    @Builder.Default
    private Map<String, Object> componentProps = new HashMap<>();
    
    /**
     * 条件样式规则
     * 条件格式设置
     */
    @Builder.Default
    private List<Map<String, Object>> conditionalStyles = new ArrayList<>();
    
    /**
     * 是否敏感数据
     */
    private boolean sensitive;
    
    /**
     * 掩码类型
     * NONE, FULL(全部掩码), PARTIAL(部分掩码), CUSTOM(自定义掩码)
     */
    private String maskType;
    
    /**
     * 掩码模式
     * 例如: "***-**-XXXX", "X**X**@XX.com"等
     */
    private String maskPattern;
    
    /**
     * 掩码字符
     * 用于替换敏感字符的字符
     */
    private String maskChar;
    
    /**
     * 显示前几位
     * 部分掩码时显示前几位原始字符
     */
    private Integer showFirstChars;
    
    /**
     * 显示后几位
     * 部分掩码时显示后几位原始字符
     */
    private Integer showLastChars;
    
    /**
     * 自定义掩码函数
     * JavaScript函数的字符串表示
     */
    private String customMaskFunction;
    
    /**
     * 是否支持复制
     */
    private boolean copyable;
    
    /**
     * 复制格式
     * 复制时的数据格式
     */
    private String copyFormat;
    
    /**
     * 是否支持编辑
     */
    private boolean editable;
    
    /**
     * 编辑组件类型
     * INPUT, SELECT, DATETIME, RADIO等
     */
    private String editComponentType;
    
    /**
     * 编辑组件属性
     */
    @Builder.Default
    private Map<String, Object> editComponentProps = new HashMap<>();
    
    /**
     * 编辑验证规则
     */
    @Builder.Default
    private List<Map<String, Object>> editRules = new ArrayList<>();
    
    /**
     * 单元格合并规则
     * 单元格合并的计算规则
     */
    private String mergeCellStrategy;
    
    /**
     * 单元格样式
     * 单元格的CSS样式
     */
    @Builder.Default
    private Map<String, Object> cellStyle = new HashMap<>();
    
    /**
     * 表头样式
     * 表头的CSS样式
     */
    @Builder.Default
    private Map<String, Object> headerStyle = new HashMap<>();
    
    /**
     * 自定义类名
     * 单元格的自定义CSS类名
     */
    private String customClass;
    
    /**
     * 表头自定义类名
     * 表头的自定义CSS类名
     */
    private String headerClass;
    
    /**
     * 是否支持汇总
     */
    private boolean summable;
    
    /**
     * 汇总类型
     * SUM, AVG, COUNT, MAX, MIN, CUSTOM等
     */
    private String summaryType;
    
    /**
     * 自定义汇总函数
     * JavaScript函数的字符串表示
     */
    private String summaryFunction;
    
    /**
     * 是否支持分组
     */
    private boolean groupable;
    
    /**
     * 分组折叠状态
     * EXPANDED, COLLAPSED
     */
    private String groupCollapsedState;
    
    /**
     * 渲染函数
     * 自定义渲染的JavaScript函数
     */
    private String renderFunction;
    
    /**
     * 权限控制
     * 用于控制哪些角色/用户可以查看此列
     */
    @Builder.Default
    private Map<String, Object> permissionControl = new HashMap<>();
    
    /**
     * 字段映射
     * 数据源到API的字段映射转换
     */
    private String fieldMapping;
    
    /**
     * 字段转换器
     * 对字段值进行转换的处理
     */
    private String valueTransformer;
    
    /**
     * 是否响应式显示
     * 是否根据屏幕尺寸自动调整显示
     */
    private boolean responsive;
    
    /**
     * 响应式显示规则
     * 不同屏幕尺寸下的显示规则
     */
    @Builder.Default
    private Map<String, Object> responsiveRules = new HashMap<>();
    
    /**
     * 是否为虚拟列
     * 是否是计算出来的虚拟列，非数据源直接获取
     */
    private boolean virtual;
    
    /**
     * 虚拟列表达式
     * 计算虚拟列值的表达式
     */
    private String virtualExpression;
    
    /**
     * 使用频率统计
     * 用于智能推荐和排序
     */
    private Integer frequencyOfUse;
    
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