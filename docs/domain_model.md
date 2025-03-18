# DataScope 领域模型

## 1. 实体 (Entities)

### 1.1 DataSource (数据源)

*   **描述**: 代表一个外部数据源，例如 MySQL 或 DB2 数据库。
*   **属性**:
    *   `id` (UUID, 主键): 数据源 ID
    *   `name` (varchar(255), 唯一): 数据源名称
    *   `type` (enum: DataSourceType): 数据源类型 (MySQL, DB2 等)
    *   `connectionDetails` (DataSourceConnectionDetails): 数据源连接详细信息 (值对象)
    *   `description` (text): 数据源描述
    *   `status` (enum): 连接状态 (Connected, Disconnected, Connecting)
    *   `syncStatus` (enum: SyncStatus): 元数据同步状态 (Pending, Syncing, Synced, Failed)
    *   `lastSyncTime` (datetime): 上次元数据同步时间
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   一对多关系到 `Database` (一个数据源可以包含多个数据库)
    *   聚合根

### 1.2 Database (数据库)

*   **描述**: 代表数据源中的一个数据库实例。
*   **属性**:
    *   `id` (UUID, 主键): 数据库 ID
    *   `dataSourceId` (UUID, 外键): 所属数据源 ID
    *   `name` (varchar(255)): 数据库名称
    *   `version` (varchar(100)): 数据库版本
    *   `characterSet` (varchar(100)): 字符集
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   一对多关系到 `Table` (一个数据库可以包含多个表格)
    *   属于 `DataSource` 聚合

### 1.3 Table (表格)

*   **描述**: 代表数据库中的一个表格。
*   **属性**:
    *   `id` (UUID, 主键): 表格 ID
    *   `databaseId` (UUID, 外键): 所属数据库 ID
    *   `name` (varchar(255)): 表格名称
    *   `schema` (TableSchema): 表格结构信息 (值对象)
    *   `tableType` (varchar(100)): 表格类型 (例如: TABLE, VIEW)
    *   `recordCount` (long): 记录数
    *   `description` (text): 表格描述
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   一对多关系到 `Column` (一个表格可以包含多个列)
    *   属于 `DataSource` 聚合

### 1.4 Column (列)

*   **描述**: 代表表格中的一个列。
*   **属性**:
    *   `id` (UUID, 主键): 列 ID
    *   `tableId` (UUID, 外键): 所属表格 ID
    *   `name` (varchar(255)): 列名称
    *   `dataType` (varchar(100)): 数据类型 (例如: VARCHAR, INT, DATETIME)
    *   `length` (int): 长度
    *   `precision` (int): 精度
    *   `nullable` (boolean): 是否允许为空
    *   `primaryKey` (boolean): 是否为主键
    *   `foreignKey` (boolean): 是否为外键
    *   `description` (text): 列描述
    *   `columnMetadata` (ColumnMetadata): 列元数据 (值对象)
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   属于 `DataSource` 聚合

### 1.5 Query (查询)

*   **描述**: 代表用户创建的 SQL 查询。
*   **属性**:
    *   `id` (UUID, 主键): 查询 ID
    *   `name` (varchar(255)): 查询名称
    *   `sqlText` (text): SQL 查询语句
    *   `description` (text): 查询描述
    *   `dataSourceId` (UUID, 外键): 关联数据源 ID
    *   `version` (int): 版本号
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   聚合根
    *   关联到 `DataSource`

### 1.6 SavedQuery (保存的查询)

*   **描述**: 代表用户保存的常用查询。
*   **属性**:
    *   `id` (UUID, 主键): 保存查询 ID
    *   `queryId` (UUID, 外键): 关联查询 ID
    *   `name` (varchar(255)): 保存查询名称
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   关联到 `Query`

### 1.7 QueryHistory (查询历史记录)

*   **描述**: 代表用户的查询操作历史记录。
*   **属性**:
    *   `id` (UUID, 主键): 历史记录 ID
    *   `queryId` (UUID, 外键): 关联查询 ID
    *   `dataSourceId` (UUID, 外键): 关联数据源 ID
    *   `sqlText` (text): 执行的 SQL 查询语句
    *   `executionTime` (datetime): 执行时间
    *   `status` (enum): 执行状态 (Success, Failed, Timeout)
    *   `error` (SQLError): 错误信息 (值对象)
    *   `userId` (UUID, 外键): 执行用户 ID
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   关联到 `Query`
    *   关联到 `DataSource`
    *   关联到 `User`

### 1.8 User (用户)

*   **描述**: 代表系统用户。
*   **属性**:
    *   `id` (UUID, 主键): 用户 ID
    *   `username` (varchar(255), 唯一): 用户名
    *   `password` (varchar(255)): 密码 (AES-256 加密 + Salt)
    *   `email` (varchar(255)): 邮箱
    *   `roleId` (UUID, 外键): 关联角色 ID
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   聚合根
    *   多对多关系到 `Role` (通过用户角色关联表)

### 1.9 Role (角色)

*   **描述**: 代表用户的角色，用于权限管理。
*   **属性**:
    *   `id` (UUID, 主键): 角色 ID
    *   `name` (varchar(255), 唯一): 角色名称
    *   `description` (text): 角色描述
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   多对多关系到 `User` (通过用户角色关联表)
    *   多对多关系到 `Permission` (通过角色权限关联表)

### 1.10 Permission (权限)

*   **描述**: 代表系统权限。
*   **属性**:
    *   `id` (UUID, 主键): 权限 ID
    *   `name` (varchar(255), 唯一): 权限名称
    *   `description` (text): 权限描述
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   多对多关系到 `Role` (通过角色权限关联表)

### 1.11 LowCodeApp (低代码应用)

*   **描述**: 代表与 DataScope 集成的低代码应用。
*   **属性**:
    *   `id` (UUID, 主键): 应用 ID
    *   `name` (varchar(255)): 应用名称
    *   `config` (text): 应用配置信息 (JSON 格式)
    *   `syncStatus` (enum: SyncStatus): 同步状态 (Pending, Syncing, Synced, Failed)
    *   `lastSyncTime` (datetime): 上次同步时间
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   聚合根

### 1.12 MetadataSyncJob (元数据同步任务)

*   **描述**: 代表元数据同步任务，负责定期同步数据源元数据。
*   **属性**:
    *   `id` (UUID, 主键): 任务 ID
    *   `dataSourceId` (UUID, 外键): 关联数据源 ID
    *   `syncType` (enum: SyncType): 同步类型 (Full, Incremental)
    *   `syncFrequency` (varchar(100)): 同步频率 (例如: Daily, Weekly)
    *   `lastSyncTime` (datetime): 上次同步时间
    *   `status` (enum: SyncStatus): 任务状态 (Pending, Running, Success, Failed)
    *   `error` (SQLError): 错误信息 (值对象)
    *   `createdAt` (datetime): 创建时间
    *   `createdBy` (varchar(255)): 创建用户
    *   `modifiedAt` (datetime): 修改时间
    *   `modifiedBy` (varchar(255)): 修改用户
    *   `nonce` (int): 乐观锁版本号
*   **关系**:
    *   关联到 `DataSource`

## 2. 值对象 (Value Objects)

### 2.1 DataSourceConnectionDetails (数据源连接详细信息)

*   **描述**: 包含连接数据源所需的详细信息。
*   **属性**:
    *   `host` (varchar(255)): 主机地址
    *   `port` (int): 端口号
    *   `databaseName` (varchar(255)): 数据库名称
    *   `username` (varchar(255)): 用户名
    *   `password` (varchar(255)): 密码 (AES-256 加密 + Salt)
    *   `connectionUrl` (varchar(1024)): 连接 URL (根据数据源类型动态生成)

### 2.2 QueryExecutionPlan (查询执行计划)

*   **描述**: 描述 SQL 查询的执行计划，用于性能分析和优化。
*   **属性**:
    *   `planDetails` (text): 执行计划详细信息 (例如 JSON 或文本格式)
    *   `estimatedCost` (decimal): 估计成本
    *   `executionTime` (long): 执行时间 (毫秒)

### 2.3 SQLError (SQL 错误信息)

*   **描述**: 封装 SQL 执行错误信息。
*   **属性**:
    *   `errorCode` (varchar(100)): 错误代码
    *   `errorMessage` (text): 错误消息
    *   `sqlState` (varchar(100)): SQLSTATE 代码
    *   `errorDetails` (text): 错误详细信息 (例如堆栈跟踪)

### 2.4 TableSchema (表格结构信息)

*   **描述**: 描述表格的结构信息，包含列、索引、主键等。
*   **属性**:
    *   `columns` (List<ColumnMetadata>): 列元数据列表
    *   `primaryKeyColumns` (List<String>): 主键列名列表
    *   `indexList` (List<IndexMetadata>): 索引元数据列表 (未来扩展)

### 2.5 ColumnMetadata (列元数据)

*   **描述**: 描述列的元数据信息。
*   **属性**:
    *   `columnName` (varchar(255)): 列名
    *   `dataType` (varchar(100)): 数据类型
    *   `length` (int): 长度
    *   `precision` (int): 精度
    *   `nullable` (boolean): 是否允许为空
    *   `defaultValue` (varchar(255)): 默认值

## 3. 聚合根 (Aggregate Roots)

*   DataSource
*   Query
*   User
*   LowCodeApp

## 4. 领域服务 (Domain Services)

*   MetadataSynchronizationService
*   QueryExecutionService
*   AIService
*   PermissionService

## 5. 实体关系 (Relationships)

*   DataSource 1:N Database
*   Database 1:N Table
*   Table 1:N Column
*   User M:N Role (通过 tbl_user_role 关联表)
*   Role M:N Permission (通过 tbl_role_permission 关联表)
*   Query -> DataSource
*   QueryHistory -> Query, DataSource, User
*   SavedQuery -> Query

---
**版本记录**

[2025-03-18 12:30:00] - 初始版本，创建 DataScope 系统领域模型文档。