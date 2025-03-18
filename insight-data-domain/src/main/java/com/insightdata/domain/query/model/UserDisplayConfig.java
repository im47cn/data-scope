package com.insightdata.domain.query.model;

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
 * 用户显示配置
 * 存储用户对查询界面的个性化显示配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDisplayConfig {
    
    /**
     * 配置ID
     */
    private String id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 关联的界面配置ID
     */
    private Long interfaceConfigId;
    
    /**
     * 保存的查询ID
     * 当此配置关联到特定保存的查询时使用
     */
    private Long savedQueryId;
    
    /**
     * 配置名称
     */
    private String name;
    
    /**
     * 是否为默认配置
     */
    private boolean isDefault;
    
    /**
     * 是否共享配置
     * 是否可以被其他用户使用
     */
    private boolean shared;
    
    /**
     * 列显示顺序
     * 列ID及其显示顺序
     */
    @Builder.Default
    private Map<String, Integer> columnOrder = new HashMap<>();
    
    /**
     * 列宽设置
     * 列ID及其宽度(px或%)
     */
    @Builder.Default
    private Map<String, String> columnWidth = new HashMap<>();
    
    /**
     * 固定列设置
     * LEFT(左侧固定), RIGHT(右侧固定), null(不固定)
     */
    @Builder.Default
    private Map<String, String> fixedColumns = new HashMap<>();
    
    /**
     * 列可见性
     * 列ID及其可见状态
     */
    @Builder.Default
    private Map<String, Boolean> columnVisibility = new HashMap<>();
    
    /**
     * 排序配置
     * 多列排序配置
     */
    @Builder.Default
    private List<Map<String, Object>> sortConfigs = new ArrayList<>();
    
    /**
     * 过滤条件配置
     * 用户保存的条件值
     */
    @Builder.Default
    private Map<String, Object> filterValues = new HashMap<>();
    
    /**
     * 高级过滤配置
     * 用户自定义的复杂过滤条件
     */
    @Builder.Default
    private List<Map<String, Object>> advancedFilters = new ArrayList<>();
    
    /**
     * 页面大小
     * 每页显示的记录数
     */
    private Integer pageSize;
    
    /**
     * 分组设置
     * 是否启用分组及分组配置
     */
    @Builder.Default
    private Map<String, Object> groupSettings = new HashMap<>();
    
    /**
     * 展开行设置
     * 自动展开的行及展开设置
     */
    @Builder.Default
    private Map<String, Object> expandSettings = new HashMap<>();
    
    /**
     * 汇总配置
     * 底部汇总行配置
     */
    @Builder.Default
    private Map<String, Object> summarySettings = new HashMap<>();
    
    /**
     * 列格式化配置
     * 列ID及其格式化设置
     */
    @Builder.Default
    private Map<String, Map<String, Object>> columnFormat = new HashMap<>();
    
    /**
     * 条件区布局
     * 条件区的布局配置
     */
    @Builder.Default
    private Map<String, Object> filterAreaLayout = new HashMap<>();
    
    /**
     * 表格主题
     * 用户选择的表格主题
     */
    private String tableTheme;
    
    /**
     * 表格密度
     * DENSE(紧凑), MEDIUM(中等), SPARSE(宽松)
     */
    private String tableDensity;
    
    /**
     * 是否显示行号
     */
    private boolean showRowNumber;
    
    /**
     * 是否显示边框
     */
    private boolean showBorder;
    
    /**
     * 是否显示条纹
     */
    private boolean showStripe;
    
    /**
     * 是否启用列筛选器
     */
    private boolean enableColumnFilter;
    
    /**
     * 是否启用列排序
     */
    private boolean enableColumnSort;
    
    /**
     * 是否启用列拖拽
     */
    private boolean enableColumnDrag;
    
    /**
     * 是否启用列宽调整
     */
    private boolean enableColumnResize;
    
    /**
     * 是否允许选择行
     */
    private boolean enableRowSelection;
    
    /**
     * 选择类型
     * SINGLE(单选), MULTIPLE(多选)
     */
    private String selectionType;
    
    /**
     * 操作列设置
     * 操作列的位置、宽度等
     */
    @Builder.Default
    private Map<String, Object> operationColumnSettings = new HashMap<>();
    
    /**
     * 高亮规则
     * 行/单元格高亮规则
     */
    @Builder.Default
    private List<Map<String, Object>> highlightRules = new ArrayList<>();
    
    /**
     * 条件颜色规则
     * 条件格式设置
     */
    @Builder.Default
    private List<Map<String, Object>> conditionalFormatRules = new ArrayList<>();
    
    /**
     * 图表显示设置
     * 是否启用图表视图及图表配置
     */
    @Builder.Default
    private Map<String, Object> chartSettings = new HashMap<>();
    
    /**
     * 导出设置
     * 导出相关配置
     */
    @Builder.Default
    private Map<String, Object> exportSettings = new HashMap<>();
    
    /**
     * 打印设置
     * 打印相关配置
     */
    @Builder.Default
    private Map<String, Object> printSettings = new HashMap<>();
    
    /**
     * 自定义样式
     * 用户自定义的CSS样式
     */
    @Builder.Default
    private Map<String, Object> customStyle = new HashMap<>();
    
    /**
     * 自定义JS
     * 用户自定义的JS脚本
     */
    private String customScript;
    
    /**
     * 使用频率
     * 记录使用次数，用于智能排序
     */
    private Integer useFrequency;
    
    /**
     * 最后使用时间
     */
    private LocalDateTime lastUsedAt;
    
    /**
     * 响应式配置
     * 在不同屏幕尺寸下的显示配置
     */
    @Builder.Default
    private Map<String, Object> responsiveConfig = new HashMap<>();
    
    /**
     * 设备类型
     * DESKTOP, MOBILE, TABLET等
     */
    private String deviceType;
    
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