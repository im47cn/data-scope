# InsightData - 自然语言查询模块

## 项目概述

InsightData是一个全面的数据管理和查询系统，允许用户将不同的数据库系统无缝集成为数据源，并通过自然语言查询和SQL查询检索数据。

本项目是InsightData的自然语言查询模块，负责将自然语言查询转换为SQL查询，并执行查询返回结果。

## 项目结构

```
src/
├── main/
│   └── java/
│       └── com/
│           └── insightdata/
│               └── nlquery/
│                   ├── preprocess/            # 文本预处理
│                   │   ├── normalizer/        # 文本标准化
│                   │   └── tokenizer/         # 分词
│                   ├── intent/                # 意图识别
│                   ├── entity/                # 实体提取
│                   ├── relation/              # 关系分析
│                   ├── generator/             # SQL生成
│                   └── executor/              # 查询执行
└── test/
    └── java/
        └── com/
            └── insightdata/
                └── nlquery/
                    └── preprocess/            # 文本预处理测试
```

## 模块说明

### 文本预处理模块

文本预处理模块是自然语言查询模块的第一个核心组件，负责对输入的自然语言查询进行预处理，为后续的意图识别和实体提取提供基础。

主要功能包括：

1. **文本标准化**：大小写转换、标点处理、空白字符处理、特殊字符处理
2. **分词**：将文本分割为单词或词组，支持中文分词
3. **词性标注**：识别单词的词性（名词、动词、形容词等）
4. **停用词过滤**：过滤常见的停用词
5. **拼写检查和纠正**：检测拼写错误，提供纠正建议

### 意图识别模块（待实现）

意图识别模块负责识别用户查询的意图，如查询类型（SELECT、INSERT、UPDATE、DELETE）和查询目的（获取数据、统计数据、过滤数据等）。

### 实体提取模块（待实现）

实体提取模块负责从用户查询中提取关键实体，如表名、列名、条件值等。

### 关系分析模块（待实现）

关系分析模块负责分析实体之间的关系，如表之间的关系、条件之间的关系等。

### SQL生成模块（待实现）

SQL生成模块负责根据意图、实体和关系生成SQL查询语句。

### 查询执行模块（待实现）

查询执行模块负责执行SQL查询语句，并返回查询结果。

## 使用方法

### 编译和运行

```bash
# 编译和运行测试
./build.sh
```

### 示例代码

```java
// 创建文本预处理器
TextPreprocessor preprocessor = new DefaultTextPreprocessor();

// 预处理文本
String text = "查询所有用户的姓名和邮箱";
PreprocessedText result = preprocessor.preprocess(text);

// 输出结果
System.out.println("原始文本: " + result.getOriginalText());
System.out.println("标准化文本: " + result.getNormalizedText());
System.out.println("分词结果: " + result.getTokens());
System.out.println("语言: " + result.getLanguage());
```

## 开发计划

1. **文本预处理模块**（当前阶段）
    - [x] 文本标准化
    - [x] 分词
    - [ ] 词性标注
    - [ ] 停用词过滤
    - [ ] 拼写检查和纠正

2. **意图识别模块**
    - [ ] 意图模型设计
    - [ ] 规则基础意图识别
    - [ ] 机器学习意图分类

3. **实体提取模块**
    - [ ] 实体模型设计
    - [ ] 规则基础实体提取
    - [ ] 命名实体识别

4. **关系分析模块**
    - [ ] 关系模型设计
    - [ ] 表关系学习
    - [ ] 查询关系分析

5. **SQL生成模块**
    - [ ] SQL模板设计
    - [ ] 基础SQL生成
    - [ ] SQL优化

6. **查询执行模块**
    - [ ] 查询执行器设计
    - [ ] 多数据源支持
    - [ ] 结果处理

## 贡献者

- InsightData团队
