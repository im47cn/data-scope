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
 * 查询界面配置
 * 定义一个完整的查询界面，包括查询条件、结果列、操作按钮等配置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryInterfaceConfig {
    
    /**
     * 界面配置ID
     */
    private String id;
    
    /**
     * 配置代码
     * 唯一标识符，用于API路径等
     */
    private String code;
    
    /**
     * 配置名称
     */
    private String name;
    
    /**
     * 配置版本
     * 支持配置的版本控制
     */
    private String version;
    
    /**
     * 关联的数据源ID
     */
    private Long dataSourceId;
    
    /**
     * 主表名
     */
    private String mainTable;
    
    /**
     * 关联表配置
     * 定义主表与其他表的关联关系
     */
    @Builder.Default
    private List<Map<String, Object>> tableRelations = new ArrayList<>();
    
    /**
     * 基础SQL
     * 查询的基础SQL模板，可包含占位符
     */
    private String baseSql;
    
    /**
     * SQL类型
     * TEMPLATE(模板), DYNAMIC(动态生成), STORED_PROCEDURE(存储过程)
     */
    private String sqlType;
    
    /**
     * 查询条件配置列表
     */
    @Builder.Default
    private List<QueryConditionConfig> conditions = new ArrayList<>();
    
    /**
     * 结果列配置列表
     */
    @Builder.Default
    private List<ResultColumnConfig> columns = new ArrayList<>();
    
    /**
     * 操作配置列表
     */
    @Builder.Default
    private List<OperationConfig> operations = new ArrayList<>();
    
    /**
     * 界面类型
     * QUERY(查询), FORM(表单), DETAIL(详情), CHART(图表)
     */
    private String interfaceType;
    
    /**
     * 布局类型
     * TABLE(表格), CARD(卡片), LIST(列表), TREE(树形), CUSTOM(自定义)
     */
    private String layoutType;
    
    /**
     * 布局配置
     * 详细的布局配置信息
     */
    @Builder.Default
    private Map<String, Object> layoutConfig = new HashMap<>();
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 是否公开
     * 是否可被所有用户访问
     */
    private boolean publicAccess;
    
    /**
     * 创建者ID
     */
    private String creatorId;
    
    /**
     * 所属业务模块
     */
    private String module;
    
    /**
     * 标签列表
     * 用于分类和过滤
     */
    @Builder.Default
    private List<String> tags = new ArrayList<>();
    
    /**
     * 默认排序字段
     */
    private String defaultSortField;
    
    /**
     * 默认排序方向
     * ASC(升序), DESC(降序)
     */
    private String defaultSortDirection;
    
    /**
     * 分页配置
     */
    @Builder.Default
    private Map<String, Object> paginationConfig = new HashMap<>();
    
    /**
     * 缓存配置
     * 配置查询结果的缓存策略
     */
    @Builder.Default
    private Map<String, Object> cacheConfig = new HashMap<>();
    
    /**
     * 权限配置
     * 访问此界面需要的权限
     */
    @Builder.Default
    private Map<String, Object> permissionConfig = new HashMap<>();
    
    /**
     * 安全配置
     * 例如SQL注入防护、敏感数据处理等
     */
    @Builder.Default
    private Map<String, Object> securityConfig = new HashMap<>();
    
    /**
     * 查询频率限制
     * 限制每个用户的查询频率
     */
    private Integer queryRateLimit;
    
    /**
     * 查询超时设置(秒)
     */
    private Integer queryTimeout;
    
    /**
     * 最大导出数量
     */
    private Integer maxExportCount;
    
    /**
     * 显示样式
     * 界面的整体样式配置
     */
    @Builder.Default
    private Map<String, Object> styleConfig = new HashMap<>();
    
    /**
     * 主题配置
     */
    @Builder.Default
    private Map<String, Object> themeConfig = new HashMap<>();
    
    /**
     * 国际化配置
     */
    @Builder.Default
    private Map<String, Object> i18nConfig = new HashMap<>();
    
    /**
     * 高级选项配置
     */
    @Builder.Default
    private Map<String, Object> advancedOptions = new HashMap<>();
    
    /**
     * 后处理器
     * 查询结果的后处理逻辑
     */
    private String postProcessor;
    
    /**
     * 前置处理器
     * 查询前的预处理逻辑
     */
    private String preProcessor;
    
    /**
     * 自定义脚本
     * 可以在界面中执行的自定义脚本
     */
    private String customScript;
    
    /**
     * 数据转换器
     * 用于转换API响应数据的格式
     */
    private String dataTransformer;
    
    /**
     * 是否记录查询历史
     */
    private boolean logQueryHistory;
    
    /**
     * 是否允许条件保存
     * 用户可以保存常用的查询条件
     */
    private boolean allowSaveConditions;
    
    /**
     * 是否允许列定制
     * 用户可以自定义显示的列
     */
    private boolean allowColumnCustomization;
    
    /**
     * 是否允许视图保存
     * 用户可以保存自定义视图
     */
    private boolean allowSaveView;
    
    /**
     * 是否支持数据下载
     */
    private boolean supportExport;
    
    /**
     * 导出格式
     * CSV, EXCEL, PDF等
     */
    private List<String> exportFormats;
    
    /**
     * 是否支持打印
     */
    private boolean supportPrint;
    
    /**
     * 打印模板
     */
    private String printTemplate;
    
    /**
     * 自动刷新设置
     * 配置数据自动刷新的间隔等
     */
    @Builder.Default
    private Map<String, Object> autoRefreshConfig = new HashMap<>();
    
    /**
     * 高级查询模式
     * 是否允许用户编写SQL等高级查询
     */
    private boolean advancedQueryMode;
    
    /**
     * 是否支持图表视图
     */
    private boolean supportChartView;
    
    /**
     * 图表配置
     */
    @Builder.Default
    private Map<String, Object> chartConfig = new HashMap<>();
    
    /**
     * 使用统计
     * 记录该界面配置的使用统计信息
     */
    @Builder.Default
    private Map<String, Object> usageStatistics = new HashMap<>();
    
    /**
     * 移动端配置
     * 针对移动设备的特殊配置
     */
    @Builder.Default
    private Map<String, Object> mobileConfig = new HashMap<>();
    
    /**
     * 关联的低代码配置
     * 与低代码平台集成的配置信息
     */
    @Builder.Default
    private Map<String, Object> lowCodeConfig = new HashMap<>();
    
    /**
     * API路径
     * 访问此界面数据的API路径
     */
    private String apiPath;
    
    /**
     * API版本
     */
    private String apiVersion;
    
    /**
     * 是否默认加载数据
     * 界面加载后是否自动执行查询
     */
    private boolean loadDataOnMount;
    
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