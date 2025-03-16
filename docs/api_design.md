# 数据管理与查询系统 - API设计文档

本文档详细描述了数据管理与查询系统的RESTful API设计，为前后端开发提供接口规范。

## 1. 设计原则

### 1.1 RESTful设计原则

- 使用HTTP方法明确表达操作语义（GET、POST、PUT、DELETE等）
- 使用名词而非动词设计资源端点
- 使用HTTP状态码表达操作结果
- 支持分页、排序和过滤
- 版本化API设计
- 使用JSON作为默认数据交换格式
- 提供全面的错误信息

### 1.2 API版本控制

- 在URL中包含版本号，如`/api/v1/datasources`
- 初期采用v1版本，后续扩展功能时平滑过渡到新版本

### 1.3 认证与授权

- 使用JWT（JSON Web Token）进行认证
- 基于角色的访问控制（RBAC）
- API密钥用于集成场景

### 1.4 响应格式标准化

**成功响应格式**:
```json
{
  "success": true,
  "data": {
    // 响应数据
  },
  "timestamp": "2025-03-15T14:30:00Z"
}
```

**错误响应格式**:
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "错误消息",
    "details": "详细错误信息"
  },
  "timestamp": "2025-03-15T14:30:00Z"
}
```

## 2. API端点设计

### 2.1 数据源管理API

#### 2.1.1 创建数据源

- **URL**: `/api/v1/datasources`
- **方法**: `POST`
- **描述**: 创建新的数据源
- **权限**: `ADMIN`, `DATASOURCE_MANAGER`
- **请求体**:
  ```json
  {
    "name": "销售数据库",
    "type": "MYSQL",
    "host": "192.168.1.100",
    "port": 3306,
    "database": "sales_db",
    "username": "dbuser",
    "password": "password123",
    "active": true,
    "description": "销售部门的MySQL数据库",
    "properties": {
      "useSSL": "false",
      "autoReconnect": "true"
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "id": 1,
      "name": "销售数据库",
      "type": "MYSQL",
      "host": "192.168.1.100",
      "port": 3306,
      "database": "sales_db",
      "username": "dbuser",
      "passwordSet": true,
      "active": true,
      "description": "销售部门的MySQL数据库",
      "properties": {
        "useSSL": "false",
        "autoReconnect": "true"
      },
      "createdAt": "2025-03-15T14:30:00Z",
      "updatedAt": "2025-03-15T14:30:00Z"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `409 Conflict`: 数据源名称已存在

#### 2.1.2 获取数据源列表

- **URL**: `/api/v1/datasources`
- **方法**: `GET`
- **描述**: 获取所有数据源的列表
- **权限**: `USER`, `ADMIN`
- **查询参数**:
  - `page`: 页码，默认0
  - `size`: 每页条数，默认20
  - `sort`: 排序字段，如`name,asc`
  - `type`: 按数据库类型过滤
  - `active`: 按活跃状态过滤
  - `search`: 搜索关键词
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "content": [
        {
          "id": 1,
          "name": "销售数据库",
          "type": "MYSQL",
          "host": "192.168.1.100",
          "port": 3306,
          "database": "sales_db",
          "username": "dbuser",
          "passwordSet": true,
          "active": true,
          "description": "销售部门的MySQL数据库",
          "lastSyncTime": "2025-03-15T12:30:00Z",
          "createdAt": "2025-03-15T10:00:00Z",
          "updatedAt": "2025-03-15T12:30:00Z"
        },
        // 更多数据源...
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
          "sorted": true,
          "unsorted": false,
          "empty": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
      },
      "totalElements": 42,
      "totalPages": 3,
      "last": false,
      "size": 20,
      "number": 0,
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "numberOfElements": 20,
      "first": true,
      "empty": false
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```

#### 2.1.3 获取数据源详情

- **URL**: `/api/v1/datasources/{id}`
- **方法**: `GET`
- **描述**: 获取特定数据源的详细信息
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "id": 1,
      "name": "销售数据库",
      "type": "MYSQL",
      "host": "192.168.1.100",
      "port": 3306,
      "database": "sales_db",
      "username": "dbuser",
      "passwordSet": true,
      "active": true,
      "description": "销售部门的MySQL数据库",
      "properties": {
        "useSSL": "false",
        "autoReconnect": "true"
      },
      "lastSyncTime": "2025-03-15T12:30:00Z",
      "schemaCount": 3,
      "tableCount": 42,
      "createdAt": "2025-03-15T10:00:00Z",
      "updatedAt": "2025-03-15T12:30:00Z"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源不存在

#### 2.1.4 更新数据源

- **URL**: `/api/v1/datasources/{id}`
- **方法**: `PUT`
- **描述**: 更新数据源信息
- **权限**: `ADMIN`, `DATASOURCE_MANAGER`
- **请求体**:
  ```json
  {
    "name": "更新后的销售数据库",
    "host": "192.168.1.101",
    "port": 3306,
    "database": "sales_db",
    "username": "dbuser",
    "password": "newpassword",
    "active": true,
    "description": "更新后的描述",
    "properties": {
      "useSSL": "true",
      "autoReconnect": "true"
    }
  }
  ```
- **响应**: 与创建数据源相同
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 数据源不存在
  - `409 Conflict`: 数据源名称已存在

#### 2.1.5 删除数据源

- **URL**: `/api/v1/datasources/{id}`
- **方法**: `DELETE`
- **描述**: 删除数据源
- **权限**: `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "message": "数据源已成功删除"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源不存在
  - `409 Conflict`: 数据源正在被使用，无法删除

#### 2.1.6 测试数据源连接

- **URL**: `/api/v1/datasources/{id}/test`
- **方法**: `POST`
- **描述**: 测试数据源连接是否可用
- **权限**: `ADMIN`, `DATASOURCE_MANAGER`
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "connected": true,
      "message": "连接成功",
      "databaseVersion": "MySQL 8.0.28",
      "connectionTime": 120
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源不存在
  - `500 Internal Server Error`: 连接测试失败

#### 2.1.7 获取支持的数据源类型

- **URL**: `/api/v1/datasources/types`
- **方法**: `GET`
- **描述**: 获取系统支持的所有数据源类型
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": [
      {
        "code": "MYSQL",
        "name": "MySQL",
        "description": "MySQL关系型数据库",
        "icon": "mysql-icon.png"
      },
      {
        "code": "DB2",
        "name": "IBM DB2",
        "description": "IBM DB2关系型数据库",
        "icon": "db2-icon.png"
      }
    ],
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```

### 2.2 元数据管理API

#### 2.2.1 同步元数据

- **URL**: `/api/v1/datasources/{id}/metadata/sync`
- **方法**: `POST`
- **描述**: 触发数据源元数据同步
- **权限**: `ADMIN`, `DATASOURCE_MANAGER`
- **请求体**:
  ```json
  {
    "syncType": "FULL", // FULL or INCREMENTAL
    "options": {
      "includeSchemas": true,
      "includeTables": true,
      "includeColumns": true,
      "includeIndexes": true,
      "includeForeignKeys": true,
      "schemaPattern": "sales%",
      "tablePattern": null,
      "tableTypes": ["TABLE", "VIEW"]
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "jobId": "sync-job-123",
      "status": "RUNNING",
      "message": "元数据同步任务已启动",
      "startTime": "2025-03-15T14:30:00Z"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 数据源不存在
  - `409 Conflict`: 已有同步任务正在进行

#### 2.2.2 获取同步状态

- **URL**: `/api/v1/datasources/{id}/metadata/sync/{jobId}`
- **方法**: `GET`
- **描述**: 获取元数据同步任务的状态
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "jobId": "sync-job-123",
      "status": "RUNNING", // PENDING, RUNNING, COMPLETED, FAILED, CANCELLED
      "progress": 65, // 百分比 0-100
      "message": "正在同步表结构",
      "startTime": "2025-03-15T14:30:00Z",
      "endTime": null,
      "statistics": {
        "schemaCount": 2,
        "tableCount": 25,
        "columnCount": 156,
        "indexCount": 38,
        "foreignKeyCount": 12
      }
    },
    "timestamp": "2025-03-15T14:30:05Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源或同步任务不存在

#### 2.2.3 取消同步任务

- **URL**: `/api/v1/datasources/{id}/metadata/sync/{jobId}`
- **方法**: `DELETE`
- **描述**: 取消进行中的元数据同步任务
- **权限**: `ADMIN`, `DATASOURCE_MANAGER`
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "jobId": "sync-job-123",
      "status": "CANCELLED",
      "message": "同步任务已取消",
      "cancelTime": "2025-03-15T14:31:00Z"
    },
    "timestamp": "2025-03-15T14:31:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源或同步任务不存在
  - `409 Conflict`: 任务已完成或已取消

#### 2.2.4 获取数据源模式列表

- **URL**: `/api/v1/datasources/{id}/schemas`
- **方法**: `GET`
- **描述**: 获取数据源的模式列表
- **权限**: `USER`, `ADMIN`
- **查询参数**:
  - `page`: 页码，默认0
  - `size`: 每页条数，默认20
  - `search`: 模式名称搜索
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "content": [
        {
          "id": 1,
          "name": "sales",
          "description": "销售数据模式",
          "tableCount": 15,
          "lastSyncTime": "2025-03-15T12:30:00Z"
        },
        {
          "id": 2,
          "name": "inventory",
          "description": "库存数据模式",
          "tableCount": 8,
          "lastSyncTime": "2025-03-15T12:30:00Z"
        }
      ],
      "pageable": {
        "pageNumber": 0,
        "pageSize": 20,
        "sort": {
          "sorted": true,
          "unsorted": false,
          "empty": false
        },
        "offset": 0,
        "paged": true,
        "unpaged": false
      },
      "totalElements": 3,
      "totalPages": 1,
      "last": true,
      "size": 20,
      "number": 0,
      "sort": {
        "sorted": true,
        "unsorted": false,
        "empty": false
      },
      "numberOfElements": 2,
      "first": true,
      "empty": false
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源不存在

#### 2.2.5 获取模式中的表列表

- **URL**: `/api/v1/datasources/{id}/schemas/{schemaName}/tables`
- **方法**: `GET`
- **描述**: 获取模式中的表列表
- **权限**: `USER`, `ADMIN`
- **查询参数**:
  - `page`: 页码，默认0
  - `size`: 每页条数，默认20
  - `search`: 表名搜索
  - `tableType`: 表类型过滤（TABLE, VIEW）
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "content": [
        {
          "id": 1,
          "name": "customers",
          "type": "TABLE",
          "description": "客户信息表",
          "columnCount": 12,
          "estimatedRowCount": 45000,
          "lastSyncTime": "2025-03-15T12:30:00Z"
        },
        {
          "id": 2,
          "name": "orders",
          "type": "TABLE",
          "description": "订单表",
          "columnCount": 8,
          "estimatedRowCount": 1200000,
          "lastSyncTime": "2025-03-15T12:30:00Z"
        }
      ],
      // 分页信息...与之前格式相同
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源或模式不存在

#### 2.2.6 获取表的列信息

- **URL**: `/api/v1/datasources/{id}/schemas/{schemaName}/tables/{tableName}/columns`
- **方法**: `GET`
- **描述**: 获取表的列信息
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "name": "customer_id",
        "ordinalPosition": 1,
        "dataType": "INT",
        "length": 11,
        "nullable": false,
        "isPrimaryKey": true,
        "isAutoIncrement": true,
        "description": "客户ID，主键",
        "defaultValue": null
      },
      {
        "id": 2,
        "name": "customer_name",
        "ordinalPosition": 2,
        "dataType": "VARCHAR",
        "length": 100,
        "nullable": false,
        "isPrimaryKey": false,
        "isAutoIncrement": false,
        "description": "客户名称",
        "defaultValue": null
      },
      // 更多列...
    ],
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源、模式或表不存在

#### 2.2.7 获取表的索引信息

- **URL**: `/api/v1/datasources/{id}/schemas/{schemaName}/tables/{tableName}/indexes`
- **方法**: `GET`
- **描述**: 获取表的索引信息
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "name": "PRIMARY",
        "type": "BTREE",
        "isUnique": true,
        "columns": [
          {
            "name": "customer_id",
            "ordinalPosition": 1,
            "sortOrder": "ASC"
          }
        ]
      },
      {
        "id": 2,
        "name": "idx_customer_email",
        "type": "BTREE",
        "isUnique": true,
        "columns": [
          {
            "name": "email",
            "ordinalPosition": 1,
            "sortOrder": "ASC"
          }
        ]
      }
      // 更多索引...
    ],
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源、模式或表不存在

#### 2.2.8 获取表的外键信息

- **URL**: `/api/v1/datasources/{id}/schemas/{schemaName}/tables/{tableName}/foreignkeys`
- **方法**: `GET`
- **描述**: 获取表的外键信息
- **权限**: `USER`, `ADMIN`
- **响应**:
  ```json
  {
    "success": true,
    "data": [
      {
        "id": 1,
        "name": "fk_orders_customer",
        "sourceTable": "orders",
        "targetSchema": "sales",
        "targetTable": "customers",
        "updateRule": "RESTRICT",
        "deleteRule": "CASCADE",
        "columns": [
          {
            "sourceColumn": "customer_id",
            "targetColumn": "customer_id",
            "ordinalPosition": 1
          }
        ]
      }
      // 更多外键...
    ],
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源、模式或表不存在

#### 2.2.9 获取元数据树结构

- **URL**: `/api/v1/datasources/{id}/metadata/tree`
- **方法**: `GET`
- **描述**: 获取数据源的元数据树结构
- **权限**: `USER`, `ADMIN`
- **查询参数**:
  - `schemaPattern`: 模式名匹配模式
  - `tablePattern`: 表名匹配模式
  - `depth`: 树的深度（1=只返回模式，2=返回模式和表，3=返回模式、表和列）
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "dataSourceId": 1,
      "dataSourceName": "销售数据库",
      "schemas": [
        {
          "id": 1,
          "name": "sales",
          "tables": [
            {
              "id": 1,
              "name": "customers",
              "type": "TABLE",
              "columns": [
                {
                  "id": 1,
                  "name": "customer_id",
                  "dataType": "INT",
                  "isPrimaryKey": true
                },
                // 更多列...
              ]
            },
            // 更多表...
          ]
        },
        // 更多模式...
      ]
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `404 Not Found`: 数据源不存在

### 2.3 查询构建与执行API

#### 2.3.1 执行SQL查询

- **URL**: `/api/v1/queries/execute`
- **方法**: `POST`
- **描述**: 执行SQL查询并返回结果
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "dataSourceId": 1,
    "sql": "SELECT c.customer_id, c.customer_name, COUNT(o.order_id) AS order_count FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE c.status = 'active' GROUP BY c.customer_id, c.customer_name ORDER BY order_count DESC LIMIT 10",
    "parameters": {
      "status": "active"
    },
    "options": {
      "timeout": 30,
      "maxRows": 1000
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "columns": [
        {
          "name": "customer_id",
          "type": "INTEGER",
          "label": "customer_id"
        },
        {
          "name": "customer_name",
          "type": "VARCHAR",
          "label": "customer_name"
        },
        {
          "name": "order_count",
          "type": "BIGINT",
          "label": "order_count"
        }
      ],
      "rows": [
        [1001, "张三企业", 42],
        [1042, "李四有限公司", 38],
        [1023, "王五科技", 35],
        // 更多行...
      ],
      "rowCount": 10,
      "executionTime": 245,
      "hasMoreData": false
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: SQL语法错误或参数不合法
  - `404 Not Found`: 数据源不存在
  - `408 Request Timeout`: 查询超时
  - `500 Internal Server Error`: 执行查询时发生错误

#### 2.3.2 验证SQL查询

- **URL**: `/api/v1/queries/validate`
- **方法**: `POST`
- **描述**: 验证SQL查询语法和结构，不执行
- **权限**: `USER`, `ADMIN`
- **请求体**: 与执行查询相同
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "valid": true,
      "message": "SQL语法有效",
      "columnMetadata": [
        {
          "name": "customer_id",
          "type": "INTEGER"
        },
        {
          "name": "customer_name",
          "type": "VARCHAR"
        },
        {
          "name": "order_count",
          "type": "BIGINT"
        }
      ]
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: SQL语法错误或参数不合法

#### 2.3.3 保存查询

- **URL**: `/api/v1/queries`
- **方法**: `POST`
- **描述**: 保存查询定义
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "name": "活跃客户订单统计",
    "description": "统计活跃客户的订单数量",
    "dataSourceId": 1,
    "sql": "SELECT c.customer_id, c.customer_name, COUNT(o.order_id) AS order_count FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE c.status = ? GROUP BY c.customer_id, c.customer_name ORDER BY order_count DESC LIMIT ?",
    "parameters": [
      {
        "name": "status",
        "type": "STRING",
        "defaultValue": "active",
        "required": true,
        "description": "客户状态"
      },
      {
        "name": "limit",
        "type": "INTEGER",
        "defaultValue": "10",
        "required": false,
        "description": "结果数量限制"
      }
    ],
    "shared": true,
    "tags": ["客户", "订单", "统计"]
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "id": 1,
      "name": "活跃客户订单统计",
      "description": "统计活跃客户的订单数量",
      "dataSourceId": 1,
      "sql": "SELECT c.customer_id, c.customer_name, COUNT(o.order_id) AS order_count FROM customers c LEFT JOIN orders o ON c.customer_id = o.customer_id WHERE c.status = ? GROUP BY c.customer_id, c.customer_name ORDER BY order_count DESC LIMIT ?",
      "parameters": [
        {
          "name": "status",
          "type": "STRING",
          "defaultValue": "active",
          "required": true,
          "description": "客户状态"
        },
        {
          "name": "limit",
          "type": "INTEGER",
          "defaultValue": "10",
          "required": false,
          "description": "结果数量限制"
        }
      ],
      "shared": true,
      "tags": ["客户", "订单", "统计"],
      "createdBy": "user1",
      "createdAt": "2025-03-15T14:30:00Z",
      "updatedAt": "2025-03-15T14:30:00Z"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 数据源不存在
  - `409 Conflict`: 查询名称已存在

#### 2.3.4 执行保存的查询

- **URL**: `/api/v1/queries/{id}/execute`
- **方法**: `POST`
- **描述**: 执行已保存的查询
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "parameters": {
      "status": "active",
      "limit": 20
    },
    "options": {
      "timeout": 30,
      "maxRows": 1000
    }
  }
  ```
- **响应**: 与执行SQL查询响应相同
- **错误码**:
  - `400 Bad Request`: 参数不合法
  - `404 Not Found`: 查询不存在

#### 2.3.5 获取查询历史

- **URL**: `/api/v1/queries/history`
- **方法**: `GET`
- **描述**: 获取用户查询历史
- **权限**: `USER`, `ADMIN`
- **查询参数**:
  - `page`: 页码，默认0
  - `size`: 每页条数，默认20
  - `dataSourceId`: 按数据源筛选
  - `startDate`: 开始日期
  - `endDate`: 结束日期
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "content": [
        {
          "id": 1,
          "dataSourceId": 1,
          "dataSourceName": "销售数据库",
          "sql": "SELECT * FROM customers LIMIT 10",
          "executionTime": 120,
          "resultRows": 10,
          "status": "COMPLETED",
          "executedAt": "2025-03-15T14:00:00Z"
        },
        // 更多历史记录...
      ],
      // 分页信息...与之前格式相同
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```

### 2.4 自然语言查询API

#### 2.4.1 自然语言转SQL

- **URL**: `/api/v1/nl2sql`
- **方法**: `POST`
- **描述**: 将自然语言转换为SQL查询
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "dataSourceId": 1,
    "naturalLanguage": "查询上个月销售额最高的前5个产品及其销售额",
    "context": {
      "recentTables": ["products", "orders", "order_items"]
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "sql": "SELECT p.product_id, p.product_name, SUM(oi.quantity * oi.unit_price) AS total_sales FROM products p JOIN order_items oi ON p.product_id = oi.product_id JOIN orders o ON oi.order_id = o.order_id WHERE o.order_date >= DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND o.order_date < DATE_FORMAT(CURRENT_DATE(), '%Y-%m-01') GROUP BY p.product_id, p.product_name ORDER BY total_sales DESC LIMIT 5",
      "confidence": 0.92,
      "understanding": "查询上个月（上个自然月）销售额（数量*单价）最高的前5个产品及其销售额",
      "schemaUsed": ["products", "orders", "order_items"],
      "alternativeSQLs": [
        {
          "sql": "SELECT p.product_id, p.product_name, SUM(oi.quantity * oi.unit_price) AS total_sales FROM products p JOIN order_items oi ON p.product_id = oi.product_id JOIN orders o ON oi.order_id = o.order_id WHERE o.order_date >= DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH) GROUP BY p.product_id, p.product_name ORDER BY total_sales DESC LIMIT 5",
          "explanation": "使用相对日期计算（过去30天）而非自然月"
        }
      ]
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 数据源不存在
  - `422 Unprocessable Entity`: 无法理解自然语言查询

#### 2.4.2 执行自然语言查询

- **URL**: `/api/v1/nl2sql/execute`
- **方法**: `POST`
- **描述**: 将自然语言转换为SQL并执行
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "dataSourceId": 1,
    "naturalLanguage": "查询上个月销售额最高的前5个产品及其销售额",
    "options": {
      "timeout": 30,
      "maxRows": 1000
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "sql": "SELECT p.product_id, p.product_name, SUM(oi.quantity * oi.unit_price) AS total_sales FROM products p JOIN order_items oi ON p.product_id = oi.product_id JOIN orders o ON oi.order_id = o.order_id WHERE o.order_date >= DATE_FORMAT(DATE_SUB(CURRENT_DATE(), INTERVAL 1 MONTH), '%Y-%m-01') AND o.order_date < DATE_FORMAT(CURRENT_DATE(), '%Y-%m-01') GROUP BY p.product_id, p.product_name ORDER BY total_sales DESC LIMIT 5",
      "columns": [
        {
          "name": "product_id",
          "type": "INTEGER",
          "label": "product_id"
        },
        {
          "name": "product_name",
          "type": "VARCHAR",
          "label": "product_name"
        },
        {
          "name": "total_sales",
          "type": "DECIMAL",
          "label": "total_sales"
        }
      ],
      "rows": [
        [1024, "高级办公椅", 125600.00],
        [2035, "笔记本电脑Pro", 98750.00],
        [3012, "智能手机X", 87320.00],
        [4056, "会议室显示器", 65400.00],
        [5078, "无线耳机", 52180.00]
      ],
      "rowCount": 5,
      "executionTime": 345,
      "hasMoreData": false,
      "confidence": 0.92
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 数据源不存在
  - `422 Unprocessable Entity`: 无法理解自然语言查询

### 2.5 低代码集成API

#### 2.5.1 创建低代码配置

- **URL**: `/api/v1/lowcode/configs`
- **方法**: `POST`
- **描述**: 创建低代码平台集成配置
- **权限**: `ADMIN`, `DEVELOPER`
- **请求体**:
  ```json
  {
    "name": "客户订单明细",
    "description": "客户订单明细查询和展示",
    "queryId": 1,
    "formConfig": {
      "layout": "standard",
      "fields": [
        {
          "id": "status",
          "type": "select",
          "label": "客户状态",
          "required": true,
          "options": [
            { "value": "active", "label": "活跃" },
            { "value": "inactive", "label": "非活跃" }
          ],
          "defaultValue": "active"
        },
        {
          "id": "limit",
          "type": "number",
          "label": "显示条数",
          "required": false,
          "min": 1,
          "max": 100,
          "defaultValue": 10
        }
      ]
    },
    "displayConfig": {
      "type": "table",
      "columns": [
        {
          "field": "customer_id",
          "header": "客户ID",
          "width": 100,
          "sortable": true
        },
        {
          "field": "customer_name",
          "header": "客户名称",
          "width": 200,
          "sortable": true
        },
        {
          "field": "order_count",
          "header": "订单数量",
          "width": 150,
          "sortable": true,
          "format": "number"
        }
      ],
      "operations": [
        {
          "type": "button",
          "label": "查看详情",
          "action": "view",
          "icon": "eye"
        }
      ]
    },
    "chartConfig": {
      "type": "bar",
      "xField": "customer_name",
      "yField": "order_count",
      "title": "客户订单统计",
      "color": "#4c51bf"
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "id": 1,
      "name": "客户订单明细",
      "description": "客户订单明细查询和展示",
      "queryId": 1,
      "formConfig": { /* 同请求 */ },
      "displayConfig": { /* 同请求 */ },
      "chartConfig": { /* 同请求 */ },
      "createdBy": "user1",
      "createdAt": "2025-03-15T14:30:00Z",
      "updatedAt": "2025-03-15T14:30:00Z"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 查询不存在
  - `409 Conflict`: 配置名称已存在

#### 2.5.2 AI辅助配置生成

- **URL**: `/api/v1/lowcode/ai-suggestion`
- **方法**: `POST`
- **描述**: 获取AI辅助的低代码配置建议
- **权限**: `ADMIN`, `DEVELOPER`
- **请求体**:
  ```json
  {
    "queryId": 1,
    "preferredDisplayType": "table", // table, chart, form, card
    "description": "我需要一个客户订单统计的可视化表格，突出显示高价值客户"
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "formConfig": { /* 表单配置建议 */ },
      "displayConfig": { /* 显示配置建议 */ },
      "chartConfig": { /* 图表配置建议 */ },
      "explanation": "根据查询结果的列结构和统计性质，建议使用表格视图显示客户基本信息，并使用条形图展示订单数量。表格中高价值客户（订单数量较多）将使用不同的背景颜色突出显示。"
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 查询不存在

#### 2.5.3 渲染低代码配置

- **URL**: `/api/v1/lowcode/configs/{id}/render`
- **方法**: `POST`
- **描述**: 根据低代码配置执行查询并渲染结果
- **权限**: `USER`, `ADMIN`
- **请求体**:
  ```json
  {
    "parameters": {
      "status": "active",
      "limit": 20
    }
  }
  ```
- **响应**:
  ```json
  {
    "success": true,
    "data": {
      "configId": 1,
      "configName": "客户订单明细",
      "queryResult": {
        "columns": [ /* 列信息 */ ],
        "rows": [ /* 行数据 */ ],
        "rowCount": 20,
        "executionTime": 245
      },
      "renderedConfig": {
        "form": { /* 表单渲染配置 */ },
        "display": { /* 显示渲染配置 */ },
        "chart": { /* 图表渲染配置 */ }
      }
    },
    "timestamp": "2025-03-15T14:30:00Z"
  }
  ```
- **错误码**:
  - `400 Bad Request`: 请求参数不合法
  - `404 Not Found`: 配置不存在

## 3. 错误码定义

| 错误码 | 错误类型 | 错误描述 |
|--------|---------|---------|
| `INVALID_REQUEST` | 400 Bad Request | 请求参数不合法 |
| `AUTHENTICATION_FAILED` | 401 Unauthorized | 认证失败 |
| `PERMISSION_DENIED` | 403 Forbidden | 权限不足 |
| `RESOURCE_NOT_FOUND` | 404 Not Found | 资源不存在 |
| `METHOD_NOT_ALLOWED` | 405 Method Not Allowed | 方法不允许 |
| `CONFLICT` | 409 Conflict | 资源冲突 |
| `QUERY_TIMEOUT` | 408 Request Timeout | 查询超时 |
| `UNPROCESSABLE_ENTITY` | 422 Unprocessable Entity | 请求参数语义错误 |
| `INTERNAL_SERVER_ERROR` | 500 Internal Server Error | 服务器内部错误 |
| `SERVICE_UNAVAILABLE` | 503 Service Unavailable | 服务不可用 |

### 3.1 数据源相关错误

| 错误码 | 错误描述 |
|--------|---------|
| `DATASOURCE_NOT_FOUND` | 数据源不存在 |
| `DATASOURCE_ALREADY_EXISTS` | 数据源名称已存在 |
| `DATASOURCE_CONNECTION_FAILED` | 数据源连接失败 |
| `DATASOURCE_IN_USE` | 数据源正在被使用 |
| `INVALID_DATASOURCE_TYPE` | 不支持的数据源类型 |
| `DATASOURCE_CREDENTIALS_INVALID` | 数据源凭证无效 |

### 3.2 元数据相关错误

| 错误码 | 错误描述 |
|--------|---------|
| `SCHEMA_NOT_FOUND` | 模式不存在 |
| `TABLE_NOT_FOUND` | 表不存在 |
| `COLUMN_NOT_FOUND` | 列不存在 |
| `METADATA_SYNC_IN_PROGRESS` | 元数据同步正在进行 |
| `METADATA_SYNC_FAILED` | 元数据同步失败 |
| `METADATA_SYNC_JOB_NOT_FOUND` | 元数据同步任务不存在 |

### 3.3 查询相关错误

| 错误码 | 错误描述 |
|--------|---------|
| `QUERY_SYNTAX_ERROR` | SQL语法错误 |
| `QUERY_EXECUTION_FAILED` | 查询执行失败 |
| `QUERY_TIMEOUT` | 查询超时 |
| `QUERY_NOT_FOUND` | 查询不存在 |
| `QUERY_ALREADY_EXISTS` | 查询名称已存在 |
| `INVALID_QUERY_PARAMETER` | 查询参数无效 |

### 3.4 自然语言查询相关错误

| 错误码 | 错误描述 |
|--------|---------|
| `NL_PARSE_FAILED` | 自然语言解析失败 |
| `NL_NOT_SUPPORTED` | 不支持的自然语言查询 |
| `NL_AMBIGUOUS` | 自然语言查询不明确 |
| `NL_SERVICE_UNAVAILABLE` | 自然语言服务不可用 |

### 3.5 低代码相关错误

| 错误码 | 错误描述 |
|--------|---------|
| `CONFIG_NOT_FOUND` | 配置不存在 |
| `CONFIG_ALREADY_EXISTS` | 配置名称已存在 |
| `INVALID_CONFIG` | 配置无效 |
| `RENDER_FAILED` | 渲染失败 |

## 4. API安全

### 4.1 认证

所有API请求都需要进行认证，系统支持以下认证方式：

1. **JWT Token认证**：
   - 用户登录后获取JWT Token
   - 在请求头中添加`Authorization: Bearer <token>`

2. **API密钥认证**（用于第三方集成）：
   - 在请求头中添加`X-API-Key: <api-key>`

### 4.2 授权

系统采用基于角色的访问控制（RBAC），主要角色包括：

1. **USER**：普通用户，可以查询数据和使用已配置的功能
2. **DEVELOPER**：开发者，可以创建和管理低代码配置
3. **DATASOURCE_MANAGER**：数据源管理员，可以管理数据源
4. **ADMIN**：系统管理员，拥有所有权限

### 4.3 请求限制

为防止滥用，系统实施以下请求限制：

1. **频率限制**：
   - 普通用户：每分钟60个请求
   - 开发者：每分钟120个请求
   - 管理员：每分钟300个请求

2. **查询限制**：
   - 查询超时限制：默认30秒，最大300秒
   - 结果集行数限制：默认1000行，最大50000行

3. **并发限制**：
   - 每用户最大并发查询数：5

### 4.4 敏感数据处理

1. **数据源密码**：
   - 传输中加密（HTTPS）
   - 存储时加密（AES-256）
   - 响应中不返回明文密码

2. **查询结果**：
   - 支持列级别的数据脱敏
   - 支持行级别的数据过滤

## 5. API性能优化

### 5.1 缓存策略

1. **元数据缓存**：
   - 缓存数据源元数据结构
   - 缓存期：24小时或手动刷新

2. **查询结果缓存**：
   - 对相同参数的查询结果缓存
   - 缓存期：可配置，默认10分钟
   - 支持手动刷新

3. **ETags支持**：
   - 使用ETag减少不必要的数据传输

### 5.2 压缩

所有API响应支持以下压缩方式：
- gzip
- deflate
- br (Brotli)

### 5.3 分页和部分响应

1. **分页**：
   - 所有列表接口支持分页
   - 默认页大小：20
   - 最大页大小：100

2. **字段过滤**：
   - 支持通过`fields`参数指定需要返回的字段
   - 例如：`/api/v1/datasources?fields=id,name,type`

## 6. API监控与治理

### 6.1 指标收集

系统收集以下API指标：

1. **性能指标**：
   - 请求响应时间
   - 查询执行时间
   - 资源使用情况

2. **使用指标**：
   - API调用频率
   - 错误率
   - 并发用户数

### 6.2 日志记录

系统记录以下日志：

1. **访问日志**：
   - 请求IP
   - 用户ID
   - 请求路径
   - 响应状态码
   - 响应时间

2. **错误日志**：
   - 错误类型
   - 错误堆栈
   - 错误上下文

### 6.3 告警机制

系统配置以下告警：

1. **性能告警**：
   - 响应时间超过阈值
   - 错误率超过阈值

2. **安全告警**：
   - 异常访问模式
   - 认证失败次数过多

## 7. API文档

### 7.1 Swagger/OpenAPI

系统提供基于OpenAPI 3.0的API文档：

- 访问URL：`/swagger-ui.html`
- 文档特性：
  - 交互式API测试
  - 请求/响应示例
  - 模型定义
  - 权限要求

### 7.2 API示例

系统提供丰富的API示例代码：

- cURL
- Java
- JavaScript
- Python

## 8. API变更管理

### 8.1 版本策略

1. **主版本**：
   - 不兼容的API更改
   - 例如：`/api/v2/...`

2. **次版本**：
   - 向后兼容的功能添加
   - 通过文档和响应头通知

3. **补丁版本**：
   - 向后兼容的错误修复
   - 不需要版本标识

### 8.2 弃用流程

1. **弃用通知**：
   - 提前3个月通知API弃用
   - 在响应头中添加弃用警告

2. **过渡期**：
   - 同时支持旧API和新API
   - 提供迁移指南

3. **下线**：
   - 弃用期结束后下线
   - 返回明确的错误消息指向新API