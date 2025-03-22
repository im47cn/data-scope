# 技术规范

本文档详细说明技术实现规范，包括技术栈选择、架构模块划分、接口定义、数据模型设计及关键实现细节。

## 1. 技术栈

### 1.1 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 17+ | 开发语言 |
| Spring Boot | 3.x | 应用框架 |
| MyBatis | 3.x | 数据访问层 |
| Maven | 3.8+ | 构建工具 |
| MySQL | 8.0+ | 系统数据存储 |
| Redis | 6.0+ | 分布式缓存 |
| Lombok | 最新稳定版 | 简化Java代码 |
| MapStruct | 最新稳定版 | 对象映射 |
| Logback | 最新稳定版 | 日志管理 |
| JUnit 5 | 最新稳定版 | 单元测试 |
| Mockito | 最新稳定版 | 测试框架 |

### 1.2 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| HTML5 | - | 页面结构 |
| Tailwind CSS | 2.2.19+ | 样式框架 |
| JavaScript/TypeScript | ES6+ | 脚本语言 |
| Vue.js | 3.x | 前端框架 |
| Pinia | 最新稳定版 | 状态管理 |
| Vue Router | 最新稳定版 | 路由管理 |
| FontAwesome | 6.x | 图标库 |
| Element Plus / Ant Design Vue | 最新稳定版 | UI组件库 |
| Chart.js / ECharts | 最新稳定版 | 图表库 |
| Axios | 最新稳定版 | HTTP客户端 |

### 1.3 外部服务集成

| 服务 | 用途 |
|------|------|
| OpenRouter LLM API | 自然语言处理 |
| MySQL数据库 | 外部数据源 |
| DB2数据库 | 外部数据源 |
| Redis服务 | 分布式缓存 |

## 2. DDD四层架构划分

系统基于领域驱动设计(DDD)的四层架构模式实现，清晰划分职责边界：

### 2.1 领域层 (Domain)

- **职责**：封装核心业务逻辑和领域模型，定义领域实体、值对象、聚合根和领域服务
- **目录结构**：`com.domain.model`、`com.domain.service`、`com.domain.repository`
- **主要内容**：
  - 领域实体：如DataSource、TableInfo、SavedQuery等
  - 领域服务：核心业务逻辑
  - 仓储接口：定义持久化操作的接口

### 2.2 应用层 (Application)

- **职责**：协调领域对象完成用户用例，处理事务边界，不包含业务规则
- **目录结构**：`com.application.service`
- **主要内容**：
  - 应用服务：如DataSourceService、QueryService等
  - 事务管理：定义事务边界
  - 领域事件处理
  - 用例编排

### 2.3 基础设施层 (Infrastructure)

- **职责**：提供技术实现和外部系统集成，实现仓储接口
- **目录结构**：`com.infrastructure.persistence`、`com.infrastructure.adapter`、`com.infrastructure.service`
- **主要内容**：
  - 仓储实现：基于MyBatis的持久化实现
  - 外部系统适配器：如LLM API适配器
  - 缓存实现
  - 消息队列实现
  - 安全组件

### 2.4 接口层 (Facade)

- **职责**：处理外部请求和响应转换，协调用户界面和应用层
- **目录结构**：`com.facade.rest`、`com.facade.dto`、`com.facade.mapper`
- **主要内容**：
  - REST控制器
  - DTO对象定义
  - DTO与领域对象的转换
  - 参数验证
  - 异常处理

## 3. 核心模块设计

系统划分为以下核心模块，每个模块包含特定的领域逻辑和技术实现。

### 3.1 数据源管理模块

#### 3.1.1 关键领域模型

```java
// 数据源实体
public class DataSource {
    private String id;                // UUID主键
    private String name;              // 数据源名称
    private DataSourceType type;      // 数据源类型(MySQL/DB2)
    private String host;              // 服务器地址
    private int port;                 // 端口
    private String databaseName;      // 数据库名称
    private String username;          // 用户名
    private String encryptedPassword; // 加密密码
    private String encryptionSalt;    // 加密盐值
    private Map<String, String> connectionProperties; // 连接参数
    private boolean active;           // 是否激活
    // 审计字段
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;
    private int nonce;                // 乐观锁字段
}

// 表信息实体
public class TableInfo {
    private String id;
    private String schemaId;
    private String name;
    private String type;
    private String description;
    private Long estimatedRowCount;
    private List<ColumnInfo> columns;
    private List<IndexInfo> indexes;
    // 审计字段...
}

// 表关系实体
public class TableRelationship {
    private String id;
    private String sourceTableId;
    private String targetTableId;
    private String sourceColumnId;
    private String targetColumnId;
    private RelationType relationType;
    private boolean isInferred;
    private double confidence;  // 推断关系的置信度0-1
    // 审计字段...
}

// 元数据同步作业
public class MetadataSyncJob {
    private String id;
    private String dataSourceId;
    private SyncType type;      // 全量/增量
    private SyncStatus status;  // 状态(进行中/完成/失败)
    private int progress;       // 进度百分比
    private String errorMessage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    // 审计字段...
}
```

#### 3.1.2 接口定义

```java
// 数据源服务接口
public interface DataSourceService {
    DataSource createDataSource(DataSourceDTO dto);
    DataSource getDataSource(String id);
    List<DataSource> getAllDataSources();
    DataSource updateDataSource(String id, DataSourceDTO dto);
    void deleteDataSource(String id);
    ConnectionTestResult testConnection(String id);
    MetadataSyncJob synchronizeMetadata(String id, SyncType syncType);
    MetadataSyncJob getSyncJob(String jobId);
    HealthStatus getHealthStatus(String id);
}

// 元数据服务接口
public interface MetadataService {
    List<SchemaInfo> getSchemas(String dataSourceId);
    List<TableInfo> getTables(String schemaId);
    List<ColumnInfo> getColumns(String tableId);
    List<IndexInfo> getIndexes(String tableId);
    List<Object[]> getSampleData(String tableId, int limit);
    TableRelationship createRelationship(RelationshipDTO dto);
    List<TableRelationship> detectRelationships(String dataSourceId, double confidenceThreshold);
}
```

#### 3.1.3 实现细节

1. **数据源凭证加密**
   - 使用AES-256加密算法保护数据源密码
   - 每个密码使用唯一的随机盐值
   - 实现主密钥轮换机制
   
```java
public class CredentialEncryptor {
    public String encrypt(String plaintext, String salt) {
        // 使用AES-256加密，CBC模式
        // 返回Base64编码的密文
    }
    
    public String decrypt(String ciphertext, String salt) {
        // 解密过程
    }
    
    public String generateSalt() {
        // 生成随机盐值
    }
}
```

2. **元数据提取机制**
   - 基于JDBC DatabaseMetaData API提取元数据
   - 支持MySQL和DB2特定元数据查询
   - 实现增量元数据同步算法
   
```java
public class JdbcMetadataExtractor implements MetadataExtractor {
    
    public List<SchemaInfo> extractSchemas(Connection connection) {
        // 提取数据库模式信息
    }
    
    public List<TableInfo> extractTables(Connection connection, String schema) {
        // 提取表信息
    }
    
    public List<ColumnInfo> extractColumns(Connection connection, String schema, String table) {
        // 提取列信息
    }
    
    // 其他元数据提取方法...
}
```

3. **表关系推断算法**
   - 基于约定命名规则的关系推断
   - 基于列数据类型和索引的关系推断
   - 使用置信度评分机制
   
```java
public class RelationshipInferenceEngine {
    
    public List<TableRelationship> inferRelationships(String dataSourceId) {
        // 推断表关系的算法实现
    }
    
    private double calculateConfidence(TableInfo sourceTable, 
                                      ColumnInfo sourceColumn,
                                      TableInfo targetTable,
                                      ColumnInfo targetColumn) {
        // 计算关系置信度的逻辑
    }
}
```

### 3.2 查询构建模块

#### 3.2.1 关键领域模型

```java
// 保存的查询
public class SavedQuery {
    private String id;
    private String name;
    private String description;
    private String dataSourceId;
    private String sqlText;
    private List<QueryParameter> parameters;
    private boolean isPublic;
    private QueryType type;
    private int executionCount;
    private LocalDateTime lastExecutedAt;
    // 审计字段...
}

// 查询参数
public class QueryParameter {
    private String id;
    private String queryId;
    private String name;
    private String description;
    private ParameterType type;
    private String defaultValue;
    private boolean required;
    private String validationRule;
    // 审计字段...
}

// 查询执行记录
public class QueryExecution {
    private String id;
    private String queryId;
    private String versionId;
    private Map<String, Object> parameters;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private ExecutionStatus status;
    private String errorMessage;
    private long recordCount;
    private long executionTimeMs;
    // 审计字段...
}

// 查询执行计划
public class QueryPlan {
    private String id;
    private String queryId;
    private String executionId;
    private String planJson;        // 执行计划JSON
    private List<String> warnings;  // 潜在性能问题警告
    private List<String> suggestions; // 优化建议
    // 审计字段...
}
```

#### 3.2.2 接口定义

```java
// 查询服务接口
public interface QueryService {
    SavedQuery createQuery(QueryDTO dto);
    SavedQuery getQuery(String id);
    List<SavedQuery> getAllQueries(QueryFilter filter);
    SavedQuery updateQuery(String id, QueryDTO dto);
    void deleteQuery(String id);
    QueryResult executeQuery(String id, Map<String, Object> parameters);
    void cancelQuery(String executionId);
    List<QueryExecution> getQueryHistory(String id);
    QueryPlan analyzeQuery(String id);
}

// 查询执行接口
public interface QueryExecutor {
    QueryExecution execute(String dataSourceId, String sql, 
                          Map<String, Object> parameters, 
                          int timeout);
    void cancel(String executionId);
    ExecutionStatus getStatus(String executionId);
}

// 查询结果管理接口
public interface ResultManager {
    PagedResult getPagedResults(String executionId, int page, int size);
    boolean exportToCsv(String executionId, String filePath, ExportOptions options);
    void cacheResults(String executionId, QueryResult results);
    QueryResult getCachedResults(String executionId);
}
```

#### 3.2.3 实现细节

1. **SQL构建器**
   - 实现SqlBuilder接口，支持各种SQL语法
   - 支持条件、连接、分组、排序等SQL功能
   - 实现参数化查询防止SQL注入
   
```java
public class SqlBuilder {
    private StringBuilder sql = new StringBuilder();
    private List<Object> parameters = new ArrayList<>();
    
    public SqlBuilder select(String... columns) {
        // 构建SELECT子句
        return this;
    }
    
    public SqlBuilder from(String table) {
        // 构建FROM子句
        return this;
    }
    
    public SqlBuilder where(String condition, Object... params) {
        // 构建WHERE子句
        return this;
    }
    
    // 其他SQL构建方法...
    
    public PreparedStatement buildPreparedStatement(Connection conn) {
        // 创建并返回PreparedStatement
    }
}
```

2. **查询执行引擎**
   - 实现查询超时控制(默认30秒)
   - 支持查询取消功能
   - 实现资源限制和监控
   
```java
public class QueryExecutionEngine implements QueryExecutor {
    
    public QueryExecution execute(String dataSourceId, String sql, 
                                 Map<String, Object> parameters, 
                                 int timeout) {
        // 实现查询执行逻辑
    }
    
    public void cancel(String executionId) {
        // 实现查询取消逻辑
    }
    
    public ExecutionStatus getStatus(String executionId) {
        // 获取查询执行状态
    }
    
    private void checkQueryLimit(String userId) {
        // 检查用户查询频率限制
    }
}
```

3. **查询优化器**
   - 分析SQL执行计划
   - 提供查询优化建议
   - 识别潜在性能问题
   
```java
public class QueryOptimizer {
    
    public QueryPlan analyze(String dataSourceId, String sql) {
        // 分析SQL执行计划
    }
    
    public List<String> suggestOptimizations(QueryPlan plan) {
        // 根据执行计划提供优化建议
    }
    
    public List<String> identifyBottlenecks(QueryPlan plan) {
        // 识别性能瓶颈
    }
}
```

4. **结果管理器**
   - 实现结果分页处理
   - 实现结果导出功能
   - 使用游标和流式处理大结果集
   
```java
public class QueryResultManager implements ResultManager {
    
    public PagedResult getPagedResults(String executionId, int page, int size) {
        // 获取分页查询结果
    }
    
    public boolean exportToCsv(String executionId, String filePath, ExportOptions options) {
        // 导出结果到CSV
    }
    
    public void cacheResults(String executionId, QueryResult results) {
        // 缓存查询结果
    }
    
    public QueryResult getCachedResults(String executionId) {
        // 获取缓存结果
    }
}
```

### 3.3 NL2SQL模块

#### 3.3.1 关键领域模型

```java
// 自然语言查询
public class NLQuery {
    private String id;
    private String dataSourceId;
    private String naturalLanguage;
    private String generatedSql;
    private double confidence;
    private ProcessingStatus status;
    private String errorMessage;
    private String userId;
    private boolean hasFeedback;
    private boolean isSuccessful;
    // 审计字段...
}

// 查询上下文
public class QueryContext {
    private String id;
    private String userId;
    private String dataSourceId;
    private List<String> recentQueries;
    private Map<String, Object> contextData;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}

// 用户反馈
public class Feedback {
    private String id;
    private String nlQueryId;
    private boolean isPositive;
    private String comments;
    private String correctSql;
    private String userId;
    private LocalDateTime createdAt;
}
```

#### 3.3.2 接口定义

```java
// 自然语言查询服务
public interface NLQueryService {
    NLQueryResult processNaturalLanguageQuery(String dataSourceId, String naturalLanguage);
    List<String> suggestRefinements(String dataSourceId, String naturalLanguage);
    void submitFeedback(String queryId, boolean isAccepted, String feedback);
    List<NLQueryHistory> getQueryHistory(String userId);
}

// SQL生成服务
public interface SQLGeneratorService {
    String generateSQL(String naturalLanguage, QueryContext context);
    String optimizeSQL(String sql);
    boolean validateSQL(String sql);
    String explainSQL(String sql);
}

// 上下文管理服务
public interface ContextManagerService {
    QueryContext createContext(String userId, String dataSourceId);
    QueryContext updateContext(String contextId, Map<String, Object> data);
    List<String> getRelevantTables(String naturalLanguage, String dataSourceId);
    QueryContext getHistoricalContext(String userId, String dataSourceId);
}
```

#### 3.3.3 实现细节

1. **NL处理器**
   - 实现自然语言预处理
   - 使用OpenRouter API集成
   - 实现提示词工程最佳实践
   
```java
public class NLProcessor {
    
    public ProcessedQuery processQuery(String naturalLanguage, String dataSourceId) {
        // 预处理查询文本
    }
    
    public List<String> extractEntities(String naturalLanguage) {
        // 提取查询中的实体
    }
    
    public QueryIntent identifyIntent(String naturalLanguage) {
        // 识别查询意图
    }
    
    public String preprocessText(String naturalLanguage) {
        // 文本预处理
    }
}
```

2. **LLM提示工程**
   - 设计结构化的提示模板
   - 包含上下文信息和数据库结构
   - 优化生成SQL的准确性
   
```java
public class PromptEngineering {
    
    public String buildSystemPrompt(String dataSourceId) {
        // 构建系统提示
    }
    
    public String buildUserPrompt(String naturalLanguage, 
                                 List<TableInfo> relevantTables,
                                 QueryContext context) {
        // 构建用户提示
    }
    
    public String extractSqlFromResponse(String llmResponse) {
        // 从LLM响应中提取SQL
    }
}
```

3. **反馈学习机制**
   - 收集用户反馈
   - 调整查询处理策略
   - 记录成功和失败案例
   
```java
public class FeedbackLearner {
    
    public void recordFeedback(String queryId, boolean isPositive) {
        // 记录用户反馈
    }
    
    public void trainFromFeedback() {
        // 基于反馈调整模型
    }
    
    public double adjustConfidence(String naturalLanguage, double baseConfidence) {
        // 根据历史数据调整置信度
    }
    
    public List<String> getSuggestions(String naturalLanguage) {
        // 获取查询建议
    }
}
```

### 3.4 低代码集成模块

#### 3.4.1 关键领域模型

```java
// 低代码配置
public class LowCodeConfig {
    private String id;
    private String queryId;
    private String name;
    private String description;
    private String version;
    private FormConfig formConfig;
    private DisplayConfig displayConfig;
    private List<WebhookConfig> webhooks;
    // 审计字段...
}

// 表单配置
public class FormConfig {
    private String id;
    private String configId;
    private String layout;
    private List<FormSection> sections;
    private List<FormField> fields;
    private List<FormButton> buttons;
    // 审计字段...
}

// 展示配置
public class DisplayConfig {
    private String id;
    private String configId;
    private String type;
    private String title;
    private PaginationConfig pagination;
    private SortingConfig sorting;
    private List<ColumnConfig> columns;
    private List<OperationConfig> operations;
    // 审计字段...
}

// Webhook配置
public class WebhookConfig {
    private String id;
    private String configId;
    private String endpointUrl;
    private String secretKey;
    private List<String> eventTypes;
    private boolean active;
    // 审计字段...
}
```

#### 3.4.2 接口定义

```java
// 低代码服务
public interface LowCodeService {
    LowCodeConfig createConfig(String queryId, LowCodeConfigDTO dto);
    LowCodeConfig getConfig(String id);
    List<LowCodeConfig> getAllConfigs(ConfigFilter filter);
    LowCodeConfig updateConfig(String id, LowCodeConfigDTO dto);
    void deleteConfig(String id);
    String exportConfig(String id, String format);
}

// 表单生成服务
public interface FormGeneratorService {
    FormConfig generateForm(String queryId);
    FormConfig customizeForm(String formId, FormDTO dto);
    boolean validateFormConfig(FormConfig config);
    FormLayoutSuggestion suggestFormLayout(QueryDTO query);
}

// Webhook管理服务
public interface WebhookService {
    WebhookConfig registerWebhook(WebhookDTO dto);
    void triggerWebhook(String webhookId, Map<String, Object> eventData);
    WebhookActivityLog logWebhookActivity(String webhookId, WebhookEvent event);
    WebhookTestResult testWebhook(String webhookId);
}
```

#### 3.4.3 实现细节

1. **JSON协议处理器**
   - 定义低代码平台集成协议
   - 实现协议序列化和反序列化
   - 协议版本控制
   
```java
public class ProtocolHandler {
    
    public String formatMessage(Object data, String protocolVersion) {
        // 格式化消息为JSON
    }
    
    public <T> T parseMessage(String message, Class<T> type) {
        // 解析JSON消息
    }
    
    public boolean validateMessage(String message, String schemaVersion) {
        // 验证消息格式
    }
    
    public JsonSchema getProtocolSchema(String version) {
        // 获取协议模式定义
    }
}
```

2. **表单生成器**
   - 根据查询参数自动生成表单
   - 智能选择合适的输入控件
   - 支持表单验证规则
   
```java
public class FormGenerator {
    
    public FormConfig generateFormFromQuery(SavedQuery query) {
        // 从查询生成表单配置
    }
    
    public FormField createFieldForParameter(QueryParameter parameter) {
        // 为参数创建表单字段
    }
    
    public String suggestControlType(ParameterType type, 
                                     String name, 
                                     String defaultValue) {
        // 建议控件类型
    }
}
```

3. **Webhook管理器**
   - 实现Webhook注册和触发
   - 支持事件过滤
   - 实现消息签名验证
   
```java
public class WebhookManager {
    
    public WebhookConfig register(String configId, WebhookDTO dto) {
        // 注册Webhook
    }
    
    public WebhookResult trigger(String webhookId, WebhookEvent event) {
        // 触发Webhook
    }
    
    public String generateSignature(String payload, String secret) {
        // 生成消息签名
    }
    
    public boolean verifySignature(String payload, String signature, String secret) {
        // 验证消息签名
    }
}
```

### 3.5 版本控制模块

#### 3.5.1 关键领域模型

```java
// 查询版本
public class QueryVersion {
    private String id;
    private String queryId;
    private int versionNumber;
    private String sqlText;
    private List<ParameterDefinition> parameters;
    private String description;
    private String commitMessage;
    private boolean isCurrent;
    // 审计字段...
}

// API版本
public class ApiVersion {
    private String id;
    private String queryId;
    private String version;
    private boolean isActive;
    private String apiPath;
    private ApiConfig config;
    // 审计字段...
}

// 变更历史
public class ChangeHistory {
    private String id;
    private String resourceId;
    private String resourceType;
    private ChangeType changeType;
    private String beforeValue;
    private String afterValue;
    private String changeReason;
    // 审计字段...
}

// 协作锁
public class CollaborationLock {
    private String id;
    private String resourceId;
    private String resourceType;
    private String lockedBy;
    private LocalDateTime lockedAt;
    private LocalDateTime expiresAt;
    // 审计字段...
}
```

#### 3.5.2 接口定义

```java
// 版本控制服务
public interface VersionControlService {
    QueryVersion createQueryVersion(String queryId, VersionDTO dto);
    List<QueryVersion> getQueryVersions(String queryId);
    QueryVersion getQueryVersion(String queryId, String versionId);
    SavedQuery rollbackToVersion(String queryId, String versionId);
    DiffResult compareVersions(String versionId1, String versionId2);
}

// 协作服务
public interface CollaborationService {
    CollaborationLock lockResource(String resourceType, String resourceId, String userId);
    void unlockResource(String resourceType, String resourceId, String userId);
    CollaborationStatus getEditStatus(String resourceType, String resourceId);
    List<ChangeHistory> getChangeHistory(String resourceType, String resourceId);
}
```

#### 3.5.3 实现细节

1. **Git风格版本管理**
   - 实现类似Git的版本模型
   - 支持版本差异比较
   - 支持版本回滚
   
```java
public class VersionManager {
    
    public QueryVersion createVersion(String queryId, VersionDTO dto) {
        // 创建新版本
    }
    
    public QueryVersion getVersion(String versionId) {
        // 获取版本信息
    }
    
    public List<QueryVersion> listVersions(String queryId) {
        // 列出所有版本
    }
    
    public SavedQuery rollbackToVersion(String queryId, String versionId) {
        // 回滚到指定版本
    }
}
```

2. **差异比较引擎**
   - 实现文本差异比较算法
   - 生成可视化差异报告
   - 支持结构化差异比较
   
```java
public class DiffGenerator {
    
    public DiffResult compareSQLVersions(String versionId1, String versionId2) {
        // 比较SQL版本差异
    }
    
    public String visualizeDiff(String text1, String text2) {
        // 生成可视化差异
    }
    
    public ChangeSummary getChangeSummary(String versionId1, String versionId2) {
        // 获取变更摘要
    }
}
```

3. **协作编辑控制**
   - 实现资源锁定机制
   - 支持锁定超时和自动释放
   - 记录编辑状态和冲突解决
   
```java
public class CollaborationManager {
    
    public CollaborationLock lockResource(String resourceType, String resourceId, String userId) {
        // 锁定资源
    }
    
    public void unlockResource(String resourceType, String resourceId, String userId) {
        // 释放锁定
    }
    
    public CollaborationStatus getEditStatus(String resourceType, String resourceId) {
        // 获取编辑状态
    }
    
    public ConflictResolution mergeChanges(String resourceId, String changeSetId) {
        // 合并变更
    }
}
```

### 3.6 共享基础设施

#### 3.6.1 数据访问服务

```java
public interface DataAccessService {
    <T> T executeQuery(String sql, Object[] params, ResultSetExtractor<T> extractor);
    int executeUpdate(String sql, Object[] params);
    List<Map<String, Object>> queryForList(String sql, Object[] params);
    <T> List<T> queryForList(String sql, Object[] params, Class<T> elementType);
}
```

#### 3.6.2 缓存服务

```java
public interface CacheService {
    <T> T get(String key, Class<T> type);
    <T> void put(String key, T value, Duration ttl);
    boolean remove(String key);
    void clear(String pattern);
    <T> T getOrCompute(String key, Function<String, T> computeFunction, Duration ttl);
}
```

#### 3.6.3 安全服务

```java
public interface SecurityService {
    String encryptData(String data, String salt);
    String decryptData(String encryptedData, String salt);
    String hashPassword(String password);
    boolean verifyPassword(String password, String hashedPassword);
    String generateToken(Map<String, Object> claims);
    Map<String, Object> validateToken(String token);
}
```

#### 3.6.4 集成服务

```java
public interface IntegrationService {
    <T> T callExternalAPI(String url, String method, Object requestBody, Class<T> responseType);
    void registerCallback(String eventType, CallbackHandler handler);
    Connection connectToDataSource(DataSource dataSource);
    LLMResponse callLLMService(LLMRequest request);
}
```

## 4. 数据模型设计

### 4.1 数据库表设计

系统使用以下命名规范：
- 所有表名以`tbl_`开头
- 使用下划线分隔单词
- 主键使用UUID (varchar(36))
- 所有表包含created_at, modified_at, created_by, modified_by和nonce字段

#### 数据源相关表

| 表名 | 主要字段 | 说明 |
|------|---------|------|
| tbl_data_source | id, name, type, host, port, database_name, username, encrypted_password, encryption_salt, connection_properties, active | 存储数据源连接信息 |
| tbl_schema_info | id, data_source_id, name, description | 存储数据库模式信息 |
| tbl_table_info | id, schema_id, name, type, description, estimated_row_count | 存储表信息 |
| tbl_column_info | id, table_id, name, data_type, ordinal_position, nullable, is_primary_key | 存储列信息 |
| tbl_index_info | id, table_id, name, type, is_unique | 存储索引信息 |
| tbl_foreign_key | id, name, source_table_id, target_table_id, update_rule, delete_rule | 存储外键信息 |
| tbl_table_relation | id, source_table_id, target_table_id, relation_type, is_inferred, confidence | 存储推断的表关系 |
| tbl_metadata_version | id, data_source_id, description, snapshot_location | 存储元数据版本 |
| tbl_sync_job | id, data_source_id, type, status, progress, error_message | 存储同步作业信息 |

#### 查询相关表

| 表名 | 主要字段 | 说明 |
|------|---------|------|
| tbl_saved_query | id, name, description, data_source_id, sql_text, created_by, is_public | 存储保存的查询 |
| tbl_query_parameter | id, query_id, name, type, default_value, required | 存储查询参数定义 |
| tbl_query_version | id, query_id, version_number, sql_text, parameters, description | 存储查询版本 |
| tbl_query_execution | id, query_id, version_id, parameters, start_time, end_time, status, error_message | 存储查询执行记录 |
| tbl_query_history | id, user_id, query_text, execution_time, record_count, created_at | 存储查询历史 |

#### 低代码集成相关表

| 表名 | 主要字段 | 说明 |
|------|---------|------|
| tbl_lowcode_config | id, query_id, name, description, version | 存储低代码配置 |
| tbl_form_config | id, config_id, layout, sections, fields, buttons | 存储表单配置 |
| tbl_display_config | id, config_id, type, title, pagination, sorting, columns, operations | 存储展示配置 |
| tbl_webhook_config | id, config_id, endpoint_url, secret_key, event_types, active | 存储Webhook配置 |
| tbl_api_version | id, query_id, version, is_active, api_path | 存储API版本 |

#### 自然语言查询相关表

| 表名 | 主要字段 | 说明 |
|------|---------|------|
| tbl_nl_query | id, data_source_id, natural_language, generated_sql, confidence, status, user_id | 存储自然语言查询 |
| tbl_nl_feedback | id, nl_query_id, is_positive, comments, correct_sql, user_id | 存储用户反馈 |
| tbl_query_context | id, user_id, data_source_id, context_data, created_at, expires_at | 存储查询上下文 |

#### 协作相关表

| 表名 | 主要字段 | 说明 |
|------|---------|------|
| tbl_collaboration_lock | id, resource_id, resource_type, locked_by, locked_at, expires_at | 存储协作锁 |
| tbl_change_history | id, resource_id, resource_type, change_type, before_value, after_value | 存储变更历史 |
| tbl_user_preference | id, user_id, preference_type, preference_key, preference_value | 存储用户偏好 |

### 4.2 索引策略

- 使用唯一索引(前缀u_idx_)保证关键字段唯一性
- 使用普通索引(前缀idx_)优化查询性能
- 索引名包含列名以便于识别用途
- 对频繁查询的字段创建索引
- 对外键关系创建索引

示例索引定义：

```sql
-- 数据源表索引
CREATE UNIQUE INDEX u_idx_datasource_name ON tbl_data_source(name);
CREATE INDEX idx_datasource_type ON tbl_data_source(type);

-- 表信息索引
CREATE INDEX idx_table_schema_id ON tbl_table_info(schema_id);
CREATE UNIQUE INDEX u_idx_table_schema_name ON tbl_table_info(schema_id, name);

-- 列信息索引
CREATE INDEX idx_column_table_id ON tbl_column_info(table_id);
CREATE UNIQUE INDEX u_idx_column_table_name ON tbl_column_info(table_id, name);

-- 查询相关索引
CREATE INDEX idx_query_datasource ON tbl_saved_query(data_source_id);
CREATE INDEX idx_query_created_by ON tbl_saved_query(created_by);
CREATE INDEX idx_query_execution_query_id ON tbl_query_execution(query_id);
CREATE INDEX idx_query_history_user_id ON tbl_query_history(user_id);
```

### 4.3 数据迁移策略

使用Flyway进行数据库版本控制和迁移：

1. 创建初始数据库结构
2. 增量更新数据库结构
3. 支持回滚和升级路径
4. 版本控制确保环境一致性

迁移脚本目录：`src/main/resources/db/migration`
命名规范：`V{version}__{description}.sql`

## 5. 安全实施

### 5.1 数据源凭证加密

- 使用AES-256 CBC模式加密算法
- 主密钥存储在环境变量或配置文件中
- 每个数据源密码使用唯一盐值
- 数据库中只存储密文和盐值

实现示例：

```java
@Service
public class AESCredentialEncryptor implements CredentialEncryptor {
    @Value("${datasource.encryption.key}")
    private String masterKey;
    
    @Override
    public String encrypt(String plaintext, String salt) {
        // 基于主密钥和盐值派生加密密钥
        SecretKey key = deriveKey(masterKey, salt);
        
        // 执行AES加密
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(salt.getBytes()));
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        
        // 返回Base64编码的密文
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }
    
    @Override
    public String decrypt(String ciphertext, String salt) {
        // 解密实现...
    }
    
    @Override
    public String generateSalt() {
        // 生成随机盐值...
    }
    
    private SecretKey deriveKey(String masterKey, String salt) {
        // 使用PBKDF2派生密钥...
    }
}
```

### 5.2 SQL注入防护

- 使用参数化查询，避免直接字符串拼接
- 输入验证和清理，过滤潜在危险字符
- 使用MyBatis的预编译功能
- 最小权限原则，数据源连接使用只读权限

### 5.3 API安全

- JWT认证和授权
- 请求频率限制(每分钟10次查询)
- API密钥管理和轮换
- HTTPS传输加密
- 审计日志记录所有API调用

### 5.4 数据掩码

- 支持指定敏感列的掩码规则
- 掩码规则包括：完全掩码、部分掩码、字符替换等
- 在数据导出和API响应中应用一致的掩码规则

## 6. 缓存策略

### 6.1 多级缓存设计

1. **应用内缓存**
   - 本地内存缓存(Caffeine)
   - 适用于频繁使用的小型静态数据
   - 短期缓存，默认TTL 5分钟

2. **分布式缓存**
   - Redis实现
   - 存储共享数据和跨实例数据
   - 可配置的TTL和缓存策略

### 6.2 缓存模式

| 缓存类型 | 缓存键模式 | 过期时间 | 刷新策略 | 实现方式 |
|---------|-----------|---------|----------|---------|
| 元数据缓存 | metadata:{dataSourceId}:{objectType} | 24小时 | 元数据同步时刷新 | Redis |
| 表关系缓存 | relation:{dataSourceId} | 24小时 | 关系更新时刷新 | Redis |
| 查询结果缓存 | query:result:{queryId}:{paramHash} | 可配置(默认10分钟) | 按需刷新，数据变更时失效 | Redis |
| 查询分析缓存 | query:analysis:{queryId} | 1小时 | 查询更新时刷新 | Redis |
| 低代码配置缓存 | lowcode:config:{configId} | 1小时 | 配置更新时刷新 | Redis |
| 常用查询缓存 | query:frequent:{userId} | 7天 | 查询频率变化时更新 | Redis |
| 用户偏好缓存 | user:preference:{userId} | 30天 | 用户修改时更新 | Redis |

### 6.3 缓存实现

```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        // 配置Redis缓存管理器
    }
    
    @Bean
    public RedisCacheConfiguration defaultCacheConfig() {
        // 配置默认缓存策略
    }
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 配置RedisTemplate
    }
}

@Service
public class RedisCacheService implements CacheService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Override
    public <T> T get(String key, Class<T> type) {
        // 从Redis获取缓存
    }
    
    @Override
    public <T> void put(String key, T value, Duration ttl) {
        // 添加缓存到Redis
    }
    
    @Override
    public boolean remove(String key) {
        // 移除缓存
    }
    
    @Override
    public void clear(String pattern) {
        // 清除匹配模式的缓存
    }
}
```

## 7. 性能优化

### 7.1 查询执行优化

- 实现超时控制，默认30秒
- 使用连接池管理数据源连接
- 分析并优化SQL执行计划
- 长时间查询异步执行
- 查询结果流式处理

### 7.2 数据库访问优化

- 使用MyBatis作为ORM框架
- 动态SQL构建以优化查询
- 使用批处理提高大批量操作性能
- 适当使用存储过程处理复杂查询

### 7.3 应用性能监控

- 收集关键性能指标
- 使用Micrometer + Prometheus监控
- 性能日志和警报系统
- 定期性能分析和优化

## 8. 国际化与本地化

- 支持中英文界面
- 错误消息和提示的国际化
- 日期时间格式本地化
- 数字和货币格式本地化

## 9. 测试策略

### 9.1 单元测试

- 使用JUnit 5和Mockito
- 测试覆盖率目标：80%
- 测试领域服务和应用服务
- 测试数据库仓储实现

### 9.2 集成测试

- 测试模块间集成
- 使用测试容器进行数据库测试
- API端到端测试
- 使用模拟外部服务进行集成测试

### 9.3 性能测试

- 负载测试验证系统容量
- 压力测试识别系统瓶颈
- 耐久测试验证长期稳定性
- 模拟真实用户负载的性能基线测试

## 10. 监控与运维

### 10.1 监控指标

- API响应时间和吞吐量
- 数据源连接状态
- 查询执行时间
- 缓存命中率
- JVM指标(内存、GC等)
- 系统资源使用率

### 10.2 日志策略

- 使用SLF4J + Logback
- 日志分级：ERROR, WARN, INFO, DEBUG, TRACE
- 按日期和大小进行日志轮换
- 结构化日志记录关键操作
- 异常堆栈完整记录

### 10.3 健康检查

- 提供系统健康检查端点
- 数据源连接状态监控
- Redis连接状态监控
- 查询执行状态监控
- 系统资源监控

## 11. 部署策略

### 11.1 本地部署

- 支持jar包部署
- 支持war包部署到Tomcat等容器
- 提供完整安装和配置指南
- 支持数据库迁移脚本

### 11.2 容器化部署

- 提供Docker镜像和Dockerfile
- Docker Compose配置一键部署
- Kubernetes部署配置
- 支持水平扩展
- 持久化存储配置

### 11.3 CI/CD集成

- Maven构建配置
- 自动化测试集成
- 镜像构建流程
- 基于环境的配置管理
- 自动化部署脚本