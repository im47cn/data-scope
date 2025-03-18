# 数据源管理模块实现详细规划

## 1. 概述

数据源管理是DataScope系统的基础功能，它允许用户连接、配置和管理各种数据库，为后续的查询构建和数据分析提供数据基础。本文档详细规划了数据源管理模块的前端实现和前后端联调方案。

## 2. 功能列表

### 2.1 核心功能

1. **数据源CRUD操作**：创建、读取、更新和删除数据源配置
2. **数据源连接测试**：验证数据源连接是否有效
3. **元数据同步**：从数据源提取模式、表、列等元数据
4. **元数据浏览**：浏览和搜索数据源的元数据
5. **数据源状态监控**：监控数据源的连接状态和性能指标

### 2.2 辅助功能

1. **数据源分类和标签**：对数据源进行分类和标记
2. **数据源搜索和筛选**：根据名称、类型、标签等条件搜索数据源
3. **连接历史记录**：记录和显示数据源的连接历史
4. **元数据同步历史**：记录和显示元数据同步的历史记录
5. **数据源使用统计**：统计和显示数据源的使用情况

## 3. 组件设计

### 3.1 数据源列表页面

#### 3.1.1 组件结构

```
DataSourceList
├── DataSourceFilter (筛选和搜索)
├── DataSourceTable (数据源列表表格)
│   └── DataSourceStatusIndicator (状态指示器)
└── ActionBar (操作栏：新建、批量删除等)
```

#### 3.1.2 功能点

1. **分页表格**：显示数据源基本信息，支持分页、排序
2. **状态指示器**：显示数据源连接状态（在线、离线、异常等）
3. **快捷操作**：每行提供编辑、删除、测试连接等操作
4. **搜索筛选**：按名称、类型、标签等条件筛选数据源
5. **批量操作**：支持批量删除、批量测试连接等操作

#### 3.1.3 API接口

1. **获取数据源列表**：`GET /api/datasources?page={page}&size={size}&sort={sort}&filter={filter}`
2. **删除数据源**：`DELETE /api/datasources/{id}`
3. **测试数据源连接**：`POST /api/datasources/{id}/test-connection`
4. **批量删除数据源**：`DELETE /api/datasources`

### 3.2 数据源表单（创建/编辑）

#### 3.2.1 组件结构

```
DataSourceForm
├── BasicInfoSection (基本信息)
├── ConnectionSection (连接信息，根据数据库类型动态变化)
├── AdvancedSection (高级设置)
├── TestConnectionButton (测试连接按钮)
└── FormActions (表单操作：保存、取消等)
```

#### 3.2.2 功能点

1. **基本信息表单**：名称、描述、类型、标签等
2. **连接信息表单**：主机、端口、用户名、密码、数据库名等，根据数据库类型动态变化
3. **高级设置**：连接池设置、超时设置、SSL配置等
4. **表单验证**：必填项检查、格式验证、唯一性检查等
5. **测试连接**：在保存前测试连接是否有效

#### 3.2.3 API接口

1. **创建数据源**：`POST /api/datasources`
2. **更新数据源**：`PUT /api/datasources/{id}`
3. **获取数据源详情**：`GET /api/datasources/{id}`
4. **测试连接**：`POST /api/datasources/test-connection`（创建前）或 `POST /api/datasources/{id}/test-connection`（更新时）

### 3.3 数据源详情页面

#### 3.3.1 组件结构

```
DataSourceDetail
├── DataSourceInfo (基本信息和连接信息)
├── MetadataStats (元数据统计)
├── ConnectionHistory (连接历史)
├── SyncHistory (同步历史)
└── ActionBar (操作栏：编辑、删除、同步元数据等)
```

#### 3.3.2 功能点

1. **基本信息展示**：显示数据源的基本信息和连接信息
2. **元数据统计**：显示模式数、表数、视图数等统计信息
3. **连接历史**：显示近期连接记录和状态
4. **同步历史**：显示元数据同步的历史记录和结果
5. **操作按钮**：提供编辑、删除、同步元数据、浏览元数据等操作

#### 3.3.3 API接口

1. **获取数据源详情**：`GET /api/datasources/{id}`
2. **获取元数据统计**：`GET /api/datasources/{id}/metadata/stats`
3. **获取连接历史**：`GET /api/datasources/{id}/connection-history`
4. **获取同步历史**：`GET /api/datasources/{id}/sync-history`
5. **触发元数据同步**：`POST /api/datasources/{id}/sync-metadata`

### 3.4 元数据浏览组件

#### 3.4.1 组件结构

```
MetadataBrowser
├── SchemaSelector (模式选择器)
├── MetadataTree (元数据树，显示表/视图层级)
├── MetadataSearch (元数据搜索)
└── MetadataDetail (元数据详情，显示表结构等)
```

#### 3.4.2 功能点

1. **模式选择**：选择要浏览的数据库模式
2. **树形浏览**：以树形结构展示表、视图等对象
3. **元数据搜索**：按名称搜索表、列等元数据
4. **表结构查看**：查看表的列定义、索引、约束等
5. **数据预览**：预览表中的数据（有限行数）

#### 3.4.3 API接口

1. **获取模式列表**：`GET /api/datasources/{id}/schemas`
2. **获取表列表**：`GET /api/datasources/{id}/schemas/{schema}/tables`
3. **获取表详情**：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}`
4. **获取表数据**：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}/data?limit={limit}`
5. **搜索元数据**：`GET /api/datasources/{id}/metadata/search?keyword={keyword}`

## 4. 数据流程

### 4.1 数据源管理流程

1. **创建数据源**：
   - 用户填写数据源表单
   - 用户点击"测试连接"按钮
   - 前端发起测试连接请求
   - 后端验证连接并返回结果
   - 连接成功后，用户点击"保存"
   - 前端发起创建数据源请求
   - 后端创建数据源并返回结果
   - 前端显示成功消息并跳转到数据源列表或详情页

2. **更新数据源**：
   - 用户从列表或详情页进入编辑页面
   - 前端加载数据源详情
   - 用户修改表单数据
   - 用户点击"测试连接"按钮
   - 前端发起测试连接请求
   - 后端验证连接并返回结果
   - 连接成功后，用户点击"保存"
   - 前端发起更新数据源请求
   - 后端更新数据源并返回结果
   - 前端显示成功消息并跳转到数据源列表或详情页

3. **删除数据源**：
   - 用户在列表或详情页点击"删除"按钮
   - 前端显示确认对话框
   - 用户确认删除
   - 前端发起删除数据源请求
   - 后端删除数据源并返回结果
   - 前端显示成功消息并更新列表或跳转到列表页

### 4.2 元数据同步流程

1. **手动触发同步**：
   - 用户在数据源详情页点击"同步元数据"按钮
   - 前端发起同步请求
   - 后端创建同步任务并返回任务ID
   - 前端轮询同步任务状态
   - 同步完成后，前端更新元数据统计和同步历史
   - 前端显示同步结果消息

2. **自动同步**：
   - 系统定时检查需要同步的数据源
   - 后端创建同步任务
   - 同步完成后，更新元数据统计和同步历史
   - 如果配置了通知，向相关用户发送通知

### 4.3 元数据浏览流程

1. **选择数据源和模式**：
   - 用户选择数据源
   - 前端加载该数据源的模式列表
   - 用户选择模式
   - 前端加载该模式的表/视图列表

2. **浏览元数据**：
   - 用户在树形结构中导航或使用搜索
   - 前端根据用户操作加载相应的元数据
   - 用户选择表/视图
   - 前端加载表/视图的详细信息（列、索引等）

3. **预览数据**：
   - 用户点击"预览数据"按钮
   - 前端发起数据预览请求
   - 后端执行查询并返回有限行数据
   - 前端显示数据预览表格

## 5. 实现步骤

### 5.1 阶段一：数据源列表和基本操作（5天）

#### 任务5.1.1：创建数据源列表组件（2天）
1. 创建DataSourceFilter组件（筛选和搜索）
2. 创建DataSourceTable组件（数据源列表表格）
3. 创建DataSourceStatusIndicator组件（状态指示器）
4. 创建ActionBar组件（操作栏）
5. 整合以上组件到DataSourceList页面

#### 任务5.1.2：实现API服务和数据流（1天）
1. 创建数据源API服务
2. 实现获取数据源列表的数据流
3. 实现分页、排序和筛选功能

#### 任务5.1.3：实现删除和测试连接功能（2天）
1. 实现单个删除功能
2. 实现批量删除功能
3. 实现测试连接功能
4. 添加操作反馈和确认对话框

### 5.2 阶段二：数据源表单（创建/编辑）（5天）

#### 任务5.2.1：创建基本表单组件（2天）
1. 创建BasicInfoSection组件（基本信息）
2. 创建ConnectionSection组件（连接信息）
3. 创建AdvancedSection组件（高级设置）
4. 创建FormActions组件（表单操作）

#### 任务5.2.2：实现动态表单和验证（2天）
1. 实现根据数据库类型动态变化的表单
2. 实现表单验证规则
3. 实现提交前验证

#### 任务5.2.3：实现API集成和测试连接（1天）
1. 实现创建数据源API集成
2. 实现更新数据源API集成
3. 实现测试连接功能
4. 添加操作反馈和页面跳转

### 5.3 阶段三：数据源详情页面（3天）

#### 任务5.3.1：创建详情页面组件（1天）
1. 创建DataSourceInfo组件（基本信息）
2. 创建MetadataStats组件（元数据统计）
3. 创建ConnectionHistory组件（连接历史）
4. 创建SyncHistory组件（同步历史）

#### 任务5.3.2：实现API集成和数据显示（1天）
1. 实现获取详情API集成
2. 实现获取统计数据API集成
3. 实现获取历史记录API集成

#### 任务5.3.3：实现操作功能（1天）
1. 实现编辑、删除功能
2. 实现同步元数据功能
3. 实现浏览元数据入口

### 5.4 阶段四：元数据浏览组件（5天）

#### 任务5.4.1：创建元数据浏览基础组件（2天）
1. 创建SchemaSelector组件（模式选择器）
2. 创建MetadataTree组件（元数据树）
3. 创建MetadataSearch组件（元数据搜索）
4. 创建MetadataDetail组件（元数据详情）

#### 任务5.4.2：实现元数据加载和显示（2天）
1. 实现模式列表加载
2. 实现表/视图列表加载
3. 实现表/视图详情加载
4. 实现元数据搜索功能

#### 任务5.4.3：实现数据预览功能（1天）
1. 创建DataPreview组件（数据预览）
2. 实现数据预览API集成
3. 实现数据分页和排序

## 6. 前后端接口定义

### 6.1 数据源管理接口

#### 6.1.1 获取数据源列表
- 请求：`GET /api/datasources`
- 参数：
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认10）
  - `sort`: 排序字段和方向（如`name,asc`）
  - `filter`: 筛选条件（JSON格式）
- 响应：
```json
{
  "content": [
    {
      "id": "string",
      "name": "string",
      "type": "MYSQL",
      "host": "string",
      "port": 3306,
      "databaseName": "string",
      "username": "string",
      "enabled": true,
      "description": "string",
      "tags": ["string"],
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z",
      "lastConnectedAt": "2023-01-01T00:00:00Z",
      "lastSyncedAt": "2023-01-01T00:00:00Z",
      "status": "ONLINE"
    }
  ],
  "pageable": {
    "page": 0,
    "size": 10,
    "sort": "name,asc",
    "totalElements": 100,
    "totalPages": 10
  }
}
```

#### 6.1.2 创建数据源
- 请求：`POST /api/datasources`
- 请求体：
```json
{
  "name": "string",
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "password": "string",
  "connectionProperties": {},
  "enabled": true,
  "description": "string",
  "tags": ["string"]
}
```
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "enabled": true,
  "description": "string",
  "tags": ["string"],
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

#### 6.1.3 获取数据源详情
- 请求：`GET /api/datasources/{id}`
- 参数：
  - `id`: 数据源ID
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "connectionProperties": {},
  "enabled": true,
  "description": "string",
  "tags": ["string"],
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z",
  "lastConnectedAt": "2023-01-01T00:00:00Z",
  "lastSyncedAt": "2023-01-01T00:00:00Z",
  "status": "ONLINE",
  "metadataStats": {
    "schemaCount": 10,
    "tableCount": 100,
    "viewCount": 20,
    "columnCount": 1000
  }
}
```

#### 6.1.4 更新数据源
- 请求：`PUT /api/datasources/{id}`
- 参数：
  - `id`: 数据源ID
- 请求体：
```json
{
  "name": "string",
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "password": "string",
  "connectionProperties": {},
  "enabled": true,
  "description": "string",
  "tags": ["string"]
}
```
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "enabled": true,
  "description": "string",
  "tags": ["string"],
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

#### 6.1.5 删除数据源
- 请求：`DELETE /api/datasources/{id}`
- 参数：
  - `id`: 数据源ID
- 响应：
```json
{
  "success": true,
  "message": "数据源删除成功"
}
```

#### 6.1.6 测试数据源连接
- 请求：`POST /api/datasources/test-connection`（创建前）或 `POST /api/datasources/{id}/test-connection`（更新时）
- 请求体：
```json
{
  "type": "MYSQL",
  "host": "string",
  "port": 3306,
  "databaseName": "string",
  "username": "string",
  "password": "string",
  "connectionProperties": {}
}
```
- 响应：
```json
{
  "success": true,
  "message": "连接成功",
  "databaseVersion": "MySQL 8.0.26",
  "driverVersion": "MySQL Connector/J 8.0.26"
}
```

#### 6.1.7 批量删除数据源
- 请求：`DELETE /api/datasources`
- 请求体：
```json
{
  "ids": ["string"]
}
```
- 响应：
```json
{
  "success": true,
  "message": "成功删除3个数据源",
  "failedIds": []
}
```

### 6.2 元数据同步接口

#### 6.2.1 触发元数据同步
- 请求：`POST /api/datasources/{id}/sync-metadata`
- 参数：
  - `id`: 数据源ID
- 请求体：
```json
{
  "syncType": "FULL",
  "schemas": ["string"]
}
```
- 响应：
```json
{
  "jobId": "string",
  "status": "PENDING",
  "message": "同步任务已创建",
  "createdAt": "2023-01-01T00:00:00Z"
}
```

#### 6.2.2 获取同步任务状态
- 请求：`GET /api/metadata-sync-jobs/{jobId}`
- 参数：
  - `jobId`: 同步任务ID
- 响应：
```json
{
  "jobId": "string",
  "dataSourceId": "string",
  "syncType": "FULL",
  "status": "RUNNING",
  "progress": 50,
  "message": "正在同步表结构",
  "startTime": "2023-01-01T00:00:00Z",
  "endTime": null,
  "schemas": ["string"],
  "syncedSchemas": 5,
  "totalSchemas": 10,
  "syncedTables": 50,
  "totalTables": 100
}
```

#### 6.2.3 获取同步历史
- 请求：`GET /api/datasources/{id}/sync-history`
- 参数：
  - `id`: 数据源ID
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认10）
- 响应：
```json
{
  "content": [
    {
      "jobId": "string",
      "dataSourceId": "string",
      "syncType": "FULL",
      "status": "SUCCESS",
      "message": "同步完成",
      "startTime": "2023-01-01T00:00:00Z",
      "endTime": "2023-01-01T00:05:00Z",
      "schemas": ["string"],
      "syncedSchemas": 10,
      "totalSchemas": 10,
      "syncedTables": 100,
      "totalTables": 100
    }
  ],
  "pageable": {
    "page": 0,
    "size": 10,
    "totalElements": 50,
    "totalPages": 5
  }
}
```

### 6.3 元数据浏览接口

#### 6.3.1 获取模式列表
- 请求：`GET /api/datasources/{id}/schemas`
- 参数：
  - `id`: 数据源ID
- 响应：
```json
{
  "schemas": [
    {
      "name": "string",
      "tableCount": 10,
      "viewCount": 5,
      "routineCount": 3,
      "default": true
    }
  ]
}
```

#### 6.3.2 获取表列表
- 请求：`GET /api/datasources/{id}/schemas/{schema}/tables`
- 参数：
  - `id`: 数据源ID
  - `schema`: 模式名称
  - `type`: 对象类型（TABLE, VIEW, ALL，默认ALL）
- 响应：
```json
{
  "tables": [
    {
      "name": "string",
      "type": "TABLE",
      "columnCount": 10,
      "rowCount": 1000,
      "sizeKB": 1024,
      "createTime": "2023-01-01T00:00:00Z",
      "updateTime": "2023-01-01T00:00:00Z",
      "comment": "string"
    }
  ]
}
```

#### 6.3.3 获取表详情
- 请求：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}`
- 参数：
  - `id`: 数据源ID
  - `schema`: 模式名称
  - `table`: 表名称
- 响应：
```json
{
  "name": "string",
  "type": "TABLE",
  "schema": "string",
  "comment": "string",
  "createTime": "2023-01-01T00:00:00Z",
  "updateTime": "2023-01-01T00:00:00Z",
  "columns": [
    {
      "name": "string",
      "type": "VARCHAR",
      "length": 255,
      "precision": 0,
      "scale": 0,
      "nullable": true,
      "defaultValue": "string",
      "primaryKey": false,
      "autoIncrement": false,
      "position": 1,
      "comment": "string"
    }
  ],
  "indexes": [
    {
      "name": "string",
      "unique": true,
      "columns": [
        {
          "name": "string",
          "position": 1,
          "ascending": true
        }
      ]
    }
  ],
  "foreignKeys": [
    {
      "name": "string",
      "referencedSchema": "string",
      "referencedTable": "string",
      "columns": [
        {
          "column": "string",
          "referencedColumn": "string"
        }
      ]
    }
  ],
  "statistics": {
    "rowCount": 1000,
    "sizeKB": 1024,
    "avgRowLength": 100,
    "lastAnalyzed": "2023-01-01T00:00:00Z"
  }
}
```

#### 6.3.4 获取表数据
- 请求：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}/data`
- 参数：
  - `id`: 数据源ID
  - `schema`: 模式名称
  - `table`: 表名称
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认10）
  - `sort`: 排序字段和方向（如`column1,asc`）
- 响应：
```json
{
  "headers": [
    {
      "name": "string",
      "type": "VARCHAR"
    }
  ],
  "data": [
    {
      "column1": "value1",
      "column2": 123
    }
  ],
  "pageable": {
    "page": 0,
    "size": 10,
    "sort": "column1,asc",
    "totalElements": 1000,
    "totalPages": 100
  }
}
```

#### 6.3.5 搜索元数据
- 请求：`GET /api/datasources/{id}/metadata/search`
- 参数：
  - `id`: 数据源ID
  - `keyword`: 搜索关键词
  - `scope`: 搜索范围（ALL, SCHEMA, TABLE, COLUMN，默认ALL）
  - `schemas`: 限定的模式列表，逗号分隔
- 响应：
```json
{
  "schemas": [
    {
      "name": "schema_match",
      "matched": true
    }
  ],
  "tables": [
    {
      "schema": "string",
      "name": "table_match",
      "type": "TABLE",
      "matched": true
    }
  ],
  "columns": [
    {
      "schema": "string",
      "table": "string",
      "name": "column_match",
      "type": "VARCHAR",
      "matched": true
    }
  ]
}
```

## 7. 状态管理设计

### 7.1 全局状态

```javascript
// 数据源模块状态
const dataSourceState = {
  // 数据源列表状态
  list: {
    loading: false,
    data: [],
    pagination: {
      current: 1,
      pageSize: 10,
      total: 0
    },
    filters: {},
    sorter: {
      field: 'createdAt',
      order: 'descend'
    }
  },
  
  // 当前选中的数据源
  current: {
    loading: false,
    data: null,
    metadataStats: null,
    connectionHistory: [],
    syncHistory: []
  },
  
  // 元数据同步状态
  sync: {
    loading: false,
    currentJob: null,
    progress: 0
  },
  
  // 元数据浏览状态
  metadata: {
    loading: false,
    schemas: [],
    currentSchema: null,
    tables: [],
    currentTable: null,
    tableDetail: null,
    tableData: {
      headers: [],
      data: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0
      }
    },
    searchResults: null
  }
};
```

### 7.2 状态管理方法

```javascript
// 数据源列表相关方法
const dataSourceActions = {
  // 加载数据源列表
  async loadDataSources(page, size, filters, sorter) {
    try {
      dataSourceState.list.loading = true;
      const params = {
        page: page - 1,
        size,
        sort: `${sorter.field},${sorter.order === 'ascend' ? 'asc' : 'desc'}`,
        filter: JSON.stringify(filters)
      };
      const response = await dataSourceApi.getDataSources(params);
      dataSourceState.list.data = response.content;
      dataSourceState.list.pagination = {
        current: page,
        pageSize: size,
        total: response.pageable.totalElements
      };
    } catch (error) {
      console.error('Failed to load data sources', error);
      // 显示错误通知
    } finally {
      dataSourceState.list.loading = false;
    }
  },
  
  // 删除数据源
  async deleteDataSource(id) {
    try {
      await dataSourceApi.deleteDataSource(id);
      // 刷新数据源列表
      this.loadDataSources(
        dataSourceState.list.pagination.current,
        dataSourceState.list.pagination.pageSize,
        dataSourceState.list.filters,
        dataSourceState.list.sorter
      );
      // 显示成功通知
    } catch (error) {
      console.error('Failed to delete data source', error);
      // 显示错误通知
    }
  },
  
  // 测试数据源连接
  async testConnection(dataSourceData, existingId = null) {
    try {
      const endpoint = existingId 
        ? dataSourceApi.testExistingConnection(existingId, dataSourceData)
        : dataSourceApi.testNewConnection(dataSourceData);
      const response = await endpoint;
      return {
        success: response.success,
        message: response.message
      };
    } catch (error) {
      console.error('Failed to test connection', error);
      return {
        success: false,
        message: error.message || '连接测试失败'
      };
    }
  }
};
```

## 8. 组件实现示例

### 8.1 数据源列表组件

```vue
<template>
  <div class="datasource-list">
    <div class="page-header">
      <h1>数据源管理</h1>
      <a-button type="primary" @click="handleCreate">
        <a-icon type="plus" /> 新建数据源
      </a-button>
    </div>
    
    <div class="datasource-filter">
      <a-form layout="inline" :model="filters" @submit.prevent="handleSearch">
        <a-form-item label="名称">
          <a-input v-model="filters.name" placeholder="数据源名称" />
        </a-form-item>
        <a-form-item label="类型">
          <a-select v-model="filters.type" placeholder="数据源类型" style="width: 120px">
            <a-select-option value="">全部</a-select-option>
            <a-select-option value="MYSQL">MySQL</a-select-option>
            <a-select-option value="DB2">DB2</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit">
            <a-icon type="search" /> 搜索
          </a-button>
          <a-button style="margin-left: 8px" @click="handleResetFilter">
            <a-icon type="clear" /> 重置
          </a-button>
        </a-form-item>
      </a-form>
    </div>
    
    <a-table
      :columns="columns"
      :dataSource="dataSource"
      :rowKey="record => record.id"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <span slot="status" slot-scope="text, record">
        <a-tag :color="record.status === 'ONLINE' ? 'green' : (record.status === 'OFFLINE' ? 'red' : 'orange')">
          {{ record.status === 'ONLINE' ? '在线' : (record.status === 'OFFLINE' ? '离线' : '异常') }}
        </a-tag>
      </span>
      
      <span slot="type" slot-scope="text">
        <a-tag color="blue">{{ text }}</a-tag>
      </span>
      
      <span slot="action" slot-scope="text, record">
        <a-button type="link" @click="handleView(record)">查看</a-button>
        <a-divider type="vertical" />
        <a-button type="link" @click="handleEdit(record)">编辑</a-button>
        <a-divider type="vertical" />
        <a-popconfirm
          title="确定要删除这个数据源吗？"
          ok-text="确定"
          cancel-text="取消"
          @confirm="handleDelete(record)"
        >
          <a-button type="link">删除</a-button>
        </a-popconfirm>
        <a-divider type="vertical" />
        <a-button type="link" @click="handleTestConnection(record)">测试连接</a-button>
      </span>
    </a-table>
  </div>
</template>

<script>
export default {
  data() {
    return {
      loading: false,
      dataSource: [],
      pagination: {
        current: 1,
        pageSize: 10,
        total: 0,
        showSizeChanger: true,
        showTotal: total => `共 ${total} 项`
      },
      filters: {
        name: '',
        type: ''
      },
      sorter: {
        field: 'createdAt',
        order: 'descend'
      },
      columns: [
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' },
          width: 100
        },
        {
          title: '名称',
          dataIndex: 'name',
          sorter: true
        },
        {
          title: '类型',
          dataIndex: 'type',
          scopedSlots: { customRender: 'type' },
          filters: [
            { text: 'MySQL', value: 'MYSQL' },
            { text: 'DB2', value: 'DB2' }
          ]
        },
        {
          title: '主机',
          dataIndex: 'host'
        },
        {
          title: '数据库',
          dataIndex: 'databaseName'
        },
        {
          title: '最后连接',
          dataIndex: 'lastConnectedAt',
          sorter: true,
          render: (text) => text ? this.$moment(text).format('YYYY-MM-DD HH:mm:ss') : '-'
        },
        {
          title: '最后同步',
          dataIndex: 'lastSyncedAt',
          sorter: true,
          render: (text) => text ? this.$moment(text).format('YYYY-MM-DD HH:mm:ss') : '-'
        },
        {
          title: '操作',
          key: 'action',
          scopedSlots: { customRender: 'action' },
          width: 280
        }
      ]
    };
  },
  mounted() {
    this.fetchDataSources();
  },
  methods: {
    async fetchDataSources() {
      try {
        this.loading = true;
        
        // 构建API请求参数
        const params = {
          page: this.pagination.current - 1,
          size: this.pagination.pageSize,
          sort: `${this.sorter.field},${this.sorter.order === 'ascend' ? 'asc' : 'desc'}`,
          filter: JSON.stringify(this.getFilters())
        };
        
        // 调用API获取数据源列表
        const response = await this.$api.dataSource.getDataSources(params);
        
        // 更新组件状态
        this.dataSource = response.content;
        this.pagination.total = response.pageable.totalElements;
      } catch (error) {
        console.error('Failed to fetch data sources', error);
        this.$message.error('加载数据源列表失败，请重试');
      } finally {
        this.loading = false;
      }
    },
    
    getFilters() {
      const result = {};
      if (this.filters.name) {
        result.name = { $like: `%${this.filters.name}%` };
      }
      if (this.filters.type) {
        result.type = { $eq: this.filters.type };
      }
      return result;
    },
    
    handleTableChange(pagination, filters, sorter) {
      this.pagination.current = pagination.current;
      this.pagination.pageSize = pagination.pageSize;
      
      if (sorter.field && sorter.order) {
        this.sorter.field = sorter.field;
        this.sorter.order = sorter.order;
      }
      
      this.fetchDataSources();
    },
    
    handleSearch() {
      this.pagination.current = 1;
      this.fetchDataSources();
    },
    
    handleResetFilter() {
      this.filters = {
        name: '',
        type: ''
      };
      this.handleSearch();
    },
    
    handleCreate() {
      this.$router.push('/datasource/create');
    },
    
    handleView(record) {
      this.$router.push(`/datasource/${record.id}`);
    },
    
    handleEdit(record) {
      this.$router.push(`/datasource/${record.id}/edit`);
    },
    
    async handleDelete(record) {
      try {
        await this.$api.dataSource.deleteDataSource(record.id);
        this.$message.success('数据源删除成功');
        this.fetchDataSources();
      } catch (error) {
        console.error('Failed to delete data source', error);
        this.$message.error('删除数据源失败，请重试');
      }
    },
    
    async handleTestConnection(record) {
      try {
        this.$message.loading('正在测试连接...', 0);
        const response = await this.$api.dataSource.testConnection(record.id);
        setTimeout(() => {
          this.$message.destroy();
          if (response.success) {
            this.$message.success(`连接成功: ${response.databaseVersion}`);
          } else {
            this.$message.error(`连接失败: ${response.message}`);
          }
        }, 500);
      } catch (error) {
        this.$message.destroy();
        console.error('Failed to test connection', error);
        this.$message.error('测试连接失败，请重试');
      }
    }
  }
};
</script>

<style scoped>
.datasource-list {
  padding: 24px;
}
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}
.datasource-filter {
  margin-bottom: 24px;
  padding: 24px;
  background: #f8f8f8;
  border-radius: 4px;
}
</style>
```

## 9. 测试策略

### 9.1 单元测试

1. **组件渲染测试**：验证组件是否正确渲染，包括初始状态、加载状态和错误状态。
2. **表单验证测试**：验证表单验证规则是否正确实施。
3. **事件处理测试**：验证事件处理函数是否正确响应用户操作。
4. **API调用测试**：使用模拟数据验证API调用是否正确发起。

### 9.2 集成测试

1. **组件集成测试**：验证组件之间的交互是否正常。
2. **路由测试**：验证页面导航和路由参数传递是否正常。
3. **状态管理测试**：验证状态更新和传播是否正确。

### 9.3 端到端测试

1. **用户流程测试**：模拟完整用户流程，如创建数据源、测试连接、查看详情、同步元数据等。
2. **错误处理测试**：模拟各种错误情况，验证系统是否正确处理和提示。
3. **性能测试**：验证加载大量数据时的性能是否符合要求。

## 10. 实施时间表

| 阶段 | 任务 | 开始日期 | 结束日期 | 负责人 |
|------|------|----------|----------|-------|
| 阶段一 | 数据源列表和基本操作 | 2025-03-20 | 2025-03-24 | 前端开发 |
| 阶段二 | 数据源表单（创建/编辑） | 2025-03-25 | 2025-03-29 | 前端开发 |
| 阶段三 | 数据源详情页面 | 2025-03-30 | 2025-04-01 | 前端开发 |
| 阶段四 | 元数据浏览组件 | 2025-04-02 | 2025-04-06 | 前端开发 |
| 阶段五 | 联调和修复 | 2025-04-07 | 2025-04-10 | 前端开发、后端开发 |

## 11. 总结

数据源管理模块是DataScope系统的基础功能，它为后续的查询构建和数据分析提供了数据源。本文档详细规划了数据源管理模块的前端实现和前后端联调方案，包括功能列表、组件设计、数据流程、实现步骤、前后端接口定义、状态管理设计、组件实现示例、测试策略和实施时间表。

通过按照本文档的规划实施，我们可以高效地完成数据源管理模块的开发，并为后续的模块开发奠定基础。