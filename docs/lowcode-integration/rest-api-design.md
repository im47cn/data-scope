# 低代码集成模块 - REST API接口详细设计

本文档详细描述DataScope低代码集成模块的REST API接口设计，包括API架构原则、请求/响应格式、身份验证、权限控制、错误处理和端点定义。

## 1. API设计原则

DataScope REST API遵循以下设计原则：

- **RESTful架构**：遵循REST架构风格，使用HTTP方法表达操作语义
- **面向资源**：API围绕资源而非操作进行设计
- **一致性**：保持API命名、参数、响应格式的一致性
- **版本化**：通过URL路径进行API版本管理
- **自描述**：提供全面的API文档和自我描述能力
- **安全优先**：实施严格的认证、授权和数据验证机制
- **性能考虑**：支持分页、部分响应和条件请求

## 2. API基础架构

### 2.1 基本URL结构

```
https://api.datascope.com/v1/lowcode/{resource}
```

其中：
- `v1` - API版本号
- `lowcode` - 低代码集成模块名称
- `{resource}` - 资源类型（queries, versions, webhooks等）

### 2.2 HTTP方法

| 方法 | 描述 | 幂等性 |
|------|------|-------|
| GET | 获取资源 | 是 |
| POST | 创建资源 | 否 |
| PUT | 全量更新资源 | 是 |
| PATCH | 部分更新资源 | 否 |
| DELETE | 删除资源 | 是 |

### 2.3 通用HTTP状态码

| 状态码 | 描述 | 使用场景 |
|--------|------|----------|
| 200 OK | 请求成功 | GET、PUT、PATCH请求成功 |
| 201 Created | 资源创建成功 | POST请求成功创建资源 |
| 204 No Content | 请求成功，无返回内容 | DELETE请求成功 |
| 400 Bad Request | 请求参数错误 | 参数缺失或格式错误 |
| 401 Unauthorized | 未认证 | 缺少认证信息或认证失败 |
| 403 Forbidden | 权限不足 | 用户无权执行请求操作 |
| 404 Not Found | 资源不存在 | 请求的资源不存在 |
| 409 Conflict | 资源冲突 | 版本冲突或唯一性约束冲突 |
| 422 Unprocessable Entity | 业务规则验证失败 | 请求格式正确但无法处理 |
| 429 Too Many Requests | 请求过于频繁 | 超过API速率限制 |
| 500 Internal Server Error | 服务器内部错误 | 服务器处理异常 |

## 3. 认证与授权

### 3.1 认证方式

#### OAuth 2.0 / JWT

主要认证机制基于OAuth 2.0和JWT，支持以下授权流程：

1. **客户端凭证授权流程**：适用于服务器间集成
2. **授权码授权流程**：适用于用户交互应用
3. **密码授权流程**：适用于可信应用

```
# 获取访问令牌
POST /oauth/token
Content-Type: application/x-www-form-urlencoded

grant_type=client_credentials&client_id={clientId}&client_secret={clientSecret}
```

#### API密钥认证

对于简单集成场景，支持API密钥认证：

```
GET /api/v1/lowcode/queries
Authorization: ApiKey {apiKey}
```

### 3.2 权限模型

采用基于角色(RBAC)和资源(ABAC)的混合授权模型：

| 角色 | 权限 |
|------|------|
| ADMIN | 所有操作 |
| DEVELOPER | 创建、读取、更新查询和版本 |
| ANALYST | 读取查询和版本，执行查询 |
| VIEWER | 只读访问 |
| INTEGRATION | API集成特定权限 |

权限粒度细化到资源级别，支持：
- 读取权限
- 写入权限
- 执行权限
- 管理权限

### 3.3 API访问控制

每个API端点都实施访问控制：

```java
@PreAuthorize("hasRole('ADMIN') or hasPermission(#queryId, 'QUERY', 'READ')")
@GetMapping("/queries/{queryId}")
public ResponseEntity<QueryDto> getQuery(@PathVariable Long queryId) {
    // 实现
}
```

## 4. 请求与响应格式

### 4.1 请求格式

#### 4.1.1 请求头

必要的请求头：
```
Content-Type: application/json
Authorization: Bearer {jwt_token}
Accept: application/json
```

可选请求头：
```
Accept-Language: zh-CN
X-Request-ID: {unique_request_id}
If-None-Match: {etag}
```

#### 4.1.2 请求体

POST/PUT/PATCH请求使用JSON格式：

```json
{
  "name": "示例查询",
  "description": "这是一个示例查询",
  "sql": "SELECT * FROM users WHERE status = :status",
  "parameters": {
    "status": {
      "type": "string",
      "default": "active"
    }
  }
}
```

#### 4.1.3 查询参数

用于过滤、排序和分页：

```
GET /api/v1/lowcode/queries?page=0&size=20&sort=updatedAt,desc&name=example
```

支持的通用参数：
- `page` - 页码（从0开始）
- `size` - 每页大小
- `sort` - 排序字段和方向
- 资源特定的过滤参数

### 4.2 响应格式

#### 4.2.1 响应头

标准响应头：
```
Content-Type: application/json
ETag: {resource_version}
X-Request-ID: {request_id}
X-Rate-Limit-Remaining: {remaining_requests}
```

#### 4.2.2 响应体

单一资源：
```json
{
  "id": 12345,
  "name": "示例查询",
  "description": "这是一个示例查询",
  "sql": "SELECT * FROM users WHERE status = :status",
  "parameters": {
    "status": {
      "type": "string",
      "default": "active"
    }
  },
  "createdAt": "2025-01-15T14:33:22Z",
  "createdBy": "user@example.com",
  "updatedAt": "2025-02-20T09:15:40Z",
  "updatedBy": "admin@example.com",
  "_links": {
    "self": {
      "href": "/api/v1/lowcode/queries/12345"
    },
    "versions": {
      "href": "/api/v1/lowcode/queries/12345/versions"
    },
    "execute": {
      "href": "/api/v1/lowcode/queries/12345/execute"
    }
  }
}
```

资源集合：
```json
{
  "content": [
    {
      "id": 12345,
      "name": "示例查询1",
      // 其他属性...
    },
    {
      "id": 12346,
      "name": "示例查询2",
      // 其他属性...
    }
  ],
  "page": {
    "size": 20,
    "totalElements": 42,
    "totalPages": 3,
    "number": 0
  },
  "_links": {
    "self": {
      "href": "/api/v1/lowcode/queries?page=0&size=20"
    },
    "next": {
      "href": "/api/v1/lowcode/queries?page=1&size=20"
    },
    "last": {
      "href": "/api/v1/lowcode/queries?page=2&size=20"
    }
  }
}
```

## 5. 错误处理

### 5.1 错误响应格式

标准错误响应：

```json
{
  "timestamp": "2025-03-18T08:14:22.356Z",
  "status": 400,
  "error": "Bad Request",
  "message": "查询名称不能为空",
  "path": "/api/v1/lowcode/queries",
  "requestId": "a1b2c3d4-e5f6",
  "errors": [
    {
      "field": "name",
      "message": "不能为空",
      "code": "NotBlank"
    }
  ]
}
```

### 5.2 错误码

系统定义一组业务错误码，包括：

| 错误码 | 描述 | HTTP状态码 |
|--------|------|------------|
| `QUERY_NOT_FOUND` | 查询不存在 | 404 |
| `VERSION_NOT_FOUND` | 版本不存在 | 404 |
| `INVALID_SQL` | SQL语法无效 | 400 |
| `PARAMETER_MISSING` | 缺少必要参数 | 400 |
| `DUPLICATE_NAME` | 名称重复 | 409 |
| `VERSION_CONFLICT` | 版本冲突 | 409 |
| `EXECUTION_TIMEOUT` | 执行超时 | 408 |
| `ACCESS_DENIED` | 访问被拒绝 | 403 |
| `RATE_LIMIT_EXCEEDED` | 超过速率限制 | 429 |

### 5.3 全局异常处理

使用Spring的`@ControllerAdvice`实现统一异常处理：

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .requestId(request.getHeader("X-Request-ID"))
                .build();
        
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    
    // 其他异常处理方法...
}
```

## 6. API端点设计

### 6.1 查询相关API

#### 查询管理

```
# 查询管理
GET    /api/v1/lowcode/queries                      # 获取查询列表
POST   /api/v1/lowcode/queries                      # 创建查询
GET    /api/v1/lowcode/queries/{id}                 # 获取查询详情
PUT    /api/v1/lowcode/queries/{id}                 # 更新查询
DELETE /api/v1/lowcode/queries/{id}                 # 删除查询
GET    /api/v1/lowcode/queries/{id}/history         # 获取查询历史
```

#### 版本管理

```
# 版本管理
GET    /api/v1/lowcode/queries/{id}/versions        # 获取版本列表
POST   /api/v1/lowcode/queries/{id}/versions        # 创建新版本
GET    /api/v1/lowcode/versions/{id}                # 获取版本详情
POST   /api/v1/lowcode/versions/{id}/publish        # 发布版本
GET    /api/v1/lowcode/versions/{id}/diff/{targetId} # 获取版本差异
POST   /api/v1/lowcode/queries/{id}/rollback/{versionId} # 回滚到指定版本
```

#### 查询执行

```
# 查询执行
POST   /api/v1/lowcode/queries/{id}/execute         # 执行查询
POST   /api/v1/lowcode/versions/{id}/execute        # 执行特定版本
GET    /api/v1/lowcode/executions/{id}              # 获取执行结果
DELETE /api/v1/lowcode/executions/{id}              # 取消执行
```

### 6.2 WebHook相关API

```
# WebHook管理
GET    /api/v1/lowcode/webhooks                     # 获取WebHook列表
POST   /api/v1/lowcode/webhooks                     # 创建WebHook
GET    /api/v1/lowcode/webhooks/{id}                # 获取WebHook详情
PUT    /api/v1/lowcode/webhooks/{id}                # 更新WebHook
DELETE /api/v1/lowcode/webhooks/{id}                # 删除WebHook
POST   /api/v1/lowcode/webhooks/{id}/activate       # 激活WebHook
POST   /api/v1/lowcode/webhooks/{id}/deactivate     # 停用WebHook
POST   /api/v1/lowcode/webhooks/{id}/test           # 测试WebHook
GET    /api/v1/lowcode/webhooks/{id}/deliveries     # 获取交付记录
```

### 6.3 数据绑定相关API

```
# 数据绑定管理
GET    /api/v1/lowcode/bindings                     # 获取绑定列表
POST   /api/v1/lowcode/bindings                     # 创建绑定
GET    /api/v1/lowcode/bindings/{id}                # 获取绑定详情
PUT    /api/v1/lowcode/bindings/{id}                # 更新绑定
DELETE /api/v1/lowcode/bindings/{id}                # 删除绑定
POST   /api/v1/lowcode/bindings/{id}/sync           # 手动同步绑定
GET    /api/v1/lowcode/bindings/{id}/sync-history   # 获取同步历史
```

### 6.4 元数据和结构API

```
# 数据源结构
GET    /api/v1/lowcode/datasources                  # 获取数据源列表
GET    /api/v1/lowcode/datasources/{id}             # 获取数据源详情
GET    /api/v1/lowcode/datasources/{id}/schemas     # 获取数据源模式
GET    /api/v1/lowcode/datasources/{id}/tables      # 获取数据源表
GET    /api/v1/lowcode/datasources/{id}/tables/{name}/columns # 获取表列

# 关系管理
GET    /api/v1/lowcode/relationships                # 获取关系列表
GET    /api/v1/lowcode/relationships/{id}           # 获取关系详情
```

## 7. 详细API规范

### 7.1 查询管理API

#### 获取查询列表

```
GET /api/v1/lowcode/queries
```

查询参数：
- `page`: 页码（默认0）
- `size`: 每页大小（默认20）
- `sort`: 排序（如：updatedAt,desc）
- `name`: 按名称过滤
- `tags`: 按标签过滤
- `status`: 按状态过滤

响应示例：
```json
{
  "content": [
    {
      "id": 12345,
      "name": "用户活跃度分析",
      "description": "分析用户活跃程度的查询",
      "tags": ["用户分析", "活跃度"],
      "status": "PUBLISHED",
      "currentVersionId": 78901,
      "createdAt": "2025-01-15T14:33:22Z",
      "createdBy": "analyst@example.com",
      "updatedAt": "2025-02-20T09:15:40Z",
      "updatedBy": "analyst@example.com"
    },
    // 更多查询...
  ],
  "page": {
    "size": 20,
    "totalElements": 42,
    "totalPages": 3,
    "number": 0
  },
  "_links": {
    "self": {"href": "/api/v1/lowcode/queries?page=0&size=20"},
    "next": {"href": "/api/v1/lowcode/queries?page=1&size=20"},
    "last": {"href": "/api/v1/lowcode/queries?page=2&size=20"}
  }
}
```

#### 创建查询

```
POST /api/v1/lowcode/queries
Content-Type: application/json
Authorization: Bearer {token}
```

请求体示例：
```json
{
  "name": "新客户注册统计",
  "description": "统计每日新注册客户数量",
  "tags": ["客户", "注册", "统计"],
  "sql": "SELECT date(created_at) as reg_date, count(*) as count FROM customers WHERE created_at >= :startDate AND created_at <= :endDate GROUP BY reg_date ORDER BY reg_date",
  "parameters": {
    "startDate": {
      "type": "date",
      "required": true,
      "default": "{{today-30d}}"
    },
    "endDate": {
      "type": "date",
      "required": true,
      "default": "{{today}}"
    }
  },
  "displayConfig": {
    "chartType": "line",
    "xAxis": "reg_date",
    "yAxis": "count",
    "title": "每日新客户注册数"
  }
}
```

响应示例（201 Created）：
```json
{
  "id": 12346,
  "name": "新客户注册统计",
  "description": "统计每日新注册客户数量",
  "tags": ["客户", "注册", "统计"],
  "status": "DRAFT",
  "currentVersionId": 78902,
  "createdAt": "2025-03-18T08:30:15Z",
  "createdBy": "analyst@example.com",
  "updatedAt": "2025-03-18T08:30:15Z",
  "updatedBy": "analyst@example.com",
  "_links": {
    "self": {"href": "/api/v1/lowcode/queries/12346"},
    "versions": {"href": "/api/v1/lowcode/queries/12346/versions"},
    "execute": {"href": "/api/v1/lowcode/queries/12346/execute"}
  }
}
```

### 7.2 WebHook管理API

#### 创建WebHook

```
POST /api/v1/lowcode/webhooks
Content-Type: application/json
Authorization: Bearer {token}
```

请求体示例：
```json
{
  "name": "查询执行通知",
  "description": "当查询执行完成时发送通知",
  "targetUrl": "https://example.com/webhooks/datascope",
  "events": ["query.executed", "version.published"],
  "headers": {
    "X-Custom-Header": "CustomValue"
  },
  "secret": "s3cr3tK3y",
  "contentType": "application/json",
  "format": "JSON",
  "maxRetries": 3,
  "retryInterval": 60
}
```

响应示例（201 Created）：
```json
{
  "id": 5001,
  "name": "查询执行通知",
  "description": "当查询执行完成时发送通知",
  "targetUrl": "https://example.com/webhooks/datascope",
  "status": "ACTIVE",
  "events": ["query.executed", "version.published"],
  "contentType": "application/json",
  "format": "JSON",
  "maxRetries": 3,
  "retryInterval": 60,
  "createdAt": "2025-03-18T09:25:30Z",
  "createdBy": "developer@example.com",
  "updatedAt": "2025-03-18T09:25:30Z",
  "updatedBy": "developer@example.com",
  "_links": {
    "self": {"href": "/api/v1/lowcode/webhooks/5001"},
    "deliveries": {"href": "/api/v1/lowcode/webhooks/5001/deliveries"},
    "test": {"href": "/api/v1/lowcode/webhooks/5001/test"}
  }
}
```

### 7.3 数据绑定API

#### 创建数据绑定

```
POST /api/v1/lowcode/bindings
Content-Type: application/json
Authorization: Bearer {token}
```

请求体示例：
```json
{
  "name": "用户列表绑定",
  "description": "用户数据与低代码应用表格组件绑定",
  "queryId": 12345,
  "appId": "app123",
  "componentId": "table001",
  "bindingType": "TWO_WAY",
  "mappings": [
    {
      "source": "user_id",
      "target": "id",
      "type": "FIELD"
    },
    {
      "source": "username",
      "target": "name",
      "type": "FIELD"
    },
    {
      "source": "email",
      "target": "email",
      "type": "FIELD"
    },
    {
      "source": "status",
      "target": "status",
      "type": "FIELD"
    }
  ],
  "transformers": [
    {
      "field": "status",
      "type": "MAP",
      "config": {
        "active": "启用",
        "inactive": "禁用",
        "pending": "待处理"
      }
    }
  ],
  "refreshInterval": 300,
  "parameters": {
    "status": {
      "type": "DYNAMIC",
      "source": "component",
      "path": "filters.status"
    }
  }
}
```

响应示例（201 Created）：
```json
{
  "id": 7001,
  "name": "用户列表绑定",
  "description": "用户数据与低代码应用表格组件绑定",
  "queryId": 12345,
  "appId": "app123",
  "componentId": "table001",
  "bindingType": "TWO_WAY",
  "mappings": [
    {
      "source": "user_id",
      "target": "id",
      "type": "FIELD"
    },
    // 其他映射...
  ],
  "transformers": [
    {
      "field": "status",
      "type": "MAP",
      "config": {
        "active": "启用",
        "inactive": "禁用",
        "pending": "待处理"
      }
    }
  ],
  "refreshInterval": 300,
  "lastSyncAt": null,
  "createdAt": "2025-03-18T10:15:20Z",
  "createdBy": "developer@example.com",
  "updatedAt": "2025-03-18T10:15:20Z",
  "updatedBy": "developer@example.com",
  "_links": {
    "self": {"href": "/api/v1/lowcode/bindings/7001"},
    "query": {"href": "/api/v1/lowcode/queries/12345"},
    "sync": {"href": "/api/v1/lowcode/bindings/7001/sync"},
    "syncHistory": {"href": "/api/v1/lowcode/bindings/7001/sync-history"}
  }
}
```

## 8. 批量操作

对于需要批量处理的场景，API支持批量操作：

```
POST /api/v1/lowcode/queries/batch
Content-Type: application/json
Authorization: Bearer {token}

{
  "operationType": "DELETE",
  "ids": [12345, 12346, 12347]
}
```

响应示例：
```json
{
  "totalCount": 3,
  "successCount": 2,
  "failureCount": 1,
  "results": [
    {"id": 12345, "status": "SUCCESS"},
    {"id": 12346, "status": "SUCCESS"},
    {"id": 12347, "status": "FAILURE", "reason": "NOT_FOUND"}
  ]
}
```

## 9. API文档和探索

### 9.1 OpenAPI规范

使用OpenAPI 3.0规范记录所有API端点，通过Swagger UI提供交互式文档：

```
GET /api-docs
GET /swagger-ui.html
```

### 9.2 API发现

实现HATEOAS（超媒体作为应用状态引擎），支持API自发现：

```
GET /api/v1/lowcode
```

响应示例：
```json
{
  "_links": {
    "queries": {"href": "/api/v1/lowcode/queries"},
    "webhooks": {"href": "/api/v1/lowcode/webhooks"},
    "bindings": {"href": "/api/v1/lowcode/bindings"},
    "datasources": {"href": "/api/v1/lowcode/datasources"},
    "documentation": {"href": "/api-docs"}
  }
}
```

## 10. 性能优化

### 10.1 分页和部分响应

所有集合接口支持分页：
```
GET /api/v1/lowcode/queries?page=0&size=20
```

支持字段过滤，减少响应大小：
```
GET /api/v1/lowcode/queries?fields=id,name,status
```

### 10.2 缓存策略

使用HTTP缓存控制减少重复请求：

```
GET /api/v1/lowcode/queries/12345
If-None-Match: "v1-12345-1614962325"
```

响应（如果未更改）：
```
HTTP/1.1 304 Not Modified
ETag: "v1-12345-1614962325"
Cache-Control: max-age=3600
```

### 10.3 压缩

支持HTTP压缩减少传输数据大小：

```
GET /api/v1/lowcode/queries
Accept-Encoding: gzip, deflate
```

## 11. 请求示例

### 11.1 查询执行

```
POST /api/v1/lowcode/queries/12345/execute
Content-Type: application/json
Authorization: Bearer {token}

{
  "parameters": {
    "status": "active",
    "startDate": "2025-01-01",
    "endDate": "2025-03-01"
  },
  "options": {
    "timeout": 30000,
    "maxRows": 1000,
    "format": "json"
  }
}
```

响应示例：
```json
{
  "executionId": "exec123",
  "status": "COMPLETED",
  "startTime": "2025-03-18T11:20:15Z",
  "endTime": "2025-03-18T11:20:16Z",
  "duration": 1245,
  "rowCount": 60,
  "columns": [
    {"name": "reg_date", "type": "DATE"},
    {"name": "count", "type": "INTEGER"}
  ],
  "data": [
    ["2025-01-01", 15],
    ["2025-01-02", 22],
    // 更多数据...
  ],
  "_links": {
    "self": {"href": "/api/v1/lowcode/executions/exec123"},
    "query": {"href": "/api/v1/lowcode/queries/12345"},
    "download": {"href": "/api/v1/lowcode/executions/exec123/download?format=csv"}
  }
}
```

### 11.2 版本比较

```
GET /api/v1/lowcode/versions/78901/diff/78902
Accept: application/json
Authorization: Bearer {token}
```

响应示例：
```json
{
  "baseVersion": {
    "id": 78901,
    "queryId": 12345,
    "versionNumber": 1,
    "createdAt": "2025-01-15T14:33:22Z",
    "createdBy": "analyst@example.com"
  },
  "targetVersion": {
    "id": 78902,
    "queryId": 12345,
    "versionNumber": 2,
    "createdAt": "2025-02-20T09:15:40Z",
    "createdBy": "analyst@example.com"
  },
  "sqlDiff": {
    "changes": [
      {
        "type": "MODIFIED",
        "lineNumber": 3,
        "oldValue": "WHERE created_at >= :startDate",
        "newValue": "WHERE created_at >= :startDate AND status = :status"
      }
    ],
    "summary": "添加了status过滤条件"
  },
  "parametersDiff": {
    "added": [
      {
        "name": "status",
        "type": "string",
        "required": true,
        "default": "active"
      }
    ],
    "modified": [],
    "removed": []
  },
  "displayConfigDiff": {
    "modified": [
      {
        "path": "title",
        "oldValue": "每日注册数",
        "newValue": "每日活跃用户注册数"
      }
    ]
  },
  "diffHtml": "<div class='diff'>...</div>",
  "_links": {
    "self": {"href": "/api/v1/lowcode/versions/78901/diff/78902"},
    "baseVersion": {"href": "/api/v1/lowcode/versions/78901"},
    "targetVersion": {"href": "/api/v1/lowcode/versions/78902"}
  }
}
```

## 12. 客户端SDK

为主要编程语言提供客户端SDK，简化API集成：

### 12.1 JavaScript SDK示例

```javascript
// 客户端初始化
const dataScope = new DataScope({
  baseUrl: 'https://api.datascope.com/v1',
  apiKey: 'your-api-key'
});

// 获取查询
const query = await dataScope.queries.get(12345);

// 执行查询
const result = await dataScope.queries.execute(12345, {
  parameters: {
    status: 'active',
    startDate: '2025-01-01',
    endDate: '2025-03-01'
  }
});

// 创建WebHook
const webhook = await dataScope.webhooks.create({
  name: 'Query Notification',
  targetUrl: 'https://example.com/webhooks',
  events: ['query.executed'],
  secret: 'your-secret-key'
});
```

### 12.2 Java SDK示例

```java
// 客户端初始化
DataScopeClient dataScope = DataScopeClient.builder()
    .baseUrl("https://api.datascope.com/v1")
    .apiKey("your-api-key")
    .build();

// 获取查询
Query query = dataScope.queries().get(12345L);

// 执行查询
Map<String, Object> parameters = new HashMap<>();
parameters.put("status", "active");
parameters.put("startDate", "2025-01-01");
parameters.put("endDate", "2025-03-01");

QueryExecution result = dataScope.queries().execute(12345L, parameters);

// 创建WebHook
WebHook webhook = dataScope.webhooks().create(new WebHookRequest()
    .name("Query Notification")
    .targetUrl("https://example.com/webhooks")
    .events(List.of("query.executed"))
    .secret("your-secret-key"));
```

## 13. 监控与分析

API包括监控和使用分析端点：

```
GET /api/v1/lowcode/stats/usage
GET /api/v1/lowcode/stats/performance
GET /api/v1/lowcode/stats/errors
```

响应示例：
```json
{
  "period": "2025-03-10T00:00:00Z/2025-03-17T00:00:00Z",
  "totalRequests": 15427,
  "uniqueClients": 23,
  "topEndpoints": [
    {
      "endpoint": "/api/v1/lowcode/queries/{id}/execute",
      "requestCount": 5892,
      "averageResponseTime": 245
    },
    {
      "endpoint": "/api/v1/lowcode/queries",
      "requestCount": 3241,
      "averageResponseTime": 120
    }
  ],
  "errorRate": 0.014,
  "topErrors": [
    {
      "status": 404,
      "count": 102,
      "topPaths": ["/api/v1/lowcode/queries/9999"]
    },
    {
      "status": 400,
      "count": 89,
      "topPaths": ["/api/v1/lowcode/queries/{id}/execute"]
    }
  ]
}
```

## 14. 安全考虑

### 14.1 跨站请求伪造保护

对于修改操作，实施CSRF保护：

```
POST /api/v1/lowcode/queries
X-CSRF-Token: {token}
```

### 14.2 速率限制

实施基于客户端的速率限制：

```
GET /api/v1/lowcode/queries
X-Rate-Limit-Limit: 1000
X-Rate-Limit-Remaining: 950
X-Rate-Limit-Reset: 1615986834
```

超出限制响应：
```
HTTP/1.1 429 Too Many Requests
Retry-After: 30
```

### 14.3 敏感数据处理

- 密码和密钥永不在响应中返回
- 生产数据库连接字符串部分掩码
- 身份验证凭据仅通过HTTPS传输

## 15. 版本控制与兼容性

### 15.1 版本迁移

提供API版本迁移指南：

```
GET /api/v1/lowcode/versions/migration-guide
```

### 15.2 版本兼容性矩阵

提供功能兼容性矩阵：

```
GET /api/v1/lowcode/versions/compatibility
```

响应示例：
```json
{
  "current": "v1",
  "supported": ["v1", "v0.9"],
  "deprecated": ["v0.8", "v0.7"],
  "sunset": ["v0.6"],
  "features": [
    {
      "name": "webhooks",
      "addedIn": "v0.8",
      "modifiedIn": ["v0.9", "v1"],
      "plannedChanges": "None"
    },
    {
      "name": "data-bindings",
      "addedIn": "v0.9",
      "modifiedIn": ["v1"],
      "plannedChanges": "Enhanced conflict resolution in v1.1"
    }
  ]
}
```

## 16. 总结

REST API接口是DataScope低代码集成模块的核心组件，提供标准化的接口，支持低代码平台访问DataScope资源，实现查询配置、执行查询、获取元数据等功能。本设计文档详细描述了API架构原则、认证授权机制、请求响应格式、错误处理以及具体端点定义，为开发团队提供了清晰的实施指南。

通过遵循此设计，开发团队将能够实现一个功能完整、安全可靠、性能优化的REST API，满足低代码平台与DataScope系统的无缝集成需求。