-- 创建表关系表
CREATE TABLE table_relationship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    data_source_id BIGINT NOT NULL,
    source_table VARCHAR(255) NOT NULL,
    source_column VARCHAR(255) NOT NULL,
    target_table VARCHAR(255) NOT NULL,
    target_column VARCHAR(255) NOT NULL,
    relationship_type VARCHAR(50) NOT NULL,
    relationship_source VARCHAR(50) NOT NULL,
    weight DOUBLE NOT NULL,
    frequency INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    
    -- 索引
    INDEX idx_data_source_id (data_source_id),
    INDEX idx_source_table (source_table),
    INDEX idx_target_table (target_table),
    
    -- 外键约束
    CONSTRAINT fk_table_relationship_data_source
        FOREIGN KEY (data_source_id)
        REFERENCES data_source (id)
        ON DELETE CASCADE
);

-- 添加唯一约束
ALTER TABLE table_relationship
ADD CONSTRAINT uk_table_relationship
UNIQUE (data_source_id, source_table, source_column, target_table, target_column);