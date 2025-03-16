-- 创建低代码应用表
CREATE TABLE app (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon VARCHAR(255),
    type VARCHAR(50),
    version VARCHAR(20),
    home_page_id BIGINT,
    publish_status INT,
    published_at TIMESTAMP,
    theme VARCHAR(50),
    style_config JSON,
    settings JSON,
    permissions JSON,
    routes JSON,
    menus JSON,
    global_state JSON,
    query_ids JSON,
    data_source_ids JSON,
    custom_config JSON,
    created_by BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by BIGINT,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    -- 唯一约束
    CONSTRAINT uk_app_code UNIQUE (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 创建索引
CREATE INDEX idx_app_type ON app(type);
CREATE INDEX idx_app_publish_status ON app(publish_status);
CREATE INDEX idx_app_created_by ON app(created_by);