# 低代码平台交互协议

本文档定义了数据管理与查询系统与低代码平台之间的JSON交互协议，用于实现查询和配置的无缝集成。

## 1. 协议概述

低代码平台交互协议是一套基于JSON的通信标准，用于在数据管理与查询系统与低代码平台之间传递查询配置、表单定义和数据展示规则。该协议支持版本控制，确保向后兼容性，同时提供足够的灵活性以适应不同的应用场景。

### 1.1 设计原则

1. **简单性**：协议设计简单明了，易于实现和理解
2. **灵活性**：支持多种查询类型和展示形式
3. **可扩展性**：允许添加新的配置选项而不破坏现有实现
4. **版本控制**：支持协议版本管理，确保向后兼容
5. **自描述**：协议消息包含足够的元数据以自我描述

### 1.2 交互流程

低代码平台与数据管理系统的典型交互流程如下：

1. 低代码平台请求查询配置
2. 数据管理系统返回查询配置（包括表单和展示规则）
3. 低代码平台根据配置生成用户界面
4. 用户填写查询条件并提交
5. 低代码平台将查询请求发送到数据管理系统
6. 数据管理系统执行查询并返回结果
7. 低代码平台根据展示规则渲染结果

## 2. 协议消息格式

### 2.1 基础消息结构

所有协议消息共享以下基础结构：

```json
{
  "version": "1.0",
  "messageType": "string",
  "timestamp": "ISO-8601 timestamp",
  "requestId": "string",
  "payload": {}
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| version | string | 协议版本号，格式为"主版本.次版本" |
| messageType | string | 消息类型，如"QueryConfigRequest"、"QueryConfigResponse" |
| timestamp | string | 消息创建时间，ISO-8601格式 |
| requestId | string | 请求标识符，用于关联请求和响应 |
| payload | object | 消息主体内容，根据messageType不同而变化 |

### 2.2 错误响应格式

当处理请求出错时，返回以下错误响应格式：

```json
{
  "version": "1.0",
  "messageType": "ErrorResponse",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "error": {
    "code": "string",
    "message": "string",
    "details": {}
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| error.code | string | 错误代码，如"INVALID_REQUEST"、"QUERY_EXECUTION_ERROR" |
| error.message | string | 人类可读的错误描述 |
| error.details | object | 错误的详细信息，可选 |

## 3. 查询配置请求与响应

### 3.1 查询配置请求

低代码平台请求查询配置的消息格式：

```json
{
  "version": "1.0",
  "messageType": "QueryConfigRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "configType": "string",
    "locale": "string",
    "userPreferences": {
      "theme": "string",
      "displayDensity": "string"
    }
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符 |
| configType | string | 配置类型，可选值："full"、"form"、"display" |
| locale | string | 区域设置，如"zh-CN"、"en-US" |
| userPreferences | object | 用户偏好设置，可选 |

### 3.2 查询配置响应

数据管理系统返回查询配置的消息格式：

```json
{
  "version": "1.0",
  "messageType": "QueryConfigResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "queryName": "string",
    "description": "string",
    "version": "string",
    "form": {},
    "display": {},
    "metadata": {}
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符 |
| queryName | string | 查询名称 |
| description | string | 查询描述 |
| version | string | 查询配置版本 |
| form | object | 表单配置，详见3.3节 |
| display | object | 展示配置，详见3.4节 |
| metadata | object | 元数据，包含查询相关的附加信息 |

### 3.3 表单配置结构

表单配置定义了查询条件的输入界面：

```json
{
  "layout": "string",
  "sections": [
    {
      "id": "string",
      "title": "string",
      "description": "string",
      "expanded": boolean,
      "fields": []
    }
  ],
  "fields": [
    {
      "id": "string",
      "name": "string",
      "label": "string",
      "type": "string",
      "defaultValue": "any",
      "placeholder": "string",
      "helpText": "string",
      "required": boolean,
      "visible": boolean,
      "readOnly": boolean,
      "validation": {},
      "options": [],
      "dependencies": [],
      "advanced": boolean,
      "properties": {}
    }
  ],
  "buttons": [
    {
      "id": "string",
      "label": "string",
      "type": "string",
      "action": "string",
      "primary": boolean,
      "icon": "string"
    }
  ]
}
```

#### 3.3.1 表单字段类型

表单字段支持以下类型：

| 字段类型 | 描述 | 特有属性 |
|---------|------|---------|
| text | 文本输入框 | maxLength, minLength, pattern |
| number | 数字输入框 | min, max, step |
| date | 日期选择器 | min, max, format |
| datetime | 日期时间选择器 | min, max, format |
| time | 时间选择器 | min, max, format |
| select | 下拉选择框 | options, multiple, searchable |
| multiselect | 多选框 | options, maxSelections |
| checkbox | 复选框 | checkedValue, uncheckedValue |
| radio | 单选按钮组 | options, layout |
| switch | 开关 | checkedValue, uncheckedValue |
| slider | 滑块 | min, max, step, range |
| textarea | 多行文本框 | rows, maxLength |
| autocomplete | 自动完成输入框 | dataSource, minChars |
| cascader | 级联选择器 | options, changeOnSelect |
| daterange | 日期范围选择器 | min, max, format |
| timerange | 时间范围选择器 | min, max, format |
| upload | 文件上传 | accept, maxSize, multiple |
| custom | 自定义组件 | componentName, properties |

#### 3.3.2 字段验证规则

字段验证规则定义如下：

```json
{
  "required": boolean,
  "min": number,
  "max": number,
  "minLength": number,
  "maxLength": number,
  "pattern": "string",
  "patternMessage": "string",
  "custom": "string",
  "customMessage": "string"
}
```

#### 3.3.3 字段依赖关系

字段依赖关系定义如下：

```json
{
  "field": "string",
  "value": "any",
  "operator": "string",
  "action": "string"
}
```

| 操作符 | 描述 |
|-------|------|
| eq | 等于 |
| neq | 不等于 |
| gt | 大于 |
| gte | 大于等于 |
| lt | 小于 |
| lte | 小于等于 |
| contains | 包含 |
| startsWith | 以...开始 |
| endsWith | 以...结束 |
| empty | 为空 |
| notEmpty | 不为空 |

| 动作 | 描述 |
|------|------|
| show | 显示字段 |
| hide | 隐藏字段 |
| enable | 启用字段 |
| disable | 禁用字段 |
| require | 设为必填 |
| optional | 设为可选 |
| setValue | 设置值 |

### 3.4 展示配置结构

展示配置定义了查询结果的展示方式：

```json
{
  "type": "string",
  "title": "string",
  "description": "string",
  "pagination": {
    "enabled": boolean,
    "pageSize": number,
    "pageSizeOptions": [number]
  },
  "sorting": {
    "enabled": boolean,
    "defaultField": "string",
    "defaultOrder": "string"
  },
  "filtering": {
    "enabled": boolean,
    "quickFilter": boolean
  },
  "selection": {
    "enabled": boolean,
    "type": "string"
  },
  "export": {
    "enabled": boolean,
    "formats": ["string"]
  },
  "columns": [
    {
      "field": "string",
      "header": "string",
      "description": "string",
      "width": number,
      "minWidth": number,
      "maxWidth": number,
      "align": "string",
      "sortable": boolean,
      "filterable": boolean,
      "hidden": boolean,
      "fixed": "string",
      "format": "string",
      "formatOptions": {},
      "render": "string",
      "cellClass": "string",
      "headerClass": "string",
      "mask": {
        "enabled": boolean,
        "type": "string",
        "pattern": "string"
      },
      "properties": {}
    }
  ],
  "operations": [
    {
      "type": "string",
      "label": "string",
      "icon": "string",
      "action": "string",
      "confirm": {
        "title": "string",
        "message": "string",
        "okText": "string",
        "cancelText": "string"
      },
      "visible": "string",
      "enabled": "string",
      "properties": {}
    }
  ],
  "emptyState": {
    "message": "string",
    "icon": "string",
    "action": {
      "label": "string",
      "action": "string"
    }
  },
  "properties": {}
}
```

#### 3.4.1 展示类型

支持的展示类型包括：

| 类型 | 描述 |
|------|------|
| table | 表格视图 |
| card | 卡片视图 |
| list | 列表视图 |
| tree | 树形视图 |
| chart | 图表视图 |
| custom | 自定义视图 |

#### 3.4.2 列格式化

列格式化支持以下类型：

| 格式类型 | 描述 | 示例 |
|---------|------|------|
| number | 数字格式化 | "1,234.56" |
| currency | 货币格式化 | "¥1,234.56" |
| percent | 百分比格式化 | "12.34%" |
| date | 日期格式化 | "2025-03-16" |
| datetime | 日期时间格式化 | "2025-03-16 04:30:00" |
| time | 时间格式化 | "04:30:00" |
| boolean | 布尔值格式化 | "是/否" |
| enum | 枚举值格式化 | 根据映射表显示 |
| custom | 自定义格式化 | 自定义函数 |

#### 3.4.3 数据掩码

数据掩码支持以下类型：

| 掩码类型 | 描述 | 示例 |
|---------|------|------|
| phone | 电话号码掩码 | "138****1234" |
| idcard | 身份证号掩码 | "110***********1234" |
| email | 电子邮件掩码 | "u***@example.com" |
| name | 姓名掩码 | "张*" |
| bankcard | 银行卡号掩码 | "****1234" |
| custom | 自定义掩码 | 根据pattern定义 |

## 4. 查询执行请求与响应

### 4.1 查询执行请求

低代码平台发送查询执行请求的消息格式：

```json
{
  "version": "1.0",
  "messageType": "QueryExecuteRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "version": "string",
    "parameters": {},
    "pagination": {
      "page": number,
      "pageSize": number
    },
    "sorting": [
      {
        "field": "string",
        "order": "string"
      }
    ],
    "filtering": [
      {
        "field": "string",
        "operator": "string",
        "value": "any"
      }
    ]
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符 |
| version | string | 查询版本，可选 |
| parameters | object | 查询参数，键值对 |
| pagination | object | 分页信息，可选 |
| sorting | array | 排序信息，可选 |
| filtering | array | 过滤信息，可选 |

### 4.2 查询执行响应

数据管理系统返回查询结果的消息格式：

```json
{
  "version": "1.0",
  "messageType": "QueryExecuteResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "success": boolean,
    "data": [{}],
    "total": number,
    "page": number,
    "pageSize": number,
    "totalPages": number,
    "executionTime": number,
    "warnings": [
      {
        "code": "string",
        "message": "string"
      }
    ],
    "metadata": {}
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符 |
| success | boolean | 查询是否成功 |
| data | array | 查询结果数据 |
| total | number | 总记录数 |
| page | number | 当前页码 |
| pageSize | number | 每页记录数 |
| totalPages | number | 总页数 |
| executionTime | number | 查询执行时间（毫秒） |
| warnings | array | 警告信息，可选 |
| metadata | object | 元数据，可选 |

## 5. 低代码配置管理

### 5.1 配置保存请求

低代码平台发送配置保存请求的消息格式：

```json
{
  "version": "1.0",
  "messageType": "ConfigSaveRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "name": "string",
    "description": "string",
    "form": {},
    "display": {},
    "createNewVersion": boolean,
    "metadata": {}
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符，新建时可为空 |
| name | string | 配置名称 |
| description | string | 配置描述 |
| form | object | 表单配置 |
| display | object | 展示配置 |
| createNewVersion | boolean | 是否创建新版本 |
| metadata | object | 元数据，可选 |

### 5.2 配置保存响应

数据管理系统返回配置保存结果的消息格式：

```json
{
  "version": "1.0",
  "messageType": "ConfigSaveResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "string",
    "version": "string",
    "success": boolean,
    "message": "string"
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| queryId | string | 查询标识符 |
| version | string | 配置版本 |
| success | boolean | 保存是否成功 |
| message | string | 结果消息 |

## 6. 元数据查询

### 6.1 元数据查询请求

低代码平台请求元数据的消息格式：

```json
{
  "version": "1.0",
  "messageType": "MetadataRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "dataSourceId": "string",
    "metadataType": "string",
    "filter": {
      "schemaName": "string",
      "tableName": "string",
      "search": "string"
    }
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| dataSourceId | string | 数据源标识符 |
| metadataType | string | 元数据类型，如"schemas"、"tables"、"columns" |
| filter | object | 过滤条件，可选 |

### 6.2 元数据查询响应

数据管理系统返回元数据的消息格式：

```json
{
  "version": "1.0",
  "messageType": "MetadataResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "dataSourceId": "string",
    "metadataType": "string",
    "items": [{}],
    "total": number
  }
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| dataSourceId | string | 数据源标识符 |
| metadataType | string | 元数据类型 |
| items | array | 元数据项列表 |
| total | number | 总项数 |

## 7. 事件通知

### 7.1 事件通知消息

数据管理系统发送事件通知的消息格式：

```json
{
  "version": "1.0",
  "messageType": "EventNotification",
  "timestamp": "2025-03-16T04:30:00Z",
  "eventId": "string",
  "eventType": "string",
  "payload": {}
}
```

| 字段 | 类型 | 描述 |
|------|------|------|
| eventId | string | 事件标识符 |
| eventType | string | 事件类型 |
| payload | object | 事件数据 |

### 7.2 支持的事件类型

| 事件类型 | 描述 |
|---------|------|
| QueryCompleted | 异步查询完成 |
| ConfigChanged | 配置变更 |
| DataSourceChanged | 数据源变更 |
| ExportCompleted | 数据导出完成 |
| SystemAlert | 系统告警 |

## 8. 协议版本控制

### 8.1 版本兼容性规则

1. **主版本**：不兼容的API变更，如删除字段、改变字段类型
2. **次版本**：向后兼容的功能性新增，如新增可选字段

### 8.2 版本协商

客户端可以在请求中指定支持的版本范围：

```json
{
  "version": "1.0",
  "supportedVersions": ["1.0", "1.1", "1.2"],
  "messageType": "QueryConfigRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {}
}
```

服务端将使用客户端支持的最高版本进行响应。

## 9. 安全考虑

### 9.1 认证与授权

所有API请求应包含认证信息，可通过以下方式之一提供：

1. **Bearer Token**：在HTTP头部添加`Authorization: Bearer <token>`
2. **API Key**：在HTTP头部添加`X-API-Key: <api-key>`
3. **OAuth 2.0**：使用标准OAuth 2.0流程

### 9.2 数据验证

1. 所有输入数据必须经过验证，确保符合协议规范
2. 敏感数据应在传输前进行掩码处理
3. 查询参数应进行类型检查和范围验证

### 9.3 请求限制

1. 实施请求频率限制，防止滥用
2. 限制单次查询返回的数据量
3. 对长时间运行的查询实施超时机制

## 10. 示例

### 10.1 查询配置请求示例

```json
{
  "version": "1.0",
  "messageType": "QueryConfigRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "order-query-001",
    "configType": "full",
    "locale": "zh-CN"
  }
}
```

### 10.2 查询配置响应示例

```json
{
  "version": "1.0",
  "messageType": "QueryConfigResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "order-query-001",
    "queryName": "订单查询",
    "description": "查询订单信息",
    "version": "1.2",
    "form": {
      "layout": "standard",
      "sections": [
        {
          "id": "basic",
          "title": "基本信息",
          "expanded": true,
          "fields": ["orderNo", "customerName", "orderDateRange"]
        },
        {
          "id": "advanced",
          "title": "高级选项",
          "expanded": false,
          "fields": ["status", "paymentMethod", "minAmount", "maxAmount"]
        }
      ],
      "fields": [
        {
          "id": "orderNo",
          "name": "orderNo",
          "label": "订单号",
          "type": "text",
          "placeholder": "请输入订单号",
          "helpText": "订单号格式为：ORD-YYYYMMDD-XXXXX",
          "validation": {
            "pattern": "ORD-\\d{8}-\\d{5}",
            "patternMessage": "订单号格式不正确"
          }
        },
        {
          "id": "customerName",
          "name": "customerName",
          "label": "客户名称",
          "type": "text",
          "placeholder": "请输入客户名称"
        },
        {
          "id": "orderDateRange",
          "name": "orderDateRange",
          "label": "订单日期",
          "type": "daterange",
          "defaultValue": ["2025-01-01", "2025-03-16"]
        },
        {
          "id": "status",
          "name": "status",
          "label": "订单状态",
          "type": "select",
          "options": [
            {"value": "all", "label": "全部"},
            {"value": "pending", "label": "待处理"},
            {"value": "processing", "label": "处理中"},
            {"value": "completed", "label": "已完成"},
            {"value": "cancelled", "label": "已取消"}
          ],
          "defaultValue": "all",
          "advanced": true
        },
        {
          "id": "paymentMethod",
          "name": "paymentMethod",
          "label": "支付方式",
          "type": "select",
          "options": [
            {"value": "all", "label": "全部"},
            {"value": "credit", "label": "信用卡"},
            {"value": "debit", "label": "借记卡"},
            {"value": "transfer", "label": "银行转账"},
            {"value": "alipay", "label": "支付宝"},
            {"value": "wechat", "label": "微信支付"}
          ],
          "defaultValue": "all",
          "advanced": true
        },
        {
          "id": "minAmount",
          "name": "minAmount",
          "label": "最小金额",
          "type": "number",
          "placeholder": "最小金额",
          "advanced": true
        },
        {
          "id": "maxAmount",
          "name": "maxAmount",
          "label": "最大金额",
          "type": "number",
          "placeholder": "最大金额",
          "advanced": true,
          "dependencies": [
            {
              "field": "minAmount",
              "operator": "notEmpty",
              "action": "require"
            }
          ]
        }
      ],
      "buttons": [
        {
          "id": "reset",
          "label": "重置",
          "type": "reset",
          "primary": false,
          "icon": "refresh"
        },
        {
          "id": "search",
          "label": "查询",
          "type": "submit",
          "primary": true,
          "icon": "search"
        }
      ]
    },
    "display": {
      "type": "table",
      "title": "订单列表",
      "pagination": {
        "enabled": true,
        "pageSize": 10,
        "pageSizeOptions": [10, 20, 50, 100]
      },
      "sorting": {
        "enabled": true,
        "defaultField": "orderDate",
        "defaultOrder": "desc"
      },
      "filtering": {
        "enabled": true,
        "quickFilter": true
      },
      "selection": {
        "enabled": true,
        "type": "checkbox"
      },
      "export": {
        "enabled": true,
        "formats": ["csv", "excel"]
      },
      "columns": [
        {
          "field": "orderNo",
          "header": "订单号",
          "width": 150,
          "sortable": true,
          "fixed": "left"
        },
        {
          "field": "customerName",
          "header": "客户名称",
          "width": 120,
          "sortable": true
        },
        {
          "field": "orderDate",
          "header": "订单日期",
          "width": 120,
          "sortable": true,
          "format": "date",
          "formatOptions": {
            "format": "yyyy-MM-dd"
          }
        },
        {
          "field": "amount",
          "header": "订单金额",
          "width": 120,
          "align": "right",
          "sortable": true,
          "format": "currency",
          "formatOptions": {
            "currency": "CNY",
            "minimumFractionDigits": 2,
            "maximumFractionDigits": 2
          }
        },
        {
          "field": "status",
          "header": "订单状态",
          "width": 100,
          "sortable": true,
          "format": "enum",
          "formatOptions": {
            "mapping": {
              "pending": "待处理",
              "processing": "处理中",
              "completed": "已完成",
              "cancelled": "已取消"
            },
            "colorMapping": {
              "pending": "#faad14",
              "processing": "#1890ff",
              "completed": "#52c41a",
              "cancelled": "#f5222d"
            }
          }
        },
        {
          "field": "paymentMethod",
          "header": "支付方式",
          "width": 120,
          "sortable": true,
          "format": "enum",
          "formatOptions": {
            "mapping": {
              "credit": "信用卡",
              "debit": "借记卡",
              "transfer": "银行转账",
              "alipay": "支付宝",
              "wechat": "微信支付"
            }
          }
        },
        {
          "field": "contactPhone",
          "header": "联系电话",
          "width": 120,
          "mask": {
            "enabled": true,
            "type": "phone"
          }
        }
      ],
      "operations": [
        {
          "type": "button",
          "label": "查看详情",
          "icon": "eye",
          "action": "view",
          "properties": {
            "route": "/orders/detail"
          }
        },
        {
          "type": "button",
          "label": "编辑",
          "icon": "edit",
          "action": "edit",
          "visible": "record.status !== 'completed' && record.status !== 'cancelled'",
          "properties": {
            "route": "/orders/edit"
          }
        },
        {
          "type": "button",
          "label": "取消订单",
          "icon": "close-circle",
          "action": "cancel",
          "visible": "record.status === 'pending' || record.status === 'processing'",
          "confirm": {
            "title": "确认取消",
            "message": "确定要取消该订单吗？此操作不可撤销。",
            "okText": "确定",
            "cancelText": "取消"
          }
        }
      ],
      "emptyState": {
        "message": "没有找到符合条件的订单",
        "icon": "inbox",
        "action": {
          "label": "清除筛选条件",
          "action": "clearFilters"
        }
      }
    }
  }
}
```

### 10.3 查询执行请求示例

```json
{
  "version": "1.0",
  "messageType": "QueryExecuteRequest",
  "timestamp": "2025-03-16T04:30:00Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "order-query-001",
    "version": "1.2",
    "parameters": {
      "orderDateRange": ["2025-01-01", "2025-03-16"],
      "status": "pending"
    },
    "pagination": {
      "page": 1,
      "pageSize": 10
    },
    "sorting": [
      {
        "field": "orderDate",
        "order": "desc"
      }
    ]
  }
}
```

### 10.4 查询执行响应示例

```json
{
  "version": "1.0",
  "messageType": "QueryExecuteResponse",
  "timestamp": "2025-03-16T04:30:05Z",
  "requestId": "req-12345",
  "payload": {
    "queryId": "order-query-001",
    "success": true,
    "data": [
      {
        "orderNo": "ORD-20250315-00001",
        "customerName": "张三",
        "orderDate": "2025-03-15",
        "amount": 1299.99,
        "status": "pending",
        "paymentMethod": "alipay",
        "contactPhone": "13812345678"
      },
      {
        "orderNo": "ORD-20250314-00015",
        "customerName": "李四",
        "orderDate": "2025-03-14",
        "amount": 2499.50,
        "status": "pending",
        "paymentMethod": "wechat",
        "contactPhone": "13987654321"
      }
      // 更多数据...
    ],
    "total": 28,
    "page": 1,
    "pageSize": 10,
    "totalPages": 3,
    "executionTime": 120
  }
}
```

## 11. 实现指南

### 11.1 低代码平台实现建议

1. **配置缓存**：缓存查询配置，减少请求次数
2. **动态表单生成**：根据表单配置动态生成表单组件
3. **动态表格生成**：根据展示配置动态生成表格组件
4. **错误处理**：实现统一的错误处理机制
5. **版本检查**：检查协议版本兼容性
6. **离线支持**：支持离线保存查询条件和结果

### 11.2 数据管理系统实现建议

1. **配置版本管理**：实现配置的版本控制
2. **参数验证**：严格验证查询参数
3. **性能优化**：优化查询执行性能
4. **安全控制**：实施访问控制和数据权限
5. **监控与日志**：记录查询执行情况
6. **扩展点**：提供自定义扩展机制

## 12. 附录

### 12.1 错误代码列表

| 错误代码 | 描述 |
|---------|------|
| INVALID_REQUEST | 请求格式无效 |
| INVALID_QUERY_ID | 查询ID无效 |
| INVALID_PARAMETERS | 查询参数无效 |
| QUERY_EXECUTION_ERROR | 查询执行错误 |
| PERMISSION_DENIED | 权限不足 |
| RESOURCE_NOT_FOUND | 资源不存在 |
| VERSION_MISMATCH | 版本不匹配 |
| RATE_LIMIT_EXCEEDED | 超出请求频率限制 |
| INTERNAL_ERROR | 内部服务器错误 |

### 12.2 字段类型映射

| 数据库类型 | 表单字段类型 | 展示格式类型 |
|-----------|------------|------------|
| VARCHAR, CHAR | text | text |
| TEXT, CLOB | textarea | text |
| INT, SMALLINT | number | number |
| BIGINT | number | number |
| DECIMAL, NUMERIC | number | currency/number |
| FLOAT, DOUBLE | number | number |
| DATE | date | date |
| TIME | time | time |
| TIMESTAMP, DATETIME | datetime | datetime |
| BOOLEAN, BIT | switch/checkbox | boolean |
| ENUM | select | enum |
| BLOB, BINARY | upload | download |