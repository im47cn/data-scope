-- 创建应用表
CREATE TABLE IF NOT EXISTS app (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL COMMENT '应用唯一编码',
    name VARCHAR(100) NOT NULL COMMENT '应用名称',
    description TEXT COMMENT '应用描述',
    icon VARCHAR(255) COMMENT '应用图标URL',
    type VARCHAR(50) COMMENT '应用类型',
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '应用版本',
    home_page_id BIGINT COMMENT '首页ID',
    publish_status TINYINT DEFAULT 0 COMMENT '发布状态: 0-未发布, 1-已发布',
    published_at TIMESTAMP NULL COMMENT '发布时间',
    theme VARCHAR(50) COMMENT '应用主题',
    style_config JSON COMMENT '应用样式配置',
    settings JSON COMMENT '全局设置',
    permissions JSON COMMENT '权限配置',
    routes JSON COMMENT '路由配置',
    menus JSON COMMENT '菜单配置',
    global_state JSON COMMENT '全局状态',
    query_ids JSON COMMENT '关联查询ID列表',
    data_source_ids JSON COMMENT '关联数据源ID列表',
    custom_config JSON COMMENT '自定义配置项',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_app_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码应用';

-- 创建页面表
CREATE TABLE IF NOT EXISTS page (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_id BIGINT NOT NULL COMMENT '所属应用ID',
    title VARCHAR(100) NOT NULL COMMENT '页面标题',
    name VARCHAR(50) NOT NULL COMMENT '页面名称',
    description TEXT COMMENT '页面描述',
    icon VARCHAR(255) COMMENT '页面图标URL',
    path VARCHAR(255) COMMENT '页面路径',
    is_home BOOLEAN DEFAULT FALSE COMMENT '是否为首页',
    layout_type VARCHAR(50) DEFAULT 'fluid' COMMENT '页面布局类型',
    template VARCHAR(50) COMMENT '页面模板',
    display_order INT DEFAULT 0 COMMENT '显示顺序',
    visible BOOLEAN DEFAULT TRUE COMMENT '是否可见',
    require_auth BOOLEAN DEFAULT TRUE COMMENT '是否需要认证',
    cached BOOLEAN DEFAULT FALSE COMMENT '是否缓存',
    components JSON COMMENT '页面组件列表',
    layout_config JSON COMMENT '布局配置',
    style JSON COMMENT '页面样式',
    theme VARCHAR(50) COMMENT '页面主题',
    script TEXT COMMENT '页面脚本',
    stylesheet TEXT COMMENT '页面样式表',
    state JSON COMMENT '页面状态',
    parameters JSON COMMENT '页面参数',
    events JSON COMMENT '页面事件',
    lifecycle_hooks JSON COMMENT '页面生命周期钩子',
    responsive_settings JSON COMMENT '响应式配置',
    permissions JSON COMMENT '权限配置',
    query_ids JSON COMMENT '关联查询ID列表',
    seo_settings JSON COMMENT 'SEO设置',
    custom_config JSON COMMENT '自定义配置项',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_page_app_id (app_id),
    UNIQUE KEY uk_page_app_name (app_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码页面';

-- 创建组件模板表
CREATE TABLE IF NOT EXISTS component_template (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(50) NOT NULL COMMENT '组件类型',
    name VARCHAR(100) NOT NULL COMMENT '组件名称',
    title VARCHAR(100) COMMENT '组件显示标题',
    icon VARCHAR(255) COMMENT '组件图标URL',
    description TEXT COMMENT '组件描述',
    is_container BOOLEAN DEFAULT FALSE COMMENT '是否为容器组件',
    category VARCHAR(50) COMMENT '组件分类',
    props_schema JSON COMMENT '属性定义Schema',
    default_props JSON COMMENT '默认属性值',
    style_schema JSON COMMENT '样式定义Schema',
    default_style JSON COMMENT '默认样式值',
    event_schema JSON COMMENT '事件定义Schema',
    validation_schema JSON COMMENT '验证规则Schema',
    data_binding_schema JSON COMMENT '数据绑定定义Schema',
    preview_image VARCHAR(255) COMMENT '预览图URL',
    usage_examples JSON COMMENT '使用示例',
    version VARCHAR(20) DEFAULT '1.0.0' COMMENT '组件版本',
    dependencies JSON COMMENT '依赖项',
    custom_config JSON COMMENT '自定义配置项',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_component_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组件模板';

-- 创建应用数据表
CREATE TABLE IF NOT EXISTS app_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_id BIGINT NOT NULL COMMENT '应用ID',
    table_name VARCHAR(100) NOT NULL COMMENT '数据表名称',
    description TEXT COMMENT '数据表描述',
    schema_definition JSON NOT NULL COMMENT '表结构定义',
    data_source_id BIGINT COMMENT '关联的数据源ID',
    custom_query_id BIGINT COMMENT '自定义查询ID',
    is_virtual BOOLEAN DEFAULT FALSE COMMENT '是否为虚拟表',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_app_data_app_id (app_id),
    UNIQUE KEY uk_app_data_app_table (app_id, table_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用数据表';

-- 创建低代码与查询关联表
CREATE TABLE IF NOT EXISTS lowcode_query_mapping (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    app_id BIGINT NOT NULL COMMENT '应用ID',
    page_id BIGINT COMMENT '页面ID',
    component_id VARCHAR(50) COMMENT '组件ID',
    query_id BIGINT NOT NULL COMMENT '查询ID',
    binding_type VARCHAR(50) NOT NULL COMMENT '绑定类型：DATA_SOURCE, FILTER, RESULT',
    binding_config JSON COMMENT '绑定配置',
    display_config JSON COMMENT '显示配置',
    filter_mapping JSON COMMENT '过滤条件映射',
    created_by BIGINT NOT NULL COMMENT '创建人ID',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_by BIGINT COMMENT '更新人ID',
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_mapping_app_id (app_id),
    INDEX idx_mapping_page_id (page_id),
    INDEX idx_mapping_query_id (query_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='低代码查询映射';

-- 添加外键约束
ALTER TABLE page
ADD CONSTRAINT fk_page_app
FOREIGN KEY (app_id) REFERENCES app (id)
ON DELETE CASCADE;

ALTER TABLE app_data
ADD CONSTRAINT fk_app_data_app
FOREIGN KEY (app_id) REFERENCES app (id)
ON DELETE CASCADE;

ALTER TABLE lowcode_query_mapping
ADD CONSTRAINT fk_mapping_app
FOREIGN KEY (app_id) REFERENCES app (id)
ON DELETE CASCADE;

ALTER TABLE lowcode_query_mapping
ADD CONSTRAINT fk_mapping_page
FOREIGN KEY (page_id) REFERENCES page (id)
ON DELETE CASCADE;