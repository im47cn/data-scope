# 查询构建器模块实现详细规划

## 1. 概述

查询构建器是DataScope系统的核心功能之一，它允许用户通过可视化界面构建SQL查询，无需直接编写SQL代码。本文档详细规划了查询构建器模块的前端实现和前后端联调方案。

## 2. 功能列表

### 2.1 核心功能

1. **可视化查询构建**：通过拖拽和配置构建SQL查询
2. **多表关联查询**：支持多表连接和子查询
3. **条件表达式构建**：支持复杂的条件表达式
4. **排序和分组**：支持结果排序和分组统计
5. **SQL预览**：实时预览生成的SQL
6. **查询执行**：执行查询并显示结果
7. **查询保存和加载**：保存查询配置和加载已保存的查询
8. **查询历史记录**：记录和查看历史查询

### 2.2 辅助功能

1. **智能表关系识别**：自动识别和推荐表关系
2. **查询验证**：验证查询的有效性和性能
3. **查询结果可视化**：以图表形式展示查询结果
4. **查询共享**：共享查询配置给其他用户
5. **查询模板**：常用查询的模板化管理
6. **条件模板**：常用条件表达式的模板化管理
7. **查询性能分析**：分析查询性能并提供优化建议

## 3. 组件设计

### 3.1 查询构建器主界面

#### 3.1.1 组件结构

```
QueryBuilder
├── DataSourceSelector (数据源选择器)
├── SchemaSelector (模式选择器)
├── TableSelector (表选择器)
├── QueryDesigner (查询设计器)
│   ├── TablePanel (表面板)
│   ├── JoinPanel (连接面板)
│   ├── ConditionPanel (条件面板)
│   ├── GroupByPanel (分组面板)
│   └── OrderByPanel (排序面板)
├── SqlPreview (SQL预览)
└── ResultPanel (结果面板)
```

#### 3.1.2 功能点

1. **数据源和模式选择**：选择要查询的数据源和模式
2. **表和列选择**：选择要查询的表和列
3. **表关系配置**：配置表之间的连接关系
4. **条件构建**：构建WHERE条件表达式
5. **分组和聚合**：设置GROUP BY和聚合函数
6. **排序设置**：设置ORDER BY排序规则
7. **SQL预览**：预览生成的SQL语句
8. **结果显示**：显示查询结果

#### 3.1.3 API接口

1. **获取数据源列表**：`GET /api/datasources?active=true`
2. **获取模式列表**：`GET /api/datasources/{id}/schemas`
3. **获取表列表**：`GET /api/datasources/{id}/schemas/{schema}/tables`
4. **获取表详情**：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}`
5. **执行查询**：`POST /api/query/execute`

### 3.2 表选择器组件

#### 3.2.1 组件结构

```
TableSelector
├── TableSearch (表搜索)
├── TableCategoryTabs (表分类标签页)
└── TableList (表列表)
    └── TableListItem (表列表项)
        └── ColumnList (列列表)
```

#### 3.2.2 功能点

1. **表搜索**：按名称搜索表
2. **表分类**：按表类型或用户定义的分类显示表
3. **表列表**：显示可用的表
4. **列选择**：选择要包含在查询中的列
5. **拖拽支持**：支持将表和列拖拽到查询设计器

#### 3.2.3 API接口

1. **获取表列表**：`GET /api/datasources/{id}/schemas/{schema}/tables`
2. **获取表详情**：`GET /api/datasources/{id}/schemas/{schema}/tables/{table}`
3. **搜索表**：`GET /api/datasources/{id}/schemas/{schema}/tables/search?keyword={keyword}`

### 3.3 查询设计器组件

#### 3.3.1 组件结构

```
QueryDesigner
├── TablePanel (表面板)
│   └── TableNode (表节点)
│       └── ColumnList (列列表)
│           └── ColumnItem (列项)
├── JoinPanel (连接面板)
│   └── JoinItem (连接项)
├── ConditionPanel (条件面板)
│   └── ConditionGroup (条件组)
│       └── ConditionItem (条件项)
├── GroupByPanel (分组面板)
│   └── GroupByItem (分组项)
└── OrderByPanel (排序面板)
    └── OrderByItem (排序项)
```

#### 3.3.2 功能点

1. **表节点**：显示选中的表及其列
2. **表关系配置**：配置表之间的连接关系
3. **条件构建**：支持AND/OR嵌套条件
4. **分组设置**：设置分组字段和聚合函数
5. **排序设置**：设置排序字段和方向
6. **拖拽支持**：支持拖拽操作进行配置
7. **布局调整**：支持调整节点位置和大小

#### 3.3.3 相关数据结构

```javascript
// 查询配置数据结构
const queryConfig = {
  // 数据源和模式
  dataSourceId: "string",
  schema: "string",
  
  // 选中的表和列
  tables: [
    {
      id: "string", // 表ID（生成的唯一标识）
      name: "string", // 表名
      alias: "string", // 表别名
      columns: [
        {
          name: "string", // 列名
          alias: "string", // 列别名
          selected: true, // 是否选中
          aggregateFunction: "string" // 聚合函数，如SUM、AVG等
        }
      ]
    }
  ],
  
  // 表连接关系
  joins: [
    {
      id: "string", // 连接ID（生成的唯一标识）
      leftTableId: "string", // 左表ID
      leftColumn: "string", // 左表列名
      rightTableId: "string", // 右表ID
      rightColumn: "string", // 右表列名
      type: "INNER" // 连接类型：INNER, LEFT, RIGHT, FULL
    }
  ],
  
  // 条件表达式
  conditions: {
    logic: "AND", // AND 或 OR
    conditions: [
      {
        // 条件组
        logic: "OR",
        conditions: [
          {
            // 简单条件
            tableId: "string",
            column: "string",
            operator: "=", // 操作符：=, !=, >, <, >=, <=, LIKE, IN, BETWEEN, IS NULL
            value: "any", // 值
            valueType: "string" // 值类型：string, number, boolean, date, etc.
          }
        ]
      },
      {
        // 简单条件
        tableId: "string",
        column: "string",
        operator: "=",
        value: "any",
        valueType: "string"
      }
    ]
  },
  
  // 分组
  groupBy: [
    {
      tableId: "string",
      column: "string"
    }
  ],
  
  // 排序
  orderBy: [
    {
      tableId: "string",
      column: "string",
      direction: "ASC" // ASC 或 DESC
    }
  ],
  
  // 限制
  limit: 100
};
```

### 3.4 SQL预览组件

#### 3.4.1 组件结构

```
SqlPreview
├── SqlEditor (SQL编辑器)
├── FormatButton (格式化按钮)
├── CopyButton (复制按钮)
└── ExecuteButton (执行按钮)
```

#### 3.4.2 功能点

1. **SQL显示**：显示根据查询配置生成的SQL
2. **语法高亮**：支持SQL语法高亮
3. **格式化**：支持SQL格式化
4. **复制**：支持复制SQL到剪贴板
5. **编辑**：支持手动编辑SQL（可选）
6. **执行**：执行显示的SQL

#### 3.4.3 API接口

1. **生成SQL**：`POST /api/query/generate-sql`（根据查询配置生成SQL）
2. **格式化SQL**：`POST /api/query/format-sql`（格式化SQL）
3. **验证SQL**：`POST /api/query/validate-sql`（验证SQL的有效性）

### 3.5 结果面板组件

#### 3.5.1 组件结构

```
ResultPanel
├── ResultTabs (结果标签页)
│   ├── TableTab (表格标签页)
│   │   └── ResultTable (结果表格)
│   └── ChartTab (图表标签页)
│       └── ResultChart (结果图表)
├── ExportButton (导出按钮)
└── PaginationControls (分页控件)
```

#### 3.5.2 功能点

1. **表格显示**：以表格形式显示查询结果
2. **图表显示**：以图表形式显示查询结果（可选）
3. **分页控制**：支持结果分页
4. **排序**：支持点击列头排序
5. **导出**：支持导出结果为CSV或Excel
6. **大数据集优化**：虚拟滚动等优化大数据集的显示

#### 3.5.3 API接口

1. **查询结果分页**：`GET /api/query/{queryId}/results?page={page}&size={size}`
2. **查询结果导出**：`GET /api/query/{queryId}/export?format={format}`

### 3.6 查询管理组件

#### 3.6.1 组件结构

```
QueryManager
├── SaveQueryForm (保存查询表单)
├── LoadQueryButton (加载查询按钮)
│   └── SavedQueryList (已保存查询列表)
├── QueryHistoryButton (查询历史按钮)
│   └── QueryHistoryList (查询历史列表)
└── ShareQueryButton (共享查询按钮)
```

#### 3.6.2 功能点

1. **保存查询**：保存当前查询配置
2. **加载查询**：加载已保存的查询配置
3. **查询历史**：查看和加载历史查询
4. **共享查询**：共享查询给其他用户
5. **收藏查询**：将查询标记为收藏

#### 3.6.3 API接口

1. **保存查询**：`POST /api/saved-queries`
2. **获取已保存查询**：`GET /api/saved-queries`
3. **获取查询历史**：`GET /api/query-history`
4. **共享查询**：`POST /api/saved-queries/{id}/share`

## 4. 数据流程

### 4.1 查询构建流程

1. **选择数据源和模式**：
   - 用户选择数据源
   - 前端加载该数据源的模式列表
   - 用户选择模式
   - 前端加载该模式的表列表

2. **选择表和列**：
   - 用户从表列表中选择表
   - 前端加载表详情（列、索引等）
   - 用户选择要包含在查询中的列
   - 前端将选中的表和列添加到查询配置中

3. **配置表关系**：
   - 用户通过拖拽或选择配置表之间的关系
   - 前端更新查询配置中的连接关系
   - 如有智能关系推荐，前端提示可能的关系

4. **配置查询条件**：
   - 用户添加和配置查询条件
   - 前端更新查询配置中的条件表达式
   - 用户可以添加嵌套条件组和多个条件

5. **配置分组和排序**：
   - 用户设置分组字段和聚合函数
   - 用户设置排序字段和方向
   - 前端更新查询配置中的分组和排序设置

6. **预览SQL**：
   - 前端根据查询配置生成SQL预览
   - 用户可以查看和可选地编辑SQL
   - 前端验证SQL的有效性

7. **执行查询**：
   - 用户点击执行按钮
   - 前端发送查询请求
   - 后端执行查询并返回结果
   - 前端在结果面板中显示结果

### 4.2 查询保存和加载流程

1. **保存查询**：
   - 用户点击保存按钮
   - 前端显示保存查询表单
   - 用户输入查询名称和描述
   - 用户设置是否公开
   - 前端发送保存查询请求
   - 后端保存查询配置
   - 前端显示保存成功消息

2. **加载查询**：
   - 用户点击加载按钮
   - 前端显示已保存查询列表
   - 用户选择要加载的查询
   - 前端发送获取查询配置请求
   - 后端返回查询配置
   - 前端加载查询配置
   - 前端更新查询构建器界面

### 4.3 查询执行和结果处理流程

1. **执行查询**：
   - 用户配置查询并点击执行
   - 前端发送执行查询请求
   - 后端创建查询任务并返回任务ID
   - 前端轮询查询任务状态
   - 查询完成后，前端获取结果
   - 前端在结果面板中显示结果

2. **分页和排序**：
   - 用户在结果面板中导航分页
   - 前端发送分页请求
   - 后端返回指定页的数据
   - 前端更新结果表格
   - 用户点击列头排序
   - 前端发送排序请求
   - 后端返回排序后的数据
   - 前端更新结果表格

3. **导出结果**：
   - 用户点击导出按钮
   - 前端显示导出选项
   - 用户选择导出格式
   - 前端发送导出请求
   - 后端生成导出文件
   - 前端下载导出文件

## 5. 实现步骤

### 5.1 阶段一：查询构建器基础界面（5天）

#### 任务5.1.1：创建主界面框架（2天）
1. 创建QueryBuilder组件（主容器）
2. 创建DataSourceSelector和SchemaSelector组件
3. 实现数据源和模式选择功能
4. 创建基本布局和导航结构

#### 任务5.1.2：创建表选择器组件（1天）
1. 创建TableSelector组件
2. 实现表搜索和过滤功能
3. 实现表列表显示
4. 实现表详情浏览

#### 任务5.1.3：实现API服务和数据流（2天）
1. 创建查询构建器API服务
2. 实现数据源和模式选择的数据流
3. 实现表和列选择的数据流
4. 实现基本状态管理

### 5.2 阶段二：查询设计器实现（4天）

#### 任务5.2.1：创建表面板组件（1天）
1. 创建TablePanel组件
2. 实现表节点显示
3. 实现列选择功能
4. 实现拖拽支持

#### 任务5.2.2：实现表关系配置（1天）
1. 创建JoinPanel组件
2. 实现表关系可视化
3. 实现关系配置界面
4. 实现智能关系推荐

#### 任务5.2.3：创建条件面板组件（2天）
1. 创建ConditionPanel组件
2. 实现条件组和条件项
3. 实现各种操作符的条件编辑器
4. 实现嵌套条件支持

### 5.3 阶段三：查询配置和执行（4天）

#### 任务5.3.1：创建分组和排序面板（1天）
1. 创建GroupByPanel组件
2. 创建OrderByPanel组件
3. 实现分组和聚合功能
4. 实现排序配置功能

#### 任务5.3.2：创建SQL预览组件（1天）
1. 创建SqlPreview组件
2. 实现SQL生成功能
3. 实现SQL格式化和复制功能
4. 实现SQL编辑功能（可选）

#### 任务5.3.3：创建结果面板组件（2天）
1. 创建ResultPanel组件
2. 实现结果表格显示
3. 实现分页和排序功能
4. 实现结果导出功能

### 5.4 阶段四：查询管理功能（3天）

#### 任务5.4.1：创建查询保存和加载功能（1天）
1. 创建SaveQueryForm组件
2. 创建LoadQueryButton和SavedQueryList组件
3. 实现查询保存功能
4. 实现查询加载功能

#### 任务5.4.2：创建查询历史功能（1天）
1. 创建QueryHistoryButton和QueryHistoryList组件
2. 实现查询历史记录
3. 实现历史查询加载功能

#### 任务5.4.3：实现查询共享和收藏功能（1天）
1. 创建ShareQueryButton组件
2. 实现查询共享功能
3. 实现查询收藏功能

## 6. 前后端接口定义

### 6.1 查询构建接口

#### 6.1.1 生成SQL
- 请求：`POST /api/query/generate-sql`
- 请求体：
```json
{
  "queryConfig": {
    "dataSourceId": "string",
    "schema": "string",
    "tables": [...],
    "joins": [...],
    "conditions": {...},
    "groupBy": [...],
    "orderBy": [...],
    "limit": 100
  }
}
```
- 响应：
```json
{
  "sql": "SELECT ... FROM ... WHERE ... GROUP BY ... ORDER BY ... LIMIT ...",
  "parameters": [],
  "valid": true,
  "errors": []
}
```

#### 6.1.2 验证SQL
- 请求：`POST /api/query/validate-sql`
- 请求体：
```json
{
  "dataSourceId": "string",
  "sql": "SELECT ... FROM ..."
}
```
- 响应：
```json
{
  "valid": true,
  "errors": [],
  "warnings": []
}
```

#### 6.1.3 执行查询
- 请求：`POST /api/query/execute`
- 请求体：
```json
{
  "dataSourceId": "string",
  "sql": "SELECT ... FROM ...",
  "parameters": [],
  "maxRows": 1000,
  "timeout": 30000
}
```
- 响应：
```json
{
  "queryId": "string",
  "status": "RUNNING",
  "message": "Query execution started"
}
```

#### 6.1.4 获取查询状态
- 请求：`GET /api/query/{queryId}/status`
- 参数：
  - `queryId`: 查询ID
- 响应：
```json
{
  "queryId": "string",
  "status": "COMPLETED",
  "message": "Query execution completed",
  "progress": 100,
  "startTime": "2023-01-01T00:00:00Z",
  "endTime": "2023-01-01T00:00:10Z",
  "resultRows": 1000,
  "resultSize": 50000
}
```

#### 6.1.5 获取查询结果
- 请求：`GET /api/query/{queryId}/results`
- 参数：
  - `queryId`: 查询ID
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认100）
  - `sort`: 排序字段和方向（如`column1,asc`）
- 响应：
```json
{
  "queryId": "string",
  "columns": [
    {
      "name": "string",
      "label": "string",
      "type": "string"
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
    "size": 100,
    "sort": "column1,asc",
    "totalElements": 1000,
    "totalPages": 10
  }
}
```

#### 6.1.6 导出查询结果
- 请求：`GET /api/query/{queryId}/export`
- 参数：
  - `queryId`: 查询ID
  - `format`: 导出格式（csv, excel）
- 响应：
  - 文件下载（Content-Type根据格式变化）

### 6.2 查询管理接口

#### 6.2.1 保存查询
- 请求：`POST /api/saved-queries`
- 请求体：
```json
{
  "name": "string",
  "description": "string",
  "dataSourceId": "string",
  "schema": "string",
  "sql": "SELECT ... FROM ...",
  "queryConfig": {...},
  "isPublic": false,
  "tags": ["string"]
}
```
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "dataSourceId": "string",
  "schema": "string",
  "sql": "SELECT ... FROM ...",
  "queryConfig": {...},
  "isPublic": false,
  "tags": ["string"],
  "createdBy": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

#### 6.2.2 获取已保存查询
- 请求：`GET /api/saved-queries`
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
      "description": "string",
      "dataSourceId": "string",
      "dataSourceName": "string",
      "schema": "string",
      "isPublic": false,
      "tags": ["string"],
      "createdBy": "string",
      "createdAt": "2023-01-01T00:00:00Z",
      "updatedAt": "2023-01-01T00:00:00Z",
      "lastExecutedAt": "2023-01-01T00:00:00Z"
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

#### 6.2.3 获取查询详情
- 请求：`GET /api/saved-queries/{id}`
- 参数：
  - `id`: 查询ID
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "dataSourceId": "string",
  "dataSourceName": "string",
  "schema": "string",
  "sql": "SELECT ... FROM ...",
  "queryConfig": {...},
  "isPublic": false,
  "tags": ["string"],
  "createdBy": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z",
  "lastExecutedAt": "2023-01-01T00:00:00Z"
}
```

#### 6.2.4 更新已保存查询
- 请求：`PUT /api/saved-queries/{id}`
- 参数：
  - `id`: 查询ID
- 请求体：
```json
{
  "name": "string",
  "description": "string",
  "sql": "SELECT ... FROM ...",
  "queryConfig": {...},
  "isPublic": false,
  "tags": ["string"]
}
```
- 响应：
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "dataSourceId": "string",
  "schema": "string",
  "sql": "SELECT ... FROM ...",
  "queryConfig": {...},
  "isPublic": false,
  "tags": ["string"],
  "createdBy": "string",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

#### 6.2.5 删除已保存查询
- 请求：`DELETE /api/saved-queries/{id}`
- 参数：
  - `id`: 查询ID
- 响应：
```json
{
  "success": true,
  "message": "查询删除成功"
}
```

#### 6.2.6 共享查询
- 请求：`POST /api/saved-queries/{id}/share`
- 参数：
  - `id`: 查询ID
- 请求体：
```json
{
  "userIds": ["string"],
  "permission": "READ" // READ, EXECUTE, EDIT
}
```
- 响应：
```json
{
  "success": true,
  "message": "查询共享成功",
  "shareUrls": [
    {
      "userId": "string",
      "url": "string"
    }
  ]
}
```

#### 6.2.7 获取查询历史
- 请求：`GET /api/query-history`
- 参数：
  - `page`: 页码（默认0）
  - `size`: 每页大小（默认10）
  - `sort`: 排序字段和方向（如`executedAt,desc`）
  - `filter`: 筛选条件（JSON格式）
- 响应：
```json
{
  "content": [
    {
      "id": "string",
      "dataSourceId": "string",
      "dataSourceName": "string",
      "sql": "SELECT ... FROM ...",
      "status": "COMPLETED",
      "rowCount": 1000,
      "executionTime": 5000,
      "executedAt": "2023-01-01T00:00:00Z",
      "executedBy": "string",
      "savedQueryId": "string",
      "savedQueryName": "string"
    }
  ],
  "pageable": {
    "page": 0,
    "size": 10,
    "sort": "executedAt,desc",
    "totalElements": 100,
    "totalPages": 10
  }
}
```

## 7. 状态管理设计

### 7.1 查询构建器状态

```javascript
// 查询构建器状态
const queryBuilderState = {
  // 数据源和模式选择状态
  dataSource: {
    loading: false,
    dataSources: [],
    selectedDataSourceId: null,
    schemas: [],
    selectedSchema: null
  },
  
  // 表选择状态
  tableSelector: {
    loading: false,
    tables: [],
    filter: '',
    selectedTables: []
  },
  
  // 查询配置状态
  queryConfig: {
    tables: [],
    joins: [],
    conditions: {
      logic: 'AND',
      conditions: []
    },
    groupBy: [],
    orderBy: [],
    limit: 100
  },
  
  // SQL预览状态
  sqlPreview: {
    loading: false,
    sql: '',
    valid: true,
    errors: [],
    warnings: []
  },
  
  // 查询执行状态
  queryExecution: {
    loading: false,
    queryId: null,
    status: 'IDLE', // IDLE, RUNNING, COMPLETED, FAILED, CANCELLED
    message: '',
    progress: 0,
    startTime: null,
    endTime: null
  },
  
  // 查询结果状态
  queryResult: {
    loading: false,
    columns: [],
    data: [],
    pagination: {
      current: 1,
      pageSize: 100,
      total: 0
    },
    sorting: {
      field: null,
      order: null
    }
  },
  
  // 保存的查询状态
  savedQueries: {
    loading: false,
    queries: [],
    selectedQueryId: null
  },
  
  // 查询历史状态
  queryHistory: {
    loading: false,
    history: [],
    filter: {}
  }
};
```

### 7.2 状态管理方法

```javascript
// 查询构建器方法
const queryBuilderActions = {
  // 加载数据源
  async loadDataSources() {
    try {
      queryBuilderState.dataSource.loading = true;
      const response = await queryApi.getDataSources({ active: true });
      queryBuilderState.dataSource.dataSources = response;
    } catch (error) {
      console.error('Failed to load data sources', error);
      // 显示错误通知
    } finally {
      queryBuilderState.dataSource.loading = false;
    }
  },
  
  // 选择数据源
  async selectDataSource(dataSourceId) {
    queryBuilderState.dataSource.selectedDataSourceId = dataSourceId;
    queryBuilderState.dataSource.selectedSchema = null;
    queryBuilderState.tableSelector.tables = [];
    queryBuilderState.tableSelector.selectedTables = [];
    queryBuilderState.queryConfig = {
      tables: [],
      joins: [],
      conditions: {
        logic: 'AND',
        conditions: []
      },
      groupBy: [],
      orderBy: [],
      limit: 100
    };
    
    // 加载模式
    try {
      queryBuilderState.dataSource.loading = true;
      const response = await queryApi.getSchemas(dataSourceId);
      queryBuilderState.dataSource.schemas = response.schemas;
    } catch (error) {
      console.error('Failed to load schemas', error);
      // 显示错误通知
    } finally {
      queryBuilderState.dataSource.loading = false;
    }
  },
  
  // 选择模式
  async selectSchema(schema) {
    queryBuilderState.dataSource.selectedSchema = schema;
    
    // 加载表
    try {
      queryBuilderState.tableSelector.loading = true;
      const response = await queryApi.getTables(
        queryBuilderState.dataSource.selectedDataSourceId,
        schema
      );
      queryBuilderState.tableSelector.tables = response.tables;
    } catch (error) {
      console.error('Failed to load tables', error);
      // 显示错误通知
    } finally {
      queryBuilderState.tableSelector.loading = false;
    }
  },
  
  // 添加表到查询
  async addTableToQuery(table) {
    // 检查表是否已添加
    if (queryBuilderState.queryConfig.tables.some(t => t.name === table.name)) {
      // 显示警告通知
      return;
    }
    
    // 加载表详情
    try {
      queryBuilderState.tableSelector.loading = true;
      const response = await queryApi.getTableDetail(
        queryBuilderState.dataSource.selectedDataSourceId,
        queryBuilderState.dataSource.selectedSchema,
        table.name
      );
      
      // 创建表配置
      const tableId = generateUniqueId();
      const tableConfig = {
        id: tableId,
        name: table.name,
        alias: '',
        columns: response.columns.map(column => ({
          name: column.name,
          alias: '',
          selected: true,
          aggregateFunction: ''
        }))
      };
      
      // 添加到查询配置
      queryBuilderState.queryConfig.tables.push(tableConfig);
      queryBuilderState.tableSelector.selectedTables.push(table.name);
      
      // 如果已有表，尝试推断关系
      if (queryBuilderState.queryConfig.tables.length > 1) {
        this.inferTableRelationships(tableId);
      }
      
      // 更新SQL预览
      this.generateSqlPreview();
    } catch (error) {
      console.error('Failed to load table details', error);
      // 显示错误通知
    } finally {
      queryBuilderState.tableSelector.loading = false;
    }
  }
};
```

## 8. 组件实现示例

### 8.1 查询构建器主界面

```vue
<template>
  <div class="query-builder">
    <div class="page-header">
      <h1>查询构建器</h1>
      <div class="actions">
        <a-button-group>
          <a-button type="primary" @click="executeQuery" :loading="queryExecution.loading" :disabled="!sqlPreview.valid">
            <a-icon type="play-circle" /> 执行
          </a-button>
          <a-button @click="saveQuery">
            <a-icon type="save" /> 保存
          </a-button>
          <a-button @click="loadQuery">
            <a-icon type="folder-open" /> 加载
          </a-button>
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item key="export">
                <a-icon type="download" /> 导出结果
              </a-menu-item>
              <a-menu-item key="shareQuery">
                <a-icon type="share-alt" /> 共享查询
              </a-menu-item>
              <a-menu-item key="history">
                <a-icon type="history" /> 查询历史
              </a-menu-item>
            </a-menu>
            <a-button>
              <a-icon type="ellipsis" />
            </a-button>
          </a-dropdown>
        </a-button-group>
      </div>
    </div>
    
    <a-row :gutter="16">
      <a-col :span="6">
        <a-card title="数据源和表" :bordered="false">
          <a-form layout="vertical">
            <a-form-item label="数据源">
              <a-select
                v-model="dataSource.selectedDataSourceId"
                placeholder="选择数据源"
                :loading="dataSource.loading"
                @change="onDataSourceChange"
              >
                <a-select-option
                  v-for="ds in dataSource.dataSources"
                  :key="ds.id"
                  :value="ds.id"
                >
                  {{ ds.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
            
            <a-form-item label="模式">
              <a-select
                v-model="dataSource.selectedSchema"
                placeholder="选择模式"
                :loading="dataSource.loading"
                :disabled="!dataSource.selectedDataSourceId"
                @change="onSchemaChange"
              >
                <a-select-option
                  v-for="schema in dataSource.schemas"
                  :key="schema.name"
                  :value="schema.name"
                >
                  {{ schema.name }}
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-form>
          
          <table-selector
            v-if="dataSource.selectedSchema"
            :tables="tableSelector.tables"
            :selected-tables="tableSelector.selectedTables"
            :loading="tableSelector.loading"
            @add-table="addTableToQuery"
          />
        </a-card>
      </a-col>
      
      <a-col :span="18">
        <a-card :bordered="false">
          <a-tabs default-active-key="designer" type="card">
            <a-tab-pane key="designer" tab="查询设计器">
              <query-designer
                :query-config="queryConfig"
                @update:query-config="onQueryConfigChange"
              />
            </a-tab-pane>
            
            <a-tab-pane key="sql" tab="SQL">
              <sql-preview
                :sql="sqlPreview.sql"
                :loading="sqlPreview.loading"
                :valid="sqlPreview.valid"
                :errors="sqlPreview.errors"
                :warnings="sqlPreview.warnings"
                @execute="executeQuery"
              />
            </a-tab-pane>
            
            <a-tab-pane key="results" tab="结果" :disabled="!queryExecution.queryId">
              <result-panel
                v-if="queryExecution.queryId"
                :query-id="queryExecution.queryId"
                :status="queryExecution.status"
                :columns="queryResult.columns"
                :data="queryResult.data"
                :pagination="queryResult.pagination"
                :sorting="queryResult.sorting"
                :loading="queryResult.loading"
                @page-change="onPageChange"
                @sort-change="onSortChange"
                @export="exportResults"
              />
              <div v-else class="empty-result">
                <a-empty description="尚未执行查询" />
              </div>
            </a-tab-pane>
          </a-tabs>
        </a-card>
      </a-col>
    </a-row>
    
    <!-- 保存查询对话框 -->
    <save-query-form
      v-model="saveQueryVisible"
      @save="onSaveQuery"
    />
    
    <!-- 加载查询对话框 -->
    <load-query-dialog
      v-model="loadQueryVisible"
      @load="onLoadQuery"
    />
  </div>
</template>

<script>
import TableSelector from './TableSelector.vue';
import QueryDesigner from './QueryDesigner.vue';
import SqlPreview from './SqlPreview.vue';
import ResultPanel from './ResultPanel.vue';
import SaveQueryForm from './SaveQueryForm.vue';
import LoadQueryDialog from './LoadQueryDialog.vue';
import { generateUniqueId } from '../utils/id-generator';

export default {
  components: {
    TableSelector,
    QueryDesigner,
    SqlPreview,
    ResultPanel,
    SaveQueryForm,
    LoadQueryDialog
  },
  
  data() {
    return {
      // 数据源和模式选择状态
      dataSource: {
        loading: false,
        dataSources: [],
        selectedDataSourceId: null,
        schemas: [],
        selectedSchema: null
      },
      
      // 表选择状态
      tableSelector: {
        loading: false,
        tables: [],
        filter: '',
        selectedTables: []
      },
      
      // 查询配置状态
      queryConfig: {
        tables: [],
        joins: [],
        conditions: {
          logic: 'AND',
          conditions: []
        },
        groupBy: [],
        orderBy: [],
        limit: 100
      },
      
      // SQL预览状态
      sqlPreview: {
        loading: false,
        sql: '',
        valid: true,
        errors: [],
        warnings: []
      },
      
      // 查询执行状态
      queryExecution: {
        loading: false,
        queryId: null,
        status: 'IDLE', // IDLE, RUNNING, COMPLETED, FAILED, CANCELLED
        message: '',
        progress: 0,
        startTime: null,
        endTime: null
      },
      
      // 查询结果状态
      queryResult: {
        loading: false,
        columns: [],
        data: [],
        pagination: {
          current: 1,
          pageSize: 100,
          total: 0
        },
        sorting: {
          field: null,
          order: null
        }
      },
      
      // 保存查询对话框
      saveQueryVisible: false,
      
      // 加载查询对话框
      loadQueryVisible: false
    };
  },
  
  created() {
    this.loadDataSources();
  },
  
  methods: {
    // 加载数据源
    async loadDataSources() {
      try {
        this.dataSource.loading = true;
        const response = await this.$api.query.getDataSources({ active: true });
        this.dataSource.dataSources = response;
      } catch (error) {
        console.error('Failed to load data sources', error);
        this.$message.error('加载数据源失败，请重试');
      } finally {
        this.dataSource.loading = false;
      }
    },
    
    // 数据源变更处理
    async onDataSourceChange(dataSourceId) {
      this.dataSource.selectedDataSourceId = dataSourceId;
      this.dataSource.selectedSchema = null;
      this.tableSelector.tables = [];
      this.tableSelector.selectedTables = [];
      this.queryConfig = {
        tables: [],
        joins: [],
        conditions: {
          logic: 'AND',
          conditions: []
        },
        groupBy: [],
        orderBy: [],
        limit: 100
      };
      this.sqlPreview.sql = '';
      
      // 加载模式
      try {
        this.dataSource.loading = true;
        const response = await this.$api.query.getSchemas(dataSourceId);
        this.dataSource.schemas = response.schemas;
      } catch (error) {
        console.error('Failed to load schemas', error);
        this.$message.error('加载模式失败，请重试');
      } finally {
        this.dataSource.loading = false;
      }
    },
    
    // 模式变更处理
    async onSchemaChange(schema) {
      this.dataSource.selectedSchema = schema;
      
      // 加载表
      try {
        this.tableSelector.loading = true;
        const response = await this.$api.query.getTables(
          this.dataSource.selectedDataSourceId,
          schema
        );
        this.tableSelector.tables = response.tables;
      } catch (error) {
        console.error('Failed to load tables', error);
        this.$message.error('加载表失败，请重试');
      } finally {
        this.tableSelector.loading = false;
      }
    },
    
    // 添加表到查询
    async addTableToQuery(table) {
      // 检查表是否已添加
      if (this.queryConfig.tables.some(t => t.name === table.name)) {
        this.$message.warning(`表 ${table.name} 已添加到查询中`);
        return;
      }
      
      // 加载表详情
      try {
        this.tableSelector.loading = true;
        const response = await this.$api.query.getTableDetail(
          this.dataSource.selectedDataSourceId,
          this.dataSource.selectedSchema,
          table.name
        );
        
        // 创建表配置
        const tableId = generateUniqueId();
        const tableConfig = {
          id: tableId,
          name: table.name,
          alias: '',
          columns: response.columns.map(column => ({
            name: column.name,
            alias: '',
            selected: true,
            aggregateFunction: ''
          }))
        };
        
        // 添加到查询配置
        this.queryConfig.tables.push(tableConfig);
        this.tableSelector.selectedTables.push(table.name);
        
        // 如果已有表，尝试推断关系
        if (this.queryConfig.tables.length > 1) {
          this.inferTableRelationships(tableId);
        }
        
        // 更新SQL预览
        this.generateSqlPreview();
      } catch (error) {
        console.error('Failed to load table details', error);
        this.$message.error(`加载表 ${table.name} 详情失败，请重试`);
      } finally {
        this.tableSelector.loading = false;
      }
    },
    
    // 推断表关系
    async inferTableRelationships(newTableId) {
      const newTable = this.queryConfig.tables.find(t => t.id === newTableId);
      if (!newTable) return;
      
      for (const existingTable of this.queryConfig.tables) {
        if (existingTable.id === newTableId) continue;
        
        // 尝试从后端获取推荐的关系
        try {
          const response = await this.$api.query.inferTableRelationship(
            this.dataSource.selectedDataSourceId,
            this.dataSource.selectedSchema,
            existingTable.name,
            newTable.name
          );
          
          if (response.relationships && response.relationships.length > 0) {
            // 使用置信度最高的关系
            const bestRelationship = response.relationships[0];
            
            // 创建连接配置
            const joinConfig = {
              id: generateUniqueId(),
              leftTableId: existingTable.id,
              leftColumn: bestRelationship.leftColumn,
              rightTableId: newTableId,
              rightColumn: bestRelationship.rightColumn,
              type: 'INNER'
            };
            
            // 添加到查询配置
            this.queryConfig.joins.push(joinConfig);
            
            // 更新SQL预览
            this.generateSqlPreview();
            
            // 显示关系推荐通知
            this.$notification.info({
              message: '表关系推荐',
              description: `已自动添加表 ${existingTable.name} 和 ${newTable.name} 之间的关系`
            });
          }
        } catch (error) {
          console.error('Failed to infer table relationships', error);
          // 不显示错误，因为这只是一个辅助功能
        }
      }
    },
    
    // 查询配置变更处理
    onQueryConfigChange(newConfig) {
      this.queryConfig = newConfig;
      this.generateSqlPreview();
    },
    
    // 生成SQL预览
    async generateSqlPreview() {
      if (this.queryConfig.tables.length === 0) {
        this.sqlPreview.sql = '';
        this.sqlPreview.valid = false;
        return;
      }
      
      try {
        this.sqlPreview.loading = true;
        
        const request = {
          queryConfig: {
            dataSourceId: this.dataSource.selectedDataSourceId,
            schema: this.dataSource.selectedSchema,
            ...this.queryConfig
          }
        };
        
        const response = await this.$api.query.generateSql(request);
        
        this.sqlPreview.sql = response.sql;
        this.sqlPreview.valid = response.valid;
        this.sqlPreview.errors = response.errors;
        this.sqlPreview.warnings = response.warnings;
      } catch (error) {
        console.error('Failed to generate SQL preview', error);
        this.sqlPreview.valid = false;
        this.sqlPreview.errors = [error.message || '生成SQL失败'];
      } finally {
        this.sqlPreview.loading = false;
      }
    },
    
    // 执行查询
    async executeQuery() {
      if (!this.sqlPreview.valid || !this.sqlPreview.sql) {
        this.$message.error('查询无效，请修复错误后再执行');
        return;
      }
      
      try {
        this.queryExecution.loading = true;
        this.queryExecution.status = 'RUNNING';
        this.queryExecution.message = '正在执行查询...';
        this.queryExecution.progress = 0;
        this.queryExecution.startTime = new Date();
        this.queryExecution.endTime = null;
        
        const request = {
          dataSourceId: this.dataSource.selectedDataSourceId,
          sql: this.sqlPreview.sql,
          parameters: [],
          maxRows: 1000,
          timeout: 30000
        };
        
        const response = await this.$api.query.executeQuery(request);
        
        this.queryExecution.queryId = response.queryId;
        
        // 开始轮询查询状态
        this.pollQueryStatus();
      } catch (error) {
        console.error('Failed to execute query', error);
        this.queryExecution.status = 'FAILED';
        this.queryExecution.message = error.message || '查询执行失败';
        this.queryExecution.endTime = new Date();
        this.queryExecution.loading = false;
        
        this.$message.error('查询执行失败：' + (error.message || '未知错误'));
      }
    },
    
    // 轮询查询状态
    async pollQueryStatus() {
      if (!this.queryExecution.queryId) return;
      
      try {
        const response = await this.$api.query.getQueryStatus(this.queryExecution.queryId);
        
        this.queryExecution.status = response.status;
        this.queryExecution.message = response.message;
        this.queryExecution.progress = response.progress;
        
        if (response.endTime) {
          this.queryExecution.endTime = new Date(response.endTime);
        }
        
        if (response.status === 'COMPLETED') {
          // 查询完成，加载结果
          this.loadQueryResults();
        } else if (response.status === 'FAILED' || response.status === 'CANCELLED') {
          // 查询失败或取消，停止轮询
          this.queryExecution.loading = false;
          
          if (response.status === 'FAILED') {
            this.$message.error('查询执行失败：' + this.queryExecution.message);
          } else {
            this.$message.warning('查询已取消');
          }
        } else {
          // 继续轮询
          setTimeout(() => this.pollQueryStatus(), 1000);
        }
      } catch (error) {
        console.error('Failed to poll query status', error);
        this.queryExecution.status = 'FAILED';
        this.queryExecution.message = error.message || '查询状态获取失败';
        this.queryExecution.endTime = new Date();
        this.queryExecution.loading = false;
        
        this.$message.error('查询状态获取失败：' + (error.message || '未知错误'));
      }
    },
    
    // 加载查询结果
    async loadQueryResults(page = 1, pageSize = 100, sort = null) {
      if (!this.queryExecution.queryId) return;
      
      try {
        this.queryResult.loading = true;
        
        const params = {
          page: page - 1,
          size: pageSize
        };
        
        if (sort) {
          params.sort = `${sort.field},${sort.order === 'ascend' ? 'asc' : 'desc'}`;
        }
        
        const response = await this.$api.query.getQueryResults(this.queryExecution.queryId, params);
        
        this.queryResult.columns = response.columns;
        this.queryResult.data = response.data;
        this.queryResult.pagination = {
          current: page,
          pageSize: pageSize,
          total: response.pageable.totalElements
        };
        this.queryResult.sorting = sort;
        
        // 自动切换到结果标签页
        this.$nextTick(() => {
          const tabs = this.$el.querySelector('.ant-tabs-bar');
          if (tabs) {
            const resultTab = Array.from(tabs.querySelectorAll('.ant-tabs-tab')).find(
              tab => tab.textContent.includes('结果')
            );
            if (resultTab) {
              resultTab.click();
            }
          }
        });
      } catch (error) {
        console.error('Failed to load query results', error);
        this.$message.error('加载查询结果失败：' + (error.message || '未知错误'));
      } finally {
        this.queryResult.loading = false;
        this.queryExecution.loading = false;
      }
    },
    
    // 分页变化处理
    onPageChange(page, pageSize) {
      this.loadQueryResults(page, pageSize, this.queryResult.sorting);
    },
    
    // 排序变化处理
    onSortChange(field, order) {
      this.loadQueryResults(
        this.queryResult.pagination.current,
        this.queryResult.pagination.pageSize,
        { field, order }
      );
    },
    
    // 导出结果
    exportResults(format) {
      if (!this.queryExecution.queryId) return;
      
      const url = this.$api.query.getExportUrl(this.queryExecution.queryId, format);
      window.open(url, '_blank');
    },
    
    // 保存查询
    saveQuery() {
      if (!this.sqlPreview.valid || !this.sqlPreview.sql) {
        this.$message.error('查询无效，请修复错误后再保存');
        return;
      }
      
      this.saveQueryVisible = true;
    },
    
    // 保存查询处理
    async onSaveQuery({ name, description, isPublic, tags }) {
      try {
        const request = {
          name,
          description,
          dataSourceId: this.dataSource.selectedDataSourceId,
          schema: this.dataSource.selectedSchema,
          sql: this.sqlPreview.sql,
          queryConfig: this.queryConfig,
          isPublic,
          tags
        };
        
        await this.$api.query.saveQuery(request);
        
        this.$message.success('查询保存成功');
        this.saveQueryVisible = false;
      } catch (error) {
        console.error('Failed to save query', error);
        this.$message.error('保存查询失败：' + (error.message || '未知错误'));
      }
    },
    
    // 加载查询
    loadQuery() {
      this.loadQueryVisible = true;
    },
    
    // 加载查询处理
    async onLoadQuery(queryId) {
      try {
        const response = await this.$api.query.getSavedQueryDetail(queryId);
        
        // 确保数据源和模式已选择
        await this.onDataSourceChange(response.dataSourceId);
        await this.onSchemaChange(response.schema);
        
        // 加载查询配置
        this.queryConfig = response.queryConfig;
        this.sqlPreview.sql = response.sql;
        this.sqlPreview.valid = true;
        
        // 更新已选表
        this.tableSelector.selectedTables = this.queryConfig.tables.map(t => t.name);
        
        this.$message.success('查询加载成功');
        this.loadQueryVisible = false;
      } catch (error) {
        console.error('Failed to load saved query', error);
        this.$message.error('加载查询失败：' + (error.message || '未知错误'));
      }
    }
  }
};
</script>

<style scoped>
.query-builder {
  padding: 24px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.actions {
  display: flex;
  gap: 8px;
}

.empty-result {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>
```

## 9. 测试策略

### 9.1 单元测试

1. **组件渲染测试**：验证组件是否正确渲染，包括初始状态、加载状态和错误状态。
2. **状态管理测试**：验证状态更新和传播是否正确。
3. **数据流测试**：验证数据流是否正确流转。
4. **条件构建测试**：验证条件表达式构建是否正确。
5. **SQL生成测试**：验证SQL生成是否正确。

### 9.2 集成测试

1. **组件集成测试**：验证组件之间的交互是否正常。
2. **API集成测试**：验证与后端API的交互是否正常。
3. **数据源连接测试**：验证数据源连接是否正常。
4. **查询执行测试**：验证查询执行和结果处理是否正常。

### 9.3 端到端测试

1. **用户流程测试**：模拟完整用户流程，如选择数据源、选择表、配置查询、执行查询、保存查询等。
2. **边缘情况测试**：测试各种边缘情况，如没有表、没有数据、大数据量、复杂查询等。
3. **错误处理测试**：模拟各种错误情况，验证系统是否正确处理和提示。
4. **性能测试**：测试在大数据量和复杂查询下的性能。

## 10. 实施时间表

| 阶段 | 任务 | 开始日期 | 结束日期 | 负责人 |
|------|------|----------|----------|-------|
| 阶段一 | 查询构建器基础界面 | 2025-04-04 | 2025-04-08 | 前端开发 |
| 阶段二 | 查询设计器实现 | 2025-04-09 | 2025-04-12 | 前端开发 |
| 阶段三 | 查询配置和执行 | 2025-04-13 | 2025-04-16 | 前端开发 |
| 阶段四 | 查询管理功能 | 2025-04-17 | 2025-04-19 | 前端开发 |
| 阶段五 | 联调和修复 | 2025-04-20 | 2025-04-23 | 前端开发、后端开发 |

## 11. 总结

查询构建器是DataScope系统的核心功能之一，它允许用户通过可视化界面构建SQL查询，无需直接编写SQL代码。本文档详细规划了查询构建器模块的前端实现和前后端联调方案，包括功能列表、组件设计、数据流程、实现步骤、前后端接口定义、状态管理设计、组件实现示例、测试策略和实施时间表。

通过按照本文档的规划实施，我们可以高效地完成查询构建器模块的开发，并为用户提供便捷的数据查询工具。