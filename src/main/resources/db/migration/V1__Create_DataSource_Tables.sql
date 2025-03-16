-- 数据源表
CREATE TABLE IF NOT EXISTS data_source (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(20) NOT NULL,
  host VARCHAR(255) NOT NULL,
  port INT NOT NULL,
  database_name VARCHAR(100) NOT NULL,
  username VARCHAR(100) NOT NULL,
  encrypted_password VARCHAR(255) NOT NULL,
  encryption_salt VARCHAR(100) NOT NULL,
  connection_properties JSON,
  last_sync_time TIMESTAMP,
  active BOOLEAN DEFAULT TRUE,
  description VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT uk_data_source_name UNIQUE (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 模式表
CREATE TABLE IF NOT EXISTS schema_info (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  data_source_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(500),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_schema_data_source FOREIGN KEY (data_source_id) REFERENCES data_source(id) ON DELETE CASCADE,
  CONSTRAINT uk_schema_data_source_name UNIQUE (data_source_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 表信息表
CREATE TABLE IF NOT EXISTS table_info (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  schema_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(20) NOT NULL,
  description VARCHAR(500),
  estimated_row_count BIGINT,
  data_size BIGINT,
  index_size BIGINT,
  last_analyzed TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_table_schema FOREIGN KEY (schema_id) REFERENCES schema_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_table_schema_name UNIQUE (schema_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 列信息表
CREATE TABLE IF NOT EXISTS column_info (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  table_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  data_type VARCHAR(50) NOT NULL,
  column_type VARCHAR(100),
  ordinal_position INT NOT NULL,
  length INT,
  `precision` INT,
  scale INT,
  nullable BOOLEAN DEFAULT TRUE,
  default_value VARCHAR(255),
  description VARCHAR(500),
  is_primary_key BOOLEAN DEFAULT FALSE,
  is_foreign_key BOOLEAN DEFAULT FALSE,
  is_auto_increment BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_column_table FOREIGN KEY (table_id) REFERENCES table_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_column_table_name UNIQUE (table_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 索引信息表
CREATE TABLE IF NOT EXISTS index_info (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  table_id BIGINT NOT NULL,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(20) NOT NULL,
  is_unique BOOLEAN DEFAULT FALSE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_index_table FOREIGN KEY (table_id) REFERENCES table_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_index_table_name UNIQUE (table_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 索引列关联表
CREATE TABLE IF NOT EXISTS index_column (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  index_id BIGINT NOT NULL,
  column_id BIGINT NOT NULL,
  ordinal_position INT NOT NULL,
  sort_order VARCHAR(4),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_index_column_index FOREIGN KEY (index_id) REFERENCES index_info(id) ON DELETE CASCADE,
  CONSTRAINT fk_index_column_column FOREIGN KEY (column_id) REFERENCES column_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_index_column UNIQUE (index_id, column_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 外键信息表
CREATE TABLE IF NOT EXISTS foreign_key (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  source_table_id BIGINT NOT NULL,
  target_table_id BIGINT NOT NULL,
  update_rule VARCHAR(20),
  delete_rule VARCHAR(20),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_foreign_key_source_table FOREIGN KEY (source_table_id) REFERENCES table_info(id) ON DELETE CASCADE,
  CONSTRAINT fk_foreign_key_target_table FOREIGN KEY (target_table_id) REFERENCES table_info(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 外键列关联表
CREATE TABLE IF NOT EXISTS foreign_key_column (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  foreign_key_id BIGINT NOT NULL,
  source_column_id BIGINT NOT NULL,
  target_column_id BIGINT NOT NULL,
  ordinal_position INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_foreign_key_column_foreign_key FOREIGN KEY (foreign_key_id) REFERENCES foreign_key(id) ON DELETE CASCADE,
  CONSTRAINT fk_foreign_key_column_source_column FOREIGN KEY (source_column_id) REFERENCES column_info(id) ON DELETE CASCADE,
  CONSTRAINT fk_foreign_key_column_target_column FOREIGN KEY (target_column_id) REFERENCES column_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_foreign_key_column UNIQUE (foreign_key_id, source_column_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 表关系表（用于存储推断的关系）
CREATE TABLE IF NOT EXISTS table_relation (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  source_table_id BIGINT NOT NULL,
  target_table_id BIGINT NOT NULL,
  relation_type VARCHAR(20) NOT NULL,
  is_inferred BOOLEAN DEFAULT TRUE,
  confidence DOUBLE,
  mapping_columns JSON,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_table_relation_source_table FOREIGN KEY (source_table_id) REFERENCES table_info(id) ON DELETE CASCADE,
  CONSTRAINT fk_table_relation_target_table FOREIGN KEY (target_table_id) REFERENCES table_info(id) ON DELETE CASCADE,
  CONSTRAINT uk_table_relation UNIQUE (source_table_id, target_table_id, relation_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 元数据版本表
CREATE TABLE IF NOT EXISTS metadata_version (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  data_source_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  created_by VARCHAR(100),
  description VARCHAR(500),
  snapshot_location VARCHAR(255),
  CONSTRAINT fk_metadata_version_data_source FOREIGN KEY (data_source_id) REFERENCES data_source(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 元数据变更记录表
CREATE TABLE IF NOT EXISTS metadata_change (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  version_id BIGINT NOT NULL,
  object_type VARCHAR(20) NOT NULL,
  object_name VARCHAR(100) NOT NULL,
  change_type VARCHAR(20) NOT NULL,
  old_value TEXT,
  new_value TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_metadata_change_version FOREIGN KEY (version_id) REFERENCES metadata_version(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 同步作业表
CREATE TABLE IF NOT EXISTS sync_job (
  id VARCHAR(36) PRIMARY KEY,
  data_source_id BIGINT NOT NULL,
  start_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  end_time TIMESTAMP,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  progress INT DEFAULT 0,
  error_message TEXT,
  schema_count INT DEFAULT 0,
  table_count INT DEFAULT 0,
  column_count INT DEFAULT 0,
  index_count INT DEFAULT 0,
  foreign_key_count INT DEFAULT 0,
  added_object_count INT DEFAULT 0,
  modified_object_count INT DEFAULT 0,
  removed_object_count INT DEFAULT 0,
  CONSTRAINT fk_sync_job_data_source FOREIGN KEY (data_source_id) REFERENCES data_source(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_data_source_type ON data_source(type);
CREATE INDEX idx_data_source_active ON data_source(active);
CREATE INDEX idx_schema_data_source_id ON schema_info(data_source_id);
CREATE INDEX idx_table_schema_id ON table_info(schema_id);
CREATE INDEX idx_column_table_id ON column_info(table_id);
CREATE INDEX idx_index_table_id ON index_info(table_id);
CREATE INDEX idx_foreign_key_source_table_id ON foreign_key(source_table_id);
CREATE INDEX idx_foreign_key_target_table_id ON foreign_key(target_table_id);
CREATE INDEX idx_table_relation_source_table_id ON table_relation(source_table_id);
CREATE INDEX idx_table_relation_target_table_id ON table_relation(target_table_id);
CREATE INDEX idx_metadata_version_data_source_id ON metadata_version(data_source_id);
CREATE INDEX idx_metadata_change_version_id ON metadata_change(version_id);
CREATE INDEX idx_sync_job_data_source_id ON sync_job(data_source_id);
CREATE INDEX idx_sync_job_status ON sync_job(status);
