# 数据管理与查询系统 - 技术规范

本文档提供数据管理与查询系统的详细技术规范，作为开发团队实施的指导。

## 1. 技术栈规范

### 1.1 后端技术栈

| 组件       | 技术选择        | 版本   | 用途                     |
|------------|-----------------|--------|--------------------------|
| 开发语言   | Java            | 17+    | 核心开发语言             |
| 应用框架   | Spring Boot     | 3.x    | 应用开发框架             |
| 持久层框架 | MyBatis         | 3.x    | ORM框架                  |
| 构建工具   | Maven           | 3.8+   | 项目构建和依赖管理         |
| 系统数据库 | MySQL           | 8.0+   | 系统元数据存储             |
| 缓存系统   | Redis           | 6.0+   | 缓存服务                 |
| API文档    | Springdoc-OpenAPI | 2.x    | API文档生成              |
| 单元测试   | JUnit           | 5.x    | 单元测试框架             |
| 日志框架   | SLF4J + Logback | -      | 日志记录                 |
| 连接池     | HikariCP        | -      | 数据库连接池             |
| JSON处理   | Jackson         | 2.x    | JSON序列化/反序列化        |
| HTTP客户端 | OkHttp          | 4.x    | 外部API调用              |

### 1.2 前端技术栈

| 组件         | 技术选择      | 版本   | 用途             |
|--------------|---------------|--------|------------------|
| 标记语言     | HTML          | 5      | 页面结构         |
| 样式框架     | Tailwind CSS  | 3.x    | 样式和布局       |
| JavaScript框架 | Vue.js        | 3.x    | 前端框架         |
| 图表库       | ECharts       | 5.x    | 数据可视化       |
| 图标库       | Font Awesome  | 6.x    | 图标             |
| HTTP客户端   | Axios         | -      | API调用          |
| 状态管理     | Pinia         | -      | 状态管理         |
| 表单验证     | Vee-Validate  | -      | 表单验证         |
| UI组件库    | Headless UI   | -      | 无样式UI组件     |
| 构建工具     | Vite          | -      | 前端构建工具     |

## 2. 模块实现规范

### 2.1 项目遵循DDD的4层架构

系统采用DDD（领域驱动设计）的四层架构，各层职责如下：

1.  **Domain层**：包含核心业务逻辑和领域模型
    *   实体类（Entity）
    *   值对象（Value Object）
    *   领域服务（Domain Service）
    *   仓储接口（Repository Interface）

2.  **Application层**：协调和编排业务流程
    *   应用服务（Application Service）
    *   数据传输对象（DTO）
    *   命令和查询处理

3.  **Infrastructure层**：提供技术实现
    *   仓储实现（Repository Implementation）
    *   外部服务集成
    *   技术组件实现
    *   持久化映射

4.  **Facade层**：对外提供接口
    *   REST控制器（Controller）
    *   API模型（API Model）
    *   请求/响应转换
    *   跨域资源共享（CORS）配置

### 2.2 包结构规范

项目包结构应遵循以下规范：

```
com.example.datainsight/
  ├── domain/                  # 领域层
  │   ├── model/               # 领域模型
  │   ├── service/             # 领域服务
  │   └── repository/          # 仓储接口
  │
  ├── application/             # 应用层
  │   ├── service/             # 应用服务
  │   ├── dto/                 # 数据传输对象
  │   └── mapper/              # DTO映射器
  │
  ├── infrastructure/          # 基础设施层
  │   ├── repository/          # 仓储实现
  │   ├── db/                  # 数据库相关
  │   ├── cache/               # 缓存实现
  │   ├── external/            # 外部服务
  │   │   ├── llm/             # LLM服务集成
  │   │   └── datasource/      # 数据源适配器
  │   ├── config/              # 配置类
  │   ├── security/            # 安全相关
  │   └── util/                # 工具类
  │
  └── facade/                  # 外观层
      ├── rest/                # REST控制器
      ├── model/               # API模型
      ├── converter/           # 转换器
      └── advice/              # 控制器通知
```

## 3. 核心组件规范

### 3.1 数据源管理模块

#### 3.1.1 数据库表设计规范

*   表名采用 `tbl` 前缀，例如 `tbl_datasource`, `tbl_schema_info`
*   唯一索引采用 `u_idx` 前缀，例如 `u_idx_datasource_name`
*   其他索引采用 `idx` 前缀，例如 `idx_datasource_type`
*   历史记录类的表，需要记录创建时间`created_at`, 创建人`created_by`
*   其他实体表，需要记录乐观锁`nonce`、创建时间`created_at`, 创建人`created_by`, 最后修改时间`modified_at`, 最后修改人`modified_by`
*   为实现数据在不同环境间平滑的导出导入，主键采用uuid并在编码时注意唯一性检查

#### 3.1.2 MySQL适配器实现规范

MySQL适配器应实现以下功能：

1.  使用JDBC驱动连接MySQL数据库
2.  支持从information\_schema获取元数据
3.  实现参数化查询执行
4.  支持事务管理
5.  提供连接池管理
6.  实现超时控制

#### 3.1.3 DB2适配器实现规范

DB2适配器应实现以下功能：

1.  使用DB2 JDBC驱动连接数据库
2.  支持从系统表获取元数据
3.  处理DB2特有的SQL语法特性
4.  实现参数化查询执行
5.  提供连接池管理
6.  实现超时控制

#### 3.1.4 元数据提取器规范

元数据提取器应遵循以下规范：

1.  支持增量和全量元数据提取
2.  实现并发提取以提高性能
3.  提供进度报告机制
4.  实现错误恢复机制
5.  支持过滤不需要的对象
6.  提供元数据版本管理

### 3.2 查询构建与执行模块

#### 3.2.1 查询构建器规范

查询构建器应支持以下功能：

1.  **基本查询功能**：
    *   SELECT语句构建
    *   WHERE条件表达式
    *   JOIN操作（INNER, LEFT, RIGHT, FULL）
    *   GROUP BY分组
    *   ORDER BY排序
    *   HAVING过滤
    *   LIMIT和OFFSET分页

2.  **支持的条件操作符**：
    *   等于（=）、不等于（!=, <>）
    *   大于（>）、小于（<）
    *   大于等于（>=）、小于等于（<=）
    *   LIKE、NOT LIKE 模糊匹配
    *   IN、NOT IN 集合匹配
    *   BETWEEN、NOT BETWEEN 范围匹配
    *   IS NULL、IS NOT NULL 空值检查
    *   AND、OR、NOT 逻辑组合

3.  **特殊功能**：
    *   子查询支持
    *   公共表表达式（CTE）支持
    *   聚合函数（COUNT, SUM, AVG, MIN, MAX）
    *   窗口函数支持
    *   数据库方言适配

#### 3.2.2 查询执行引擎规范

查询执行引擎应实现以下功能：

1.  参数化查询处理
2.  查询超时控制（默认30秒）
3.  大结果集分页处理
4.  执行计划分析
5.  性能监控和统计
6.  异步查询支持
7.  结果集缓存

#### 3.2.3 表关系管理规范

表关系管理应支持以下功能：

1.  外键关系自动识别
2.  基于命名规则的关系推断
3.  基于数据分析的关系推断
4.  关系置信度评分
5.  手动关系定义与编辑
6.  关系持久化存储
7.  关系导入/导出

### 3.3 NL2SQL模块

#### 3.3.1 OpenRouter LLM集成规范

OpenRouter LLM集成应遵循以下规范：

1.  实现可配置的模型选择
2.  使用非阻塞API调用
3.  实现请求重试和错误处理
4.  提供超时控制
5.  实现API密钥安全管理
6.  支持上下文优化和Prompt工程
7.  结果评分与验证

#### 3.3.2 查询转换器规范

查询转换器应实现以下功能：

1.  将LLM生成的SQL解析为结构化对象
2.  验证SQL语法和安全性
3.  转换SQL方言适应不同数据库
4.  处理常见的SQL错误和修正
5.  提供转换信心度评分
6.  支持逐步求精的转换过程

#### 3.3.3 查询增强器规范

查询增强器应实现以下功能：

1.  将数据库元数据注入到LLM上下文
2.  提供表关系信息到提示中
3.  基于历史查询优化提示
4.  上下文压缩以适应模型限制
5.  结果验证和改进建议

### 3.4 低代码集成模块

#### 3.4.1 JSON协议规范

系统与低代码平台交互的JSON协议应遵循以下规范：

**查询配置JSON结构**：

```json
{
  "version": "1.0",
  "queryId": "string",
  "config": {
    "form": {
      "layout": "string",
      "fields": [
        {
          "id": "string",
          "type": "string",
          "label": "string",
          "required": boolean,
          "defaultValue": "any",
          "validation": {
            "key": "value"
          },
          "options": []
        }
      ]
    },
    "display": {
      "type": "string",
      "columns": [
        {
          "field": "string",
          "header": "string",
          "width": number,
          "sortable": boolean,
          "format": "string",
          "mask": "string",
          "align": "string"
        }
      ],
      "operations": [
        {
          "type": "string",
          "label": "string",
          "action": "string",
          "icon": "string",
          "condition": "string"
        }
      ],
      "pagination": {
        "enabled": boolean,
        "pageSize": number,
        "pageSizeOptions": []
      },
      "sorting": {
        "defaultField": "string",
        "defaultOrder": "string"
      }
    }
  }
}
```

**查询执行JSON结构**：

```json
{
  "version": "1.0",
  "queryId": "string",
  "parameters": {
    "key": "value"
  },
  "pagination": {
    "page": number,
    "pageSize": number
  },
  "sorting": {
    "field": "string",
    "order": "string"
  }
}
```

**查询结果JSON结构**：

```json
{
  "version": "1.0",
  "queryId": "string",
  "success": boolean,
  "timestamp": "string",
  "data": {
    "items": [],
    "total": number
  },
  "error": {
    "code": "string",
    "message": "string"
  },
  "pagination": {
    "page": number,
    "pageSize": number,
    "totalPages": number
  }
}
```

#### 3.4.2 数据类型映射规范

系统应实现以下数据类型映射规则：

| 数据库类型      | Java类型   | UI组件类型   | 可用验证规则                                  |
|---------------|------------|--------------|------------------------------------------------|
| VARCHAR, CHAR | String     | text         | required, minLength, maxLength, pattern         |
| TEXT, CLOB    | String     | textarea     | required, minLength, maxLength                  |
| INT, SMALLINT | Integer    | number       | required, min, max                               |
| BIGINT        | Long       | number       | required, min, max                               |
| DECIMAL, NUMERIC | BigDecimal | number       | required, min, max, precision                    |
| DATE          | LocalDate  | date-picker  | required, minDate, maxDate                       |
| TIME          | LocalTime  | time-picker  | required                                         |
| TIMESTAMP, DATETIME | LocalDateTime | datetime-picker | required, minDateTime, maxDateTime                |
| BOOLEAN, BIT  | Boolean    | checkbox, toggle | required                                         |
| ENUM          | String     | select, radio | required, options                                |
| BLOB, BINARY  | byte[]     | file-upload  | required, maxSize, fileTypes                      |

#### 3.4.3 表单生成器规范

表单生成器应实现以下功能：

1.  基于查询参数自动生成表单定义
2.  支持自定义表单布局和分组
3.  实现必填字段验证
4.  支持条件字段显示/隐藏
5.  生成表单状态管理代码
6.  支持字段间依赖关系
7.  提供默认值处理

#### 3.4.4 显示配置生成器规范

显示配置生成器应实现以下功能：

1.  基于查询结果自动生成表格定义
2.  支持自定义列属性（宽度、排序等）
3.  实现数据格式化规则
4.  支持敏感数据掩码处理
5.  提供操作列配置
6.  支持分页、排序、筛选
7.  提供多种视图模式（表格、卡片等）

### 3.5 数据访问层

#### 3.5.1 仓储接口规范

系统核心实体的仓储接口应遵循以下模式：

```java
public interface EntityRepository<T, ID> {
    /**
     * 保存实体
     */
    T save(T entity);

    /**
     * 根据ID查找实体
     */
    Optional<T> findById(ID id);

    /**
     * 查找所有实体
     */
    List<T> findAll();

    /**
     * 分页查询
     */
    Page<T> findAll(Pageable pageable);

    /**
     * 条件查询
     */
    List<T> findByCriteria(Criteria criteria);

    /**
     * 删除实体
     */
    void delete(T entity);

    /**
     * 根据ID删除实体
     */
    void deleteById(ID id);

    /**
     * 检查实体是否存在
     */
    boolean existsById(ID id);

    /**
     * 获取实体数量
     */
    long count();
}
```

#### 3.5.2 缓存管理规范

缓存管理应遵循以下规范：

1.  使用Redis作为分布式缓存
2.  实现缓存键生成策略
3.  支持TTL过期策略
4.  实现缓存更新和失效策略
5.  提供缓存统计和监控
6.  支持缓存预热机制
7.  实现缓存穿透和缓存击穿防护

#### 3.5.3 数据源连接池规范

数据源连接池应实现以下功能：

1.  使用HikariCP实现连接池
2.  支持连接池大小动态调整
3.  实现空闲连接管理
4.  提供连接验证和重连机制
5.  实现连接超时控制
6.  提供连接池监控和统计
7.  支持多数据源配置

## 4. API接口规范

### 4.1 REST API设计规范

系统REST API应遵循以下规范：

1.  **URL命名**：
    *   使用名词而非动词
    *   使用小写字母和连字符
    *   使用复数表示资源集合
    *   使用URL参数传递查询条件

2.  **HTTP方法使用**：
    *   GET：查询资源
    *   POST：创建资源
    *   PUT：完整更新资源
    *   PATCH：部分更新资源
    *   DELETE：删除资源

3.  **状态码使用**：
    *   200 OK：成功的GET、PUT、PATCH
    *   201 Created：成功的POST
    *   204 No Content：成功的DELETE
    *   400 Bad Request：客户端错误
    *   401 Unauthorized：未认证
    *   403 Forbidden：权限不足
    *   404 Not Found：资源不存在
    *   409 Conflict：资源冲突
    *   422 Unprocessable Entity：验证失败
    *   500 Internal Server Error：服务器错误

4.  **版本控制**：
    *   在URL中包含版本号：`/api/v1/resources`
    *   主版本号表示不兼容变更
    *   支持至少前一个主版本的API

5.  **响应格式**：
    *   使用JSON格式
    *   包含状态信息
    *   提供分页元数据
    *   统一的错误响应格式

6.  **安全性**：
    *   仅使用HTTPS
    *   实现请求限流
    *   验证所有输入
    *   实施CORS策略

### 4.2 API文档规范

API文档应通过Springdoc-OpenAPI自动生成，并遵循以下规范：

1.  所有API端点都有描述性注释
2.  包含请求和响应模型的详细说明
3.  提供示例请求和响应
4.  说明所有可能的错误码和响应
5.  提供认证和授权信息
6.  包含API版本信息
7.  提供交互式API测试功能

### 4.3 错误处理规范

系统错误处理应遵循以下规范：

1.  **统一错误响应格式**：

```json
{
  "status": 400,
  "code": "VALIDATION_ERROR",
  "message": "请求验证失败",
  "details": [
    {
      "field": "username",
      "error": "用户名不能为空"
    }
  ],
  "timestamp": "2025-03-15T12:34:56.789Z",
  "path": "/api/users"
}
```

2.  **错误代码规范**：
    *   业务错误：`BUSINESS_*`
    *   验证错误：`VALIDATION_*`
    *   安全错误：`SECURITY_*`
    *   系统错误：`SYSTEM_*`
    *   数据错误：`DATA_*`
    *   集成错误：`INTEGRATION_*`

3.  **异常处理**：
    *   使用全局异常处理器
    *   将所有未捕获异常映射为适当响应
    *   记录详细错误日志
    *   隐藏内部错误详情，仅返回安全的错误信息

## 5. 安全实现规范

### 5.1 数据源凭证加密

数据源凭证加密应遵循以下规范：

1.  **加密算法**：
    *   使用AES-256 GCM模式
    *   每个密码单独加盐
    *   加密密钥安全存储

2.  **密钥管理**：
    *   加密密钥不直接存储在配置文件中
    *   支持基于环境变量的密钥配置
    *   实现密钥轮换机制

3.  **加密实现**：

```java
public class CredentialEncryptor {
    private final SecretKey secretKey;

    /**
     * 加密凭证
     */
    public String encrypt(String plainText, String salt) {
        // 使用AES-256 GCM模式加密
    }

    /**
     * 解密凭证
     */
    public String decrypt(String encryptedText, String salt) {
        // 解密
    }

    /**
     * 生成随机盐
     */
    public String generateSalt() {
        // 生成随机盐
    }
}
```

### 5.2 SQL注入防护

SQL注入防护应实现以下机制：

1.  使用参数化查询（PreparedStatement）
2.  输入验证和转义
3.  使用ORM框架的安全查询机制
4.  实施白名单验证SQL关键字
5.  在日志中隐藏敏感SQL参数
6.  对所有用户输入进行验证

### 5.3 敏感数据处理

敏感数据处理应遵循以下规范：

1.  **数据掩码**：
    *   支持多种掩码模式（全部、部分、自定义）
    *   掩码规则可配置
    *   在日志中自动掩码敏感数据

2.  **实现示例**：

```java
public class DataMasker {
    /**
     * 应用掩码规则
     */
    public String mask(String data, MaskRule rule) {
        switch (rule.getType()) {
            case FULL:
                return "******";
            case PARTIAL:
                // 保留前N后M字符
                return maskPartial(data, rule.getKeepStart(), rule.getKeepEnd());
            case CUSTOM:
                // 应用自定义掩码规则
                return applyCustomRule(data, rule.getPattern());
            default:
                return data;
        }
    }
}
```

## 6. 前端实现规范

### 6.1 Vue.js组件规范

Vue.js组件应遵循以下规范：

1.  **组件文件结构**：
    *   每个组件一个文件
    *   使用单文件组件格式（.vue）
    *   使用组合式API（Composition API）

2.  **组件命名**：
    *   使用PascalCase命名组件
    *   使用前缀区分不同类型组件（如Base、App、The）

3.  **组件样式**：
    *   使用scoped CSS隔离样式
    *   使用Tailwind CSS工具类
    *   提取公共样式到单独的文件

4.  **组件通信**：
    *   Props向下传递数据
    *   事件向上传递数据
    *   使用依赖注入共享数据
    *   使用Pinia管理全局状态

### 6.2 数据可视化实现规范

数据可视化应遵循以下规范：

1.  **图表组件化**：
    *   每种图表类型一个组件
    *   支持响应式调整
    *   提供通用的数据格式转换

2.  **使用ECharts的最佳实践**：
    *   主题定制
    *   按需加载图表组件
    *   实现图表事件交互
    *   提供图表导出功能

3.  **图表类型支持**：
    *   柱状图、折线图、饼图
    *   散点图、热力图
    *   树图、雷达图
    *   复合图表类型

### 6.3 UI组件实现规范

UI组件应遵循以下规范：

1.  **表格组件**：
    *   支持分页、排序、筛选
    *   自定义列渲染
    *   行选择和行操作
    *   虚拟滚动优化大数据集

2.  **表单组件**：
    *   统一的验证机制
    *   动态表单生成
    *   复杂输入控件支持
    *   表单状态管理

3.  **查询构建器UI**：
    *   拖拽式界面
    *   条件表达式构建
    *   实时SQL预览
    *   查询模板保存和加载

## 7. 测试规范

### 7.1 单元测试规范

单元测试应遵循以下规范：

1.  使用JUnit 5作为测试框架
2.  编写涵盖所有核心业务逻辑的测试
3.  使用Mockito模拟依赖
4.  遵循AAA模式（Arrange-Act-Assert）
5.  针对边界条件和异常情况编写测试
6.  测试覆盖率目标至少80%

### 7.2 集成测试规范

集成测试应遵循以下规范：

1.  使用TestContainers提供测试环境
2.  编写涵盖关键集成点的测试
3.  使用Spring Boot Test框架
4.  针对API端点编写测试
5.  测试数据源适配器与真实数据库交互
6.  模拟外部服务依赖

### 7.3 前端测试规范

前端测试应遵循以下规范：

1.  使用Vitest作为测试框架
2.  使用Vue Test Utils测试组件
3.  编写组件单元测试
4.  编写关键用户流程的端到端测试
5.  测试覆盖率目标至少70%

## 8. 部署规范

### 8.1 本地部署规范

系统本地部署应遵循以下规范：

1.  **部署环境要求**：
    *   操作系统：Linux/Windows Server
    *   JDK 17+
    *   MySQL 8.0+
    *   Redis 6.0+
    *   Nginx（可选）

2.  **部署包结构**：
    *   后端服务JAR包
    *   前端静态资源包
    *   配置文件模板
    *   数据库初始化脚本
    *   部署指南文档

3.  **配置管理**：
    *   使用外部配置文件
    *   环境变量覆盖默认配置
    *   敏感配置加密存储
    *   提供配置校验机制

### 8.2 容器化部署规范

系统容器化部署应遵循以下规范：

1.  **Docker镜像**：
    *   基于官方JDK镜像
    *   多阶段构建减小镜像大小
    *   非root用户运行
    *   镜像版本与应用版本一致

2.  **Docker Compose配置**：
    *   包含所有必要服务（应用、MySQL、Redis）
    *   配置持久化存储
    *   服务间网络配置
    *   健康检查设置

3.  **容器安全**：
    *   限制容器资源使用
    *   定期更新基础镜像
    *   使用最小权限原则
    *   容器内文件系统只读

## 9. 性能与优化规范

### 9.1 查询性能优化

查询性能优化应遵循以下规范：

1.  **索引优化**：
    *   分析查询模式设计索引
    *   避免过度索引
    *   监控索引使用情况
    *   定期维护索引

2.  **查询优化**：
    *   限制结果集大小
    *   使用分页查询
    *   避免不必要的连接
    *   优化子查询

3.  **缓存策略**：
    *   缓存常用查询结果
    *   实施缓存预热
    *   定义合理的缓存过期策略
    *   监控缓存命中率

### 9.2 前端性能优化

前端性能优化应遵循以下规范：

1.  **资源优化**：
    *   代码拆分与懒加载
    *   静态资源压缩
    *   图片优化
    *   CDN部署

2.  **渲染优化**：
    *   虚拟滚动处理大列表
    *   组件懒加载
    *   避免不必要的重渲染
    *   使用Web Workers处理复杂计算

3.  **网络优化**：
    *   API请求批处理
    *   数据预加载
    *   本地缓存利用
    *   应用离线支持

## 10. 开发流程与规范

### 10.1 版本控制规范

版本控制应遵循以下规范：

1.  **Git工作流**：
    *   采用GitFlow工作流
    *   feature分支用于新功能
    *   release分支用于发布准备
    *   hotfix分支用于紧急修复
    *   main分支保持稳定可部署

2.  **提交规范**：
    *   使用Angular提交消息规范
    *   包含类型、范围和描述
    *   提交前运行代码格式化和测试

3.  **版本号管理**：
    *   遵循语义化版本（Semantic Versioning）
    *   主版本号：不兼容的API变更
    *   次版本号：向后兼容的功能新增
    *   修订号：向后兼容的问题修复

### 10.2 代码规范

代码规范应遵循以下准则：

1.  **Java代码规范**：
    *   遵循Google Java Style
    *   使用Lombok减少样板代码
    *   编写详细的JavaDoc注释
    *   使用Stream API和函数式编程

2.  **JavaScript/TypeScript规范**：
    *   遵循Airbnb风格指南
    *   使用ESLint验证代码
    *   优先使用箭头函数和解构赋值
    *   编写JSDoc注释

3.  **数据库设计规范**：
    *   表名使用下划线分隔的小写, 且采用 `tbl` 前缀
    *   唯一索引采用 `u_idx` 前缀
    *   其他索引采用 `idx` 前缀
    *   为实现数据在不同环境间平滑的导出导入，主键采用uuid并在编码时注意唯一性检查
    *   外键命名为{表名}\_id
    *   包含created_at和updated_at字段
    *   使用UTF-8字符集

## 11. 技术成功标准

系统实施的技术成功标准如下：

1.  **功能完整性**：
    *   实现所有核心功能需求
    *   通过所有功能测试
    *   所有用户故事得到验收

2.  **性能指标**：
    *   页面加载时间 < 3秒
    *   简单查询响应时间 < 50ms
    *   复杂查询响应时间 < 5秒
    *   系统能同时处理20个并发用户

3.  **质量指标**：
    *   代码测试覆盖率 > 80%
    *   所有高严重性和中严重性缺陷修复
    *   静态代码分析通过率 > 95%

4.  **安全指标**：
    *   通过所有安全测试
    *   无高风险和中风险漏洞
    *   数据加密和掩码正确实施

5.  **用户体验指标**：
    *   用户测试满意度 > 80%
    *   关键任务完成时间符合预期
    *   错误消息明确且有指导性