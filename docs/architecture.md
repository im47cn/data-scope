# DataScope 系统架构设计

## 1. 概述

DataScope 是一个全面的数据管理和查询系统，旨在帮助用户高效地发现、查询和管理多数据源的数据。系统采用模块化设计，基于领域驱动设计 (DDD) 思想和六边形架构模式，确保系统的可扩展性、可维护性和安全性。

## 2. 架构图

[系统架构图 - 待补充，可以使用 PlantUML 或其他工具绘制]

## 3. 模块详细设计

### 3.1 数据源管理模块 (DataSource Management Module)

*   **功能:**
    *   数据源注册与配置 (支持 MySQL, DB2 等)
    *   连接管理与连接池维护
    *   元数据同步 (全量和增量)
    *   连接状态监控与告警
*   **技术:** Spring Boot, MyBatis, 连接池 (如 HikariCP)
*   **领域服务:** DataSourceService, MetadataSyncService, ConnectionManager
*   **基础设施:** DataSourceRepository, MetadataRepository, DB2Adapter, MySQLAdapter

### 3.2 元数据管理模块 (Metadata Management Module)

*   **功能:**
    *   元数据存储 (数据库, 表, 字段, 索引, 关系)
    *   元数据查询与检索
    *   元数据缓存 (Redis)
*   **技术:** Spring Boot, MyBatis, Redis
*   **领域服务:** MetadataService
*   **基础设施:** MetadataRepository, RedisRepository

### 3.3 查询构建模块 (Query Builder Module)

*   **功能:**
    *   SQL 查询编辑器 (语法高亮, 自动补全, 查询验证)
    *   自然语言查询界面
    *   查询参数化
    *   查询模板管理
    *   查询历史记录
*   **技术:** Spring Boot, 前端框架 (HTML, Tailwind CSS, Element UI/Ant Design), SQL 解析器, 自然语言处理 (NLP) 库
*   **领域服务:** QueryBuilderService, NLQueryService, QueryHistoryService
*   **基础设施:** QueryParser, NLProcessor

### 3.4 查询执行引擎 (Query Execution Engine)

*   **功能:**
    *   查询接收与解析
    *   查询计划生成与优化
    *   数据源适配器选择
    *   查询执行与结果处理
    *   查询超时控制
    *   速率限制 (基于用户)
    *   执行计划分析
*   **技术:** Spring Boot, MyBatis, 数据库驱动, 查询优化器
*   **领域服务:** QueryExecutionService
*   **基础设施:** QueryExecutor, DB2Executor, MySQLExecutor

### 3.5 AI 辅助模块 (AI Assistant Module)

*   **功能:**
    *   自然语言生成 SQL (NL to SQL)
    *   数据库关系推断
    *   查询优化建议
    *   自然语言数据探索与结果解释
*   **技术:** Spring Boot, 大型语言模型 (LLM) API (如 OpenAI), NLP 库
*   **领域服务:** AIService
*   **基础设施:** LLMClient, RelationInferencer, QueryOptimizer, NLDataExplorer

### 3.6 低代码集成模块 (Low-Code Integration Module)

*   **功能:**
    *   REST API (查询执行, 元数据获取, 配置管理)
    *   WebHook (查询结果推送, 数据变更通知)
    *   参数化查询支持
    *   与流行低代码平台集成 (如 Node-RED, Appsmith)
*   **技术:** Spring Boot, Spring REST, WebHook 技术
*   **领域服务:** LowCodeIntegrationService
*   **基础设施:** RESTController, WebHookSender

### 3.7 版本控制模块 (Version Control Module)

*   **功能:**
    *   SQL 查询版本控制
    *   API 版本控制
    *   历史记录追踪
    *   差异比较
    *   版本回滚
    *   协作编辑
*   **技术:** Spring Boot, 版本控制系统 (如 Git), 数据库
*   **领域服务:** VersionControlService
*   **基础设施:** VersionRepository, DiffTool

### 3.8 安全模块 (Security Module)

*   **功能:**
    *   用户认证 (Authentication)
    *   用户授权 (Authorization) - 基于角色 (RBAC)
    *   密码加密 (AES-256 with salt)
    *   会话管理
    *   审计日志 (操作日志, 查询日志, 安全日志)
*   **技术:** Spring Boot Security, JWT, AES-256, Logback
*   **领域服务:** AuthService, RBACService, AuditLogService
*   **基础设施:** UserRepository, RoleRepository, PasswordEncryptor, SessionManager, LogWriter

### 3.9 UI 模块 (UI Module)

*   **功能:**
    *   数据源管理界面
    *   元数据浏览界面 (数据库, 表, 字段)
    *   查询构建界面 (SQL, 自然语言)
    *   查询结果展示界面 (表格, 分页, 排序, 过滤)
    *   图表可视化界面 (柱状图, 折线图, 饼图, 散点图)
    *   用户个性化设置界面 (主题, 列顺序, 默认排序, 页面布局, 保存查询)
*   **技术:** HTML, Tailwind CSS, FontAwesome 6, Element UI/Ant Design, JavaScript, Vue.js/React (待定), 图表库 (如 ECharts, Chart.js)
*   **组件:** 通用 UI 组件库, 图表组件, 查询编辑器组件, 数据表格组件

## 4. 技术栈

*   **后端:** Java 11+, Spring Boot 2.7+, MyBatis 3.5+, Maven 3.8+, Redis 6.0+, SLF4J, Logback, Lombok, MapStruct, Swagger
*   **数据库:** MySQL 8.0+, DB2
*   **前端:** HTML, Tailwind CSS, FontAwesome 6, Element UI/Ant Design, JavaScript, Vue.js/React (待定), ECharts/Chart.js
*   **部署:** Docker, Kubernetes

## 5. 数据模型

*   **表前缀:** `tbl_`
*   **主键:** UUID (varchar(36)), 可选顺序 ID 生成
*   **时间戳字段:** `created_at`, `modified_at` (datetime, 自动管理)
*   **用户跟踪字段:** `created_by`, `modified_by` (外键关联用户表)
*   **索引命名:** 唯一索引 `u_idx_列名`, 普通索引 `idx_列名`
*   **乐观锁:** `nonce` 字段 (冲突预防, 重试机制)

## 6. 开发指南

*   **架构:** 领域驱动设计 (DDD), 模块化, 六边形架构
*   **数据访问:** MyBatis (禁用 JPA/Hibernate)
*   **代码简化:** Lombok
*   **对象映射:** MapStruct
*   **文档:** 中文注释, Swagger API 文档, 系统架构图
*   **日志:** SLF4J, Logback (上下文信息, 日志轮换)
*   **测试:** JUnit 5, Mockito, 单元测试 (>= 80% 覆盖率), 集成测试, 性能测试
*   **代码标准:** 阿里巴巴 Java 编码规范, SonarQube 代码质量检查, 自动格式化

## 7. 部署方案

*   **本地部署:** Jar 包运行
*   **Docker 容器化:** 提供 Dockerfile 和 Docker Compose 文件
*   **Kubernetes 部署:** 提供 Kubernetes 配置文件 (YAML)

## 8. 性能指标

*   **查询超时:** 30 秒
*   **速率限制:** 每用户每分钟 10 次查询
*   **查询执行计划分析:** 提供查询执行计划查看功能
*   **数据导出:** CSV 格式 (<= 50,000 条记录), 后台任务处理更大导出, 可配置导出模板

## 9. 安全性

*   **密码加密:** AES-256 with salt
*   **访问控制:** 基于角色 (RBAC)
*   **审计日志:** 全面审计日志记录
*   **会话管理:** 安全会话管理

## 10. UI/UX 设计

*   **前端技术:** HTML, Tailwind CSS, FontAwesome 6, Element UI/Ant Design
*   **响应式设计:** 适配桌面和移动设备
*   **主题切换:** 明暗主题
*   **交互式查询构建表单**
*   **分页结果页面**
*   **动态图表可视化**
*   **智能功能:** 自动隐藏不常用条件, 最近使用, 收藏, 上下文帮助
*   **个性化设置:** 列顺序, 默认排序, 页面布局, 保存查询, 用户偏好设置

---
[2025-03-18 12:24:41] - Initial architecture design document created.