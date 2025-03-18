-- 数据源表
CREATE TABLE tbl_data_source (
    id VARCHAR(36) PRIMARY KEY COMMENT '数据源ID',
    name VARCHAR(100) NOT NULL COMMENT '数据源名称',
    type VARCHAR(20) NOT NULL COMMENT '数据源类型',
    host VARCHAR(255) NOT NULL COMMENT '服务器地址',
    port INT NOT NULL COMMENT '端口',
    database_name VARCHAR(100) NOT NULL COMMENT '数据库名称',
    username VARCHAR(100) NOT NULL COMMENT '用户名',
    encrypted_password VARCHAR(255) NOT NULL COMMENT '加密密码',
    encryption_salt VARCHAR(100) NOT NULL COMMENT '加密盐值',
    connection_properties JSON COMMENT '连接参数',
    last_sync_time TIMESTAMP COMMENT '最后同步时间',
    active BOOLEAN DEFAULT TRUE COMMENT '是否激活',
    description VARCHAR(500) COMMENT '描述',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT uk_tbl_data_source_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据源信息表';

-- 模式表
CREATE TABLE tbl_schema_info (
    id VARCHAR(36) PRIMARY KEY COMMENT '模式ID',
    data_source_id VARCHAR(36) NOT NULL COMMENT '数据源ID',
    name VARCHAR(100) NOT NULL COMMENT '模式名称',
    description VARCHAR(500) COMMENT '描述',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_schema_data_source FOREIGN KEY (data_source_id) REFERENCES tbl_data_source(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_schema_data_source_name UNIQUE (data_source_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据库模式信息表';

-- 表信息表
CREATE TABLE tbl_table_info (
    id VARCHAR(36) PRIMARY KEY COMMENT '表ID',
    schema_id VARCHAR(36) NOT NULL COMMENT '模式ID',
    name VARCHAR(100) NOT NULL COMMENT '表名',
    type VARCHAR(20) NOT NULL COMMENT '表类型',
    description VARCHAR(500) COMMENT '描述',
    estimated_row_count BIGINT COMMENT '预估行数',
    data_size BIGINT COMMENT '数据大小(字节)',
    index_size BIGINT COMMENT '索引大小(字节)',
    last_analyzed TIMESTAMP COMMENT '最后分析时间',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_table_schema FOREIGN KEY (schema_id) REFERENCES tbl_schema_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_table_schema_name UNIQUE (schema_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表信息表';

-- 列信息表
CREATE TABLE tbl_column_info (
    id VARCHAR(36) PRIMARY KEY COMMENT '列ID',
    table_id VARCHAR(36) NOT NULL COMMENT '表ID',
    name VARCHAR(100) NOT NULL COMMENT '列名',
    data_type VARCHAR(50) NOT NULL COMMENT '数据类型',
    column_type VARCHAR(100) COMMENT '列类型',
    ordinal_position INT NOT NULL COMMENT '列位置',
    length INT COMMENT '长度',
    precision INT COMMENT '精度',
    scale INT COMMENT '小数位数',
    nullable BOOLEAN DEFAULT TRUE COMMENT '是否可空',
    default_value VARCHAR(255) COMMENT '默认值',
    description VARCHAR(500) COMMENT '描述',
    is_primary_key BOOLEAN DEFAULT FALSE COMMENT '是否主键',
    is_foreign_key BOOLEAN DEFAULT FALSE COMMENT '是否外键',
    is_auto_increment BOOLEAN DEFAULT FALSE COMMENT '是否自增',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_column_table FOREIGN KEY (table_id) REFERENCES tbl_table_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_column_table_name UNIQUE (table_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='列信息表';

-- 索引信息表
CREATE TABLE tbl_index_info (
    id VARCHAR(36) PRIMARY KEY COMMENT '索引ID',
    table_id VARCHAR(36) NOT NULL COMMENT '表ID',
    name VARCHAR(100) NOT NULL COMMENT '索引名称',
    type VARCHAR(20) NOT NULL COMMENT '索引类型',
    is_unique BOOLEAN DEFAULT FALSE COMMENT '是否唯一索引',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_index_table FOREIGN KEY (table_id) REFERENCES tbl_table_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_index_table_name UNIQUE (table_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='索引信息表';

-- 索引列关联表
CREATE TABLE tbl_index_column (
    id VARCHAR(36) PRIMARY KEY COMMENT '索引列ID',
    index_id VARCHAR(36) NOT NULL COMMENT '索引ID',
    column_id VARCHAR(36) NOT NULL COMMENT '列ID',
    ordinal_position INT NOT NULL COMMENT '位置',
    sort_order VARCHAR(4) COMMENT '排序方向',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_index_column_index FOREIGN KEY (index_id) REFERENCES tbl_index_info(id) ON DELETE CASCADE,
    CONSTRAINT fk_tbl_index_column_column FOREIGN KEY (column_id) REFERENCES tbl_column_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_index_column UNIQUE (index_id, column_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='索引列关联表';

-- 表关系表
CREATE TABLE tbl_table_relationship (
    id VARCHAR(36) PRIMARY KEY COMMENT '关系ID',
    source_table_id VARCHAR(36) NOT NULL COMMENT '源表ID',
    target_table_id VARCHAR(36) NOT NULL COMMENT '目标表ID',
    relationship_type VARCHAR(50) NOT NULL COMMENT '关系类型',
    relationship_source VARCHAR(50) NOT NULL COMMENT '关系来源',
    weight DOUBLE NOT NULL COMMENT '权重',
    mapping_columns JSON COMMENT '映射列',
    is_inferred BOOLEAN DEFAULT FALSE COMMENT '是否推断的关系',
    confidence DOUBLE COMMENT '置信度',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_relationship_source FOREIGN KEY (source_table_id) REFERENCES tbl_table_info(id) ON DELETE CASCADE,
    CONSTRAINT fk_tbl_relationship_target FOREIGN KEY (target_table_id) REFERENCES tbl_table_info(id) ON DELETE CASCADE,
    CONSTRAINT uk_tbl_relationship UNIQUE (source_table_id, target_table_id, relationship_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表关系表';

-- 保存的查询表
CREATE TABLE tbl_saved_query (
    id VARCHAR(36) PRIMARY KEY COMMENT '查询ID',
    data_source_id VARCHAR(36) NOT NULL COMMENT '数据源ID',
    name VARCHAR(100) NOT NULL COMMENT '查询名称',
    description TEXT COMMENT '查询描述',
    sql_text TEXT NOT NULL COMMENT 'SQL文本',
    parameters JSON COMMENT '参数定义',
    default_values JSON COMMENT '默认参数值',
    result_schema JSON COMMENT '结果集模式',
    tags JSON COMMENT '标签',
    is_public BOOLEAN DEFAULT FALSE COMMENT '是否公开',
    query_type VARCHAR(20) DEFAULT 'SELECT' COMMENT '查询类型',
    execution_count INT DEFAULT 0 COMMENT '执行次数',
    average_execution_time BIGINT DEFAULT 0 COMMENT '平均执行时间(毫秒)',
    last_executed_at TIMESTAMP COMMENT '最后执行时间',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by VARCHAR(36) NOT NULL COMMENT '创建人',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    updated_by VARCHAR(36) COMMENT '更新人',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    CONSTRAINT fk_tbl_saved_query_data_source FOREIGN KEY (data_source_id) REFERENCES tbl_data_source(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='保存的查询表';

-- 查询执行记录表
CREATE TABLE tbl_query_execution (
    id VARCHAR(36) PRIMARY KEY COMMENT '执行ID',
    query_id VARCHAR(36) NOT NULL COMMENT '查询ID',
    parameters JSON COMMENT '执行参数',
    start_time TIMESTAMP NOT NULL COMMENT '开始时间',
    end_time TIMESTAMP COMMENT '结束时间',
    status VARCHAR(20) NOT NULL COMMENT '执行状态',
    error_message TEXT COMMENT '错误信息',
    result_count BIGINT COMMENT '结果数量',
    execution_time BIGINT COMMENT '执行时间(毫秒)',
    created_by VARCHAR(36) NOT NULL COMMENT '执行人',
    CONSTRAINT fk_tbl_execution_query FOREIGN KEY (query_id) REFERENCES tbl_saved_query(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='查询执行记录表';

-- 创建索引
CREATE INDEX idx_tbl_data_source_type ON tbl_data_source(type);
CREATE INDEX idx_tbl_data_source_active ON tbl_data_source(active);
CREATE INDEX idx_tbl_schema_data_source_id ON tbl_schema_info(data_source_id);
CREATE INDEX idx_tbl_table_schema_id ON tbl_table_info(schema_id);
CREATE INDEX idx_tbl_column_table_id ON tbl_column_info(table_id);
CREATE INDEX idx_tbl_index_table_id ON tbl_index_info(table_id);
CREATE INDEX idx_tbl_saved_query_data_source_id ON tbl_saved_query(data_source_id);
CREATE INDEX idx_tbl_saved_query_created_by ON tbl_saved_query(created_by);
CREATE INDEX idx_tbl_saved_query_is_public ON tbl_saved_query(is_public);
CREATE INDEX idx_tbl_query_execution_query_id ON tbl_query_execution(query_id);
CREATE INDEX idx_tbl_query_execution_start_time ON tbl_query_execution(start_time);
CREATE INDEX idx_tbl_query_execution_status ON tbl_query_execution(status);