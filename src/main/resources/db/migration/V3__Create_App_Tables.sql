-- 创建低代码应用表
CREATE TABLE app (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    icon VARCHAR(255),
    type VARCHAR(50),
    version VARCHAR(50),
    home_page_id BIGINT,
    publish_status INT,
    published_at TIMESTAMP,
    theme JSON,
    style_config JSON,
    settings JSON,
    permissions JSON,
    routes JSON,
    menus JSON,
    global_state JSON,
    custom_config JSON,
    created_by VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(50),
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 唯一约束
    CONSTRAINT uk_app_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建应用-查询关联表
CREATE TABLE app_query (
    app_id BIGINT NOT NULL,
    query_id BIGINT NOT NULL,
    PRIMARY KEY (app_id, query_id),
    
    -- 外键约束
    CONSTRAINT fk_app_query_app_id
        FOREIGN KEY (app_id)
        REFERENCES app (id)
        ON DELETE CASCADE
    -- 注意：query_id 目前没有外键约束，因为相关表可能尚未创建
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建应用-数据源关联表
CREATE TABLE app_data_source (
    app_id BIGINT NOT NULL,
    data_source_id BIGINT NOT NULL,
    PRIMARY KEY (app_id, data_source_id),
    
    -- 外键约束
    CONSTRAINT fk_app_data_source_app_id
        FOREIGN KEY (app_id)
        REFERENCES app (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_app_data_source_data_source_id
        FOREIGN KEY (data_source_id)
        REFERENCES data_source (id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_app_publish_status ON app(publish_status);
CREATE INDEX idx_app_published_at ON app(published_at);
CREATE INDEX idx_app_created_at ON app(created_at);
CREATE INDEX idx_app_updated_at ON app(updated_at);
CREATE INDEX idx_app_query_query_id ON app_query(query_id);
CREATE INDEX idx_app_data_source_data_source_id ON app_data_source(data_source_id);