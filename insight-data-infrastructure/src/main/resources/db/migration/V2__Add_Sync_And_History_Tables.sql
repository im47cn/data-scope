-- 元数据同步作业表
CREATE TABLE tbl_metadata_sync_job (
    id VARCHAR(36) PRIMARY KEY COMMENT '作业ID',
    data_source_id VARCHAR(36) NOT NULL COMMENT '数据源ID',
    type VARCHAR(20) NOT NULL COMMENT '同步类型',
    status VARCHAR(20) NOT NULL COMMENT '状态',
    start_time TIMESTAMP COMMENT '开始时间',
    end_time TIMESTAMP COMMENT '结束时间',
    progress INT DEFAULT 0 COMMENT '进度',
    total_items INT COMMENT '总项数',
    processed_items INT DEFAULT 0 COMMENT '已处理项数',
    parameters JSON COMMENT '同步参数',
    error_message TEXT COMMENT '错误信息',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT fk_tbl_sync_job_data_source FOREIGN KEY (data_source_id) REFERENCES tbl_data_source(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='元数据同步作业表';

-- 表变更历史表
CREATE TABLE tbl_table_change_history (
    id VARCHAR(36) PRIMARY KEY COMMENT '历史ID',
    table_id VARCHAR(36) NOT NULL COMMENT '表ID',
    change_type VARCHAR(20) NOT NULL COMMENT '变更类型',
    change_details JSON NOT NULL COMMENT '变更详情',
    detected_at TIMESTAMP NOT NULL COMMENT '检测时间',
    sync_job_id VARCHAR(36) COMMENT '同步作业ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    CONSTRAINT fk_tbl_change_history_table FOREIGN KEY (table_id) REFERENCES tbl_table_info(id) ON DELETE CASCADE,
    CONSTRAINT fk_tbl_change_history_job FOREIGN KEY (sync_job_id) REFERENCES tbl_metadata_sync_job(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='表变更历史表';

-- 创建索引
CREATE INDEX idx_tbl_sync_job_data_source_id ON tbl_metadata_sync_job(data_source_id);
CREATE INDEX idx_tbl_sync_job_status ON tbl_metadata_sync_job(status);
CREATE INDEX idx_tbl_sync_job_created_at ON tbl_metadata_sync_job(created_at);
CREATE INDEX idx_tbl_change_history_table_id ON tbl_table_change_history(table_id);
CREATE INDEX idx_tbl_change_history_detected_at ON tbl_table_change_history(detected_at);
CREATE INDEX idx_tbl_change_history_sync_job_id ON tbl_table_change_history(sync_job_id);