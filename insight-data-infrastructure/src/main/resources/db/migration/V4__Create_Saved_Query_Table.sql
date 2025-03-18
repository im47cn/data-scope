-- Create saved query table
CREATE TABLE saved_query (
    id VARCHAR(50) PRIMARY KEY,
    data_source_id VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    sql TEXT NOT NULL,
    parameters JSONB,
    default_values JSONB,
    tags JSONB,
    is_public BOOLEAN DEFAULT FALSE,
    created_by VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    usage_count INTEGER DEFAULT 0,
    average_execution_time BIGINT DEFAULT 0,
    last_executed_at TIMESTAMP,
    FOREIGN KEY (data_source_id) REFERENCES data_source(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX idx_saved_query_data_source_id ON saved_query(data_source_id);
CREATE INDEX idx_saved_query_name ON saved_query(name);
CREATE INDEX idx_saved_query_created_by ON saved_query(created_by);
CREATE INDEX idx_saved_query_is_public ON saved_query(is_public);
CREATE INDEX idx_saved_query_tags ON saved_query USING gin(tags);

-- Create triggers
CREATE OR REPLACE FUNCTION update_saved_query_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER saved_query_updated_at
    BEFORE UPDATE ON saved_query
    FOR EACH ROW
    EXECUTE FUNCTION update_saved_query_updated_at();

-- Comments
COMMENT ON TABLE saved_query IS '保存的查询模板';
COMMENT ON COLUMN saved_query.id IS '模板ID';
COMMENT ON COLUMN saved_query.data_source_id IS '数据源ID';
COMMENT ON COLUMN saved_query.name IS '模板名称';
COMMENT ON COLUMN saved_query.description IS '模板描述';
COMMENT ON COLUMN saved_query.sql IS 'SQL语句';
COMMENT ON COLUMN saved_query.parameters IS '参数定义';
COMMENT ON COLUMN saved_query.default_values IS '默认参数值';
COMMENT ON COLUMN saved_query.tags IS '标签';
COMMENT ON COLUMN saved_query.is_public IS '是否公开';
COMMENT ON COLUMN saved_query.created_by IS '创建人';
COMMENT ON COLUMN saved_query.created_at IS '创建时间';
COMMENT ON COLUMN saved_query.updated_at IS '更新时间';
COMMENT ON COLUMN saved_query.usage_count IS '使用次数';
COMMENT ON COLUMN saved_query.average_execution_time IS '平均执行时间(毫秒)';
COMMENT ON COLUMN saved_query.last_executed_at IS '最后执行时间';