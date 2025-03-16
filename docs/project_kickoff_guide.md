# 数据管理与查询系统 - 项目启动指南

本文档提供项目启动所需的必要信息，包括环境搭建、开发规范、团队协作工具等。

## 开发环境配置

### 所需软件和工具

| 软件/工具 | 版本 | 用途 |
|----------|------|------|
| JDK | 17+ | Java开发环境 |
| Maven | 3.8+ | 项目构建工具 |
| MySQL | 8.0+ | 系统数据库 |
| Redis | 6.0+ | 缓存服务 |
| Git | 最新版 | 版本控制 |
| IntelliJ IDEA | 最新版 | 后端IDE(推荐) |
| VS Code | 最新版 | 前端IDE(推荐) |
| Node.js | 16+ | 前端开发环境 |
| Docker | 最新版 | 容器化服务 |
| Postman | 最新版 | API测试工具 |

### 本地开发环境搭建步骤

1. **安装JDK 17**
   ```bash
   # 使用SDKMAN安装(推荐)
   curl -s "https://get.sdkman.io" | bash
   source "$HOME/.sdkman/bin/sdkman-init.sh"
   sdk install java 17.0.4.1-tem
   ```

2. **安装Maven**
   ```bash
   # 使用SDKMAN安装
   sdk install maven
   ```

3. **安装Docker和Docker Compose**
   ```bash
   # 根据操作系统不同，安装方式有所不同
   # 例如，在Ubuntu上：
   sudo apt-get update
   sudo apt-get install docker-ce docker-ce-cli containerd.io docker-compose-plugin
   ```

4. **启动开发环境依赖服务**
   ```bash
   # 进入项目根目录
   cd insight-data
   
   # 启动依赖服务
   docker-compose -f docker/docker-compose-dev.yml up -d
   ```

5. **克隆项目仓库**
   ```bash
   git clone [项目仓库URL]
   cd insight-data
   ```

6. **构建后端项目**
   ```bash
   mvn clean install
   ```

7. **设置前端开发环境**
   ```bash
   cd frontend
   npm install
   ```

## 开发规范

### 代码风格

1. **Java代码规范**
   - 遵循Google Java Style Guide
   - 使用空格而非tab进行缩进(4个空格)
   - 类名使用UpperCamelCase
   - 方法名和变量名使用lowerCamelCase
   - 常量使用UPPER_SNAKE_CASE
   - 包名使用全小写

2. **前端代码规范**
   - 遵循Airbnb JavaScript Style Guide
   - 使用ESLint进行代码检查
   - 使用Prettier进行代码格式化
   - Vue组件使用PascalCase命名
   - CSS类名使用kebab-case

### 提交规范

使用约定式提交(Conventional Commits)规范：

```
<type>(<scope>): <subject>

<body>

<footer>
```

常用type:
- feat: 新功能
- fix: 修复bug
- docs: 文档变更
- style: 代码风格调整
- refactor: 重构代码
- test: 添加测试
- chore: 构建过程或辅助工具变动

例如:
```
feat(datasource): 添加数据源连接测试功能

添加了一个新的API端点用于测试数据源连接。
这个功能将帮助用户在创建数据源前验证连接信息。

Closes #123
```

### 分支管理策略

采用GitHub Flow工作流:

1. `main`分支始终保持可部署状态
2. 从`main`分支创建功能分支进行开发
3. 功能开发完成后，创建Pull Request
4. 代码评审通过后，合并到`main`分支
5. 合并后立即部署

分支命名规范:
- 功能分支: `feature/[功能描述]`
- 缺陷修复: `fix/[缺陷描述]`
- 文档更新: `docs/[文档描述]`

## 项目结构说明

### 后端项目结构

```
insight-data/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── insightdata/
│   │   │           ├── domain/          # 领域层
│   │   │           │   ├── datasource/  # 数据源领域模型
│   │   │           │   ├── metadata/    # 元数据领域模型
│   │   │           │   ├── query/       # 查询领域模型
│   │   │           │   └── shared/      # 共享领域模型
│   │   │           ├── application/     # 应用层
│   │   │           │   ├── service/     # 应用服务
│   │   │           │   └── dto/         # 数据传输对象
│   │   │           ├── infrastructure/  # 基础设施层
│   │   │           │   ├── repository/  # 仓储实现
│   │   │           │   ├── adapter/     # 适配器实现
│   │   │           │   ├── config/      # 配置类
│   │   │           │   └── security/    # 安全相关
│   │   │           └── facade/          # 表现层
│   │   │               ├── rest/        # REST API
│   │   │               ├── websocket/   # WebSocket
│   │   │               └── exception/   # 异常处理
│   │   └── resources/
│   │       ├── application.yml          # 应用配置
│   │       ├── application-dev.yml      # 开发环境配置
│   │       └── db/
│   │           └── migration/           # 数据库迁移脚本
│   └── test/                            # 测试代码
├── docker/                              # Docker配置
├── docs/                                # 项目文档
├── frontend/                            # 前端项目
└── pom.xml                              # Maven配置
```

### 前端项目结构

```
frontend/
├── public/                  # 静态资源
├── src/
│   ├── assets/              # 资源文件
│   ├── components/          # 通用组件
│   │   ├── common/          # 基础组件
│   │   ├── datasource/      # 数据源相关组件
│   │   ├── query/           # 查询相关组件
│   │   └── visualization/   # 可视化组件
│   ├── views/               # 页面视图
│   ├── router/              # 路由配置
│   ├── store/               # 状态管理
│   ├── api/                 # API调用
│   ├── utils/               # 工具函数
│   ├── styles/              # 全局样式
│   ├── App.vue              # 根组件
│   └── main.js              # 入口文件
├── .eslintrc.js             # ESLint配置
├── .prettierrc              # Prettier配置
├── package.json             # 依赖配置
└── vue.config.js            # Vue配置
```

## 团队协作工具

### 项目管理

- **JIRA**: [JIRA项目URL]
  - 用于任务管理、缺陷跟踪、敏捷板
  - 每个Sprint在JIRA中创建和管理

### 文档管理

- **Confluence**: [Confluence空间URL]
  - 用于存储和共享项目文档
  - 包括产品需求、架构设计、会议记录等

### 代码仓库

- **GitHub/GitLab**: [仓库URL]
  - 用于代码版本控制
  - Pull Request/Merge Request流程

### 沟通工具

- **Slack**: [Slack频道URL]
  - 项目团队主要沟通工具
  - 设置专门的频道用于不同主题的讨论

### CI/CD

- **Jenkins/GitHub Actions**: [CI/CD平台URL]
  - 自动化构建、测试和部署
  - 从提交到部署的自动化流程

## 项目启动会议议程

1. 项目背景和目标介绍
2. 产品需求概述
3. 技术架构讨论
4. 团队成员介绍和职责划分
5. 开发流程和规范说明
6. 项目时间线和里程碑
7. 风险识别和应对策略
8. 问答环节

## 第一周关键任务

1. 完成开发环境搭建
2. 确认技术栈和架构决策
3. 完成项目骨架搭建
4. 实现基础组件和公共模块
5. 开始数据源管理核心功能开发

## 常见问题(FAQ)

### 1. 如何设置本地开发环境?
参考上述"本地开发环境搭建步骤"部分。

### 2. 如何获取项目访问权限?
联系项目经理或系统管理员获取相关仓库、JIRA、Confluence的访问权限。

### 3. 我需要熟悉哪些技术?
- 后端: Java, Spring Boot, MySQL, Redis, DDD
- 前端: Vue.js, Tailwind CSS, ECharts
- 通用: Git, Docker, RESTful API设计

### 4. 遇到技术问题怎么办?
- 首先查询项目文档和知识库
- 在Slack相关频道提问
- 与团队技术负责人或架构师讨论

### 5. 如何提交代码?
- 遵循分支管理策略创建功能分支
- 完成功能开发和单元测试
- 提交Pull Request并请求代码评审
- 评审通过后合并到主分支

## 联系人

| 角色 | 姓名 | 联系方式 |
|-----|-----|---------|
| 项目经理 | [姓名] | [邮箱/手机] |
| 架构师 | [姓名] | [邮箱/手机] |
| 后端负责人 | [姓名] | [邮箱/手机] |
| 前端负责人 | [姓名] | [邮箱/手机] |
| 测试负责人 | [姓名] | [邮箱/手机] |