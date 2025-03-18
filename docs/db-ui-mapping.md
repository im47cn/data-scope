# 数据库表结构与 UI 映射

## 1. 数据库表结构 (Database Table Schemas)

### 1.1 tbl_data_source

*   **表名**: `tbl_data_source`
*   **描述**: 存储数据源信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '数据源ID'
    *   `name` varchar(255) UNIQUE NOT NULL COMMENT '数据源名称'
    *   `type` varchar(50) NOT NULL COMMENT '数据源类型 (MySQL, DB2 等)'
    *   `connection_details` TEXT NOT NULL COMMENT '连接详细信息 (JSON 格式)'
    *   `description` TEXT COMMENT '数据源描述'
    *   `status` varchar(50) NOT NULL COMMENT '连接状态 (Connected, Disconnected, Connecting)'
    *   `sync_status` varchar(50) NOT NULL COMMENT '元数据同步状态 (Pending, Syncing, Synced, Failed)'
    *   `last_sync_time` datetime COMMENT '上次元数据同步时间'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `u_idx_datasource_name` UNIQUE INDEX (`name`)
    *   `idx_datasource_type` INDEX (`type`)

### 1.2 tbl_database

*   **表名**: `tbl_database`
*   **描述**: 存储数据库实例信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '数据库ID'
    *   `data_source_id` varchar(36) NOT NULL COMMENT '所属数据源ID，外键 references tbl_data_source(id)'
    *   `name` varchar(255) NOT NULL COMMENT '数据库名称'
    *   `version` varchar(100) COMMENT '数据库版本'
    *   `character_set` varchar(100) COMMENT '字符集'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_database_datasourceid` INDEX (`data_source_id`)
    *   `idx_database_name` INDEX (`name`)
*   **外键**:
    *   `data_source_id` FOREIGN KEY REFERENCES `tbl_data_source` (`id`)

### 1.3 tbl_table

*   **表名**: `tbl_table`
*   **描述**: 存储表格信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '表格ID'
    *   `database_id` varchar(36) NOT NULL COMMENT '所属数据库ID，外键 references tbl_database(id)'
    *   `name` varchar(255) NOT NULL COMMENT '表格名称'
    *   `schema` TEXT COMMENT '表格结构信息 (JSON 格式)'
    *   `table_type` varchar(100) COMMENT '表格类型 (TABLE, VIEW)'
    *   `record_count` bigint COMMENT '记录数'
    *   `description` TEXT COMMENT '表格描述'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_table_databaseid` INDEX (`database_id`)
    *   `idx_table_name` INDEX (`name`)
*   **外键**:
    *   `database_id` FOREIGN KEY REFERENCES `tbl_database` (`id`)

### 1.4 tbl_column

*   **表名**: `tbl_column`
*   **描述**: 存储列信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '列ID'
    *   `table_id` varchar(36) NOT NULL COMMENT '所属表格ID，外键 references tbl_table(id)'
    *   `name` varchar(255) NOT NULL COMMENT '列名称'
    *   `data_type` varchar(100) NOT NULL COMMENT '数据类型 (VARCHAR, INT, DATETIME)'
    *   `length` int COMMENT '长度'
    *   `precision` int COMMENT '精度'
    *   `nullable` boolean DEFAULT TRUE COMMENT '是否允许为空'
    *   `primary_key` boolean DEFAULT FALSE COMMENT '是否为主键'
    *   `foreign_key` boolean DEFAULT FALSE COMMENT '是否为外键'
    *   `description` TEXT COMMENT '列描述'
    *   `column_metadata` TEXT COMMENT '列元数据 (JSON 格式)'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_column_tableid` INDEX (`table_id`)
    *   `idx_column_name` INDEX (`name`)
*   **外键**:
    *   `table_id` FOREIGN KEY REFERENCES `tbl_table` (`id`)

### 1.5 tbl_query

*   **表名**: `tbl_query`
*   **描述**: 存储用户创建的查询信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '查询ID'
    *   `name` varchar(255) NOT NULL COMMENT '查询名称'
    *   `sql_text` TEXT NOT NULL COMMENT 'SQL 查询语句'
    *   `description` TEXT COMMENT '查询描述'
    *   `data_source_id` varchar(36) NOT NULL COMMENT '关联数据源ID，外键 references tbl_data_source(id)'
    *   `version` int NOT NULL DEFAULT 1 COMMENT '版本号'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_query_datasourceid` INDEX (`data_source_id`)
    *   `idx_query_name` INDEX (`name`)
*   **外键**:
    *   `data_source_id` FOREIGN KEY REFERENCES `tbl_data_source` (`id`)

### 1.6 tbl_saved_query

*   **表名**: `tbl_saved_query`
*   **描述**: 存储用户保存的查询信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '保存查询ID'
    *   `query_id` varchar(36) NOT NULL COMMENT '关联查询ID，外键 references tbl_query(id)'
    *   `name` varchar(255) NOT NULL COMMENT '保存查询名称'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `u_idx_savedquery_queryid` UNIQUE INDEX (`query_id`)
    *   `idx_savedquery_name` INDEX (`name`)
*   **外键**:
    *   `query_id` FOREIGN KEY REFERENCES `tbl_query` (`id`)

### 1.7 tbl_query_history

*   **表名**: `tbl_query_history`
*   **描述**: 存储查询历史记录
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '历史记录ID'
    *   `query_id` varchar(36) NOT NULL COMMENT '关联查询ID，外键 references tbl_query(id)'
    *   `data_source_id` varchar(36) NOT NULL COMMENT '关联数据源ID，外键 references tbl_data_source(id)'
    *   `sql_text` TEXT NOT NULL COMMENT '执行的 SQL 查询语句'
    *   `execution_time` datetime NOT NULL COMMENT '执行时间'
    *   `status` varchar(50) NOT NULL COMMENT '执行状态 (Success, Failed, Timeout)'
    *   `error` TEXT COMMENT '错误信息 (JSON 格式 SQLError)'
    *   `user_id` varchar(36) NOT NULL COMMENT '执行用户ID，外键 references tbl_user(id)'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_queryhistory_queryid` INDEX (`query_id`)
    *   `idx_queryhistory_datasourceid` INDEX (`data_source_id`)
    *   `idx_queryhistory_userid` INDEX (`user_id`)
    *   `idx_queryhistory_executiontime` INDEX (`execution_time`)
*   **外键**:
    *   `query_id` FOREIGN KEY REFERENCES `tbl_query` (`id`)
    *   `data_source_id` FOREIGN KEY REFERENCES `tbl_data_source` (`id`)
    *   `user_id` FOREIGN KEY REFERENCES `tbl_user` (`id`)

### 1.8 tbl_user

*   **表名**: `tbl_user`
*   **描述**: 存储用户信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '用户ID'
    *   `username` varchar(255) UNIQUE NOT NULL COMMENT '用户名'
    *   `password` varchar(255) NOT NULL COMMENT '密码 (AES-256 加密 + Salt)'
    *   `email` varchar(255) COMMENT '邮箱'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `u_idx_user_username` UNIQUE INDEX (`username`)

### 1.9 tbl_role

*   **表名**: `tbl_role`
*   **描述**: 存储角色信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '角色ID'
    *   `name` varchar(255) UNIQUE NOT NULL COMMENT '角色名称'
    *   `description` TEXT COMMENT '角色描述'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `u_idx_role_name` UNIQUE INDEX (`name`)

### 1.10 tbl_permission

*   **表名**: `tbl_permission`
*   **描述**: 存储权限信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '权限ID'
    *   `name` varchar(255) UNIQUE NOT NULL COMMENT '权限名称'
    *   `description` TEXT COMMENT '权限描述'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `u_idx_permission_name` UNIQUE INDEX (`name`)

### 1.11 tbl_low_code_app

*   **表名**: `tbl_low_code_app`
*   **描述**: 存储低代码应用信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '应用ID'
    *   `name` varchar(255) NOT NULL COMMENT '应用名称'
    *   `config` TEXT COMMENT '应用配置信息 (JSON 格式)'
    *   `sync_status` varchar(50) NOT NULL COMMENT '同步状态 (Pending, Syncing, Synced, Failed)'
    *   `last_sync_time` datetime COMMENT '上次同步时间'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_lowcodeapp_name` INDEX (`name`)

### 1.12 tbl_metadata_sync_job

*   **表名**: `tbl_metadata_sync_job`
*   **描述**: 存储元数据同步任务信息
*   **列**:
    *   `id` varchar(36) PRIMARY KEY COMMENT '任务ID'
    *   `data_source_id` varchar(36) NOT NULL COMMENT '关联数据源ID，外键 references tbl_data_source(id)'
    *   `sync_type` varchar(50) NOT NULL COMMENT '同步类型 (Full, Incremental)'
    *   `sync_frequency` varchar(100) COMMENT '同步频率 (Daily, Weekly)'
    *   `last_sync_time` datetime COMMENT '上次同步时间'
    *   `status` varchar(50) NOT NULL COMMENT '任务状态 (Pending, Running, Success, Failed)'
    *   `error` TEXT COMMENT '错误信息 (JSON 格式 SQLError)'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   `nonce` int NOT NULL DEFAULT 0 COMMENT '乐观锁版本号'
*   **索引**:
    *   `idx_metadatasyncjob_datasourceid` INDEX (`data_source_id`)
    *   `idx_metadatasyncjob_synctype` INDEX (`sync_type`)
    *   `idx_metadatasyncjob_status` INDEX (`status`)
*   **外键**:
    *   `data_source_id` FOREIGN KEY REFERENCES `tbl_data_source` (`id`)

### 1.13 tbl_user_role (用户角色关联表)

*   **表名**: `tbl_user_role`
*   **描述**: 存储用户和角色之间的多对多关系
*   **列**:
    *   `user_id` varchar(36) NOT NULL COMMENT '用户ID，外键 references tbl_user(id)'
    *   `role_id` varchar(36) NOT NULL COMMENT '角色ID，外键 references tbl_role(id)'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   **联合主键**: (`user_id`, `role_id`)
*   **索引**:
    *   `idx_userrole_userid` INDEX (`user_id`)
    *   `idx_userrole_roleid` INDEX (`role_id`)
*   **外键**:
    *   `user_id` FOREIGN KEY REFERENCES `tbl_user` (`id`)
    *   `role_id` FOREIGN KEY REFERENCES `tbl_role` (`id`)

### 1.14 tbl_role_permission (角色权限关联表)

*   **表名**: `tbl_role_permission`
*   **描述**: 存储角色和权限之间的多对多关系
*   **列**:
    *   `role_id` varchar(36) NOT NULL COMMENT '角色ID，外键 references tbl_role(id)'
    *   `permission_id` varchar(36) NOT NULL COMMENT '权限ID，外键 references tbl_permission(id)'
    *   `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    *   `created_by` varchar(255) COMMENT '创建用户'
    *   `modified_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
    *   `modified_by` varchar(255) COMMENT '修改用户'
    *   **联合主键**: (`role_id`, `permission_id`)
*   **索引**:
    *   `idx_rolepermission_roleid` INDEX (`role_id`)
    *   `idx_rolepermission_permissionid` INDEX (`permission_id`)
*   **外键**:
    *   `role_id` FOREIGN KEY REFERENCES `tbl_role` (`id`)
    *   `permission_id` FOREIGN KEY REFERENCES `tbl_permission` (`id`)

## 2. UI 元素与数据库表映射 (UI Element to Database Table Mapping)

| UI 界面/元素                 | 数据库表                  | 主要操作                                                                 |
| :--------------------------- | :------------------------ | :----------------------------------------------------------------------- |
| **数据源管理**               | `tbl_data_source`         | 增删改查数据源，连接测试，元数据同步配置                                                 |
| 数据源列表 (表格)            | `tbl_data_source`         | 展示数据源列表，分页，排序，筛选                                                               |
| 数据源表单 (表单)            | `tbl_data_source`         | 创建和编辑数据源                                                                   |
| 数据源连接状态指示器 (图标)   | `tbl_data_source`         | 实时显示数据源连接状态                                                               |
| 元数据同步配置 (表单)        | `tbl_data_source`, `tbl_metadata_sync_job` | 配置元数据同步频率和类型                                                               |
| **元数据浏览**               | `tbl_database`, `tbl_table`, `tbl_column` | 浏览数据库、表格和列的元数据信息                                                               |
| 数据库列表 (树形控件)        | `tbl_data_source`, `tbl_database` | 展示数据源和数据库的层级结构                                                               |
| 表格列表 (表格)              | `tbl_table`               | 展示表格列表，分页，排序，筛选，查看表结构                                                           |
| 列列表 (表格)                | `tbl_column`              | 展示列列表，分页，排序，筛选，查看列详细信息                                                           |
| 表结构详情 (表格)            | `tbl_column`              | 展示表格的列结构详细信息                                                               |
| **查询构建**                 | `tbl_query`, `tbl_data_source`, `tbl_table`, `tbl_column` | 构建 SQL 查询，选择数据源和表格，选择列，设置查询条件，执行查询                                                   |
| 数据源选择器 (下拉框)        | `tbl_data_source`         | 选择要查询的数据源                                                                   |
| 表格选择器 (树形控件/多选框) | `tbl_database`, `tbl_table` | 选择要查询的表格                                                                     |
| 列选择器 (多选框/拖拽列表)   | `tbl_column`              | 选择要查询的列                                                                       |
| 条件构建器 (表单)            |  N/A                      | 构建查询条件 (WHERE 子句)                                                              |
| SQL 编辑器 (文本框)          | `tbl_query`               | 手动编写和编辑 SQL 查询语句，支持语法高亮和自动补全                                                        |
| 查询执行按钮 (按钮)          |  N/A                      | 提交查询请求                                                                       |
| **查询结果展示**             |  N/A                      | 展示查询结果数据                                                                   |
| 结果表格 (表格)              |  N/A                      | 以表格形式展示查询结果数据，支持分页，排序，筛选                                                         |
| 分页控件 (分页器)            |  N/A                      | 分页浏览查询结果                                                                   |
| 排序控件 (表头)              |  N/A                      | 对查询结果进行排序                                                                   |
| 筛选控件 (输入框/下拉框)     |  N/A                      | 对查询结果进行筛选                                                                   |
| 图表可视化 (图表)            |  N/A                      | 以图表形式可视化展示查询结果数据 (柱状图，折线图，饼图，散点图)                                                |
| **查询历史**                 | `tbl_query_history`, `tbl_query`, `tbl_data_source`, `tbl_user` | 查看和管理查询历史记录                                                               |
| 查询历史列表 (表格)          | `tbl_query_history`         | 展示查询历史记录列表，分页，排序，筛选，查看查询详情                                                         |
| 查询详情 (弹窗/页面)         | `tbl_query_history`, `tbl_query`, `tbl_data_source`, `tbl_user` | 展示查询历史记录的详细信息，包括 SQL 语句，执行时间，执行状态，错误信息，执行用户，关联数据源和查询等 |
| **保存查询**                 | `tbl_saved_query`, `tbl_query` | 保存和管理常用查询                                                                   |
| 保存查询列表 (表格)          | `tbl_saved_query`         | 展示保存查询列表，分页，排序，筛选，查看保存查询详情                                                         |
| 保存查询表单 (表单)          | `tbl_saved_query`         | 创建和编辑保存查询                                                                 |
| **用户管理**                 | `tbl_user`, `tbl_role`    | 管理系统用户信息                                                                   |
| 用户列表 (表格)              | `tbl_user`                | 展示用户列表，分页，排序，筛选，查看用户详情                                                             |
| 用户表单 (表单)              | `tbl_user`                | 创建和编辑用户                                                                     |
| **角色管理**                 | `tbl_role`, `tbl_permission` | 管理系统角色信息和权限                                                               |
| 角色列表 (表格)              | `tbl_role`                | 展示角色列表，分页，排序，筛选，查看角色详情                                                             |
| 角色表单 (表单)              | `tbl_role`                | 创建和编辑角色，配置角色权限                                                               |
| 权限列表 (多选框/列表)       | `tbl_permission`          | 选择角色拥有的权限                                                                   |
| **低代码应用管理**           | `tbl_low_code_app`        | 管理与 DataScope 集成的低代码应用                                                           |
| 低代码应用列表 (表格)        | `tbl_low_code_app`        | 展示低代码应用列表，分页，排序，筛选，查看应用详情                                                         |
| 低代码应用表单 (表单)        | `tbl_low_code_app`        | 创建和编辑低代码应用，配置同步信息                                                             |
| **元数据同步任务管理**       | `tbl_metadata_sync_job`, `tbl_data_source` | 管理元数据同步任务                                                                   |
| 同步任务列表 (表格)          | `tbl_metadata_sync_job`         | 展示同步任务列表，分页，排序，筛选，查看任务详情                                                             |
| 同步任务表单 (表单)          | `tbl_metadata_sync_job`         | 创建和编辑同步任务，配置同步频率和类型                                                             |
| 手动同步按钮 (按钮)          | `tbl_data_source`         | 手动触发元数据同步                                                                   |

---
**版本记录**

[2025-03-18 12:35:00] - 初始版本，创建数据库表结构与 UI 映射文档。