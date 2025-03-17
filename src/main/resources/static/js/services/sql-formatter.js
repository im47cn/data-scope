/**
 * SQL格式化服务
 * 提供SQL语句的格式化、关键字高亮和语法分析功能
 */
const SqlFormatter = {
    // SQL关键字定义
    KEYWORDS: {
        COMMANDS: [
            'SELECT', 'INSERT', 'UPDATE', 'DELETE', 'CREATE', 'DROP', 'ALTER', 'TRUNCATE',
            'MERGE', 'GRANT', 'REVOKE', 'COMMIT', 'ROLLBACK'
        ],
        CLAUSES: [
            'FROM', 'WHERE', 'GROUP BY', 'HAVING', 'ORDER BY', 'LIMIT', 'OFFSET',
            'JOIN', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN', 'FULL JOIN',
            'UNION', 'UNION ALL', 'INTERSECT', 'EXCEPT'
        ],
        OPERATORS: [
            'AND', 'OR', 'NOT', 'IN', 'EXISTS', 'BETWEEN', 'LIKE', 'IS NULL',
            'IS NOT NULL', 'ASC', 'DESC'
        ],
        FUNCTIONS: [
            'COUNT', 'SUM', 'AVG', 'MAX', 'MIN', 'COALESCE', 'NULLIF',
            'CASE', 'WHEN', 'THEN', 'ELSE', 'END',
            'CAST', 'CONVERT', 'SUBSTRING', 'CONCAT', 'TRIM',
            'DATE', 'DATETIME', 'YEAR', 'MONTH', 'DAY'
        ]
    },

    /**
     * 格式化SQL语句
     * @param {string} sql 原始SQL语句
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    format(sql, options = {}) {
        if (!sql) return '';

        const defaultOptions = {
            indent: '  ',            // 缩进字符
            uppercase: true,         // 关键字大写
            maxLineLength: 80,       // 最大行长度
            linesBetweenClauses: 1   // 子句之间的空行数
        };

        const config = { ...defaultOptions, ...options };
        let formatted = sql.trim();

        // 标准化空白字符
        formatted = this.normalizeWhitespace(formatted);

        // 处理关键字大小写
        if (config.uppercase) {
            formatted = this.uppercaseKeywords(formatted);
        }

        // 添加换行和缩进
        formatted = this.addLineBreaksAndIndentation(formatted, config);

        // 处理子查询格式
        formatted = this.formatSubqueries(formatted, config);

        // 对齐选择列
        formatted = this.alignSelectColumns(formatted, config);

        // 处理长行
        if (config.maxLineLength > 0) {
            formatted = this.wrapLongLines(formatted, config);
        }

        return formatted;
    },

    /**
     * 标准化空白字符
     * @param {string} sql SQL语句
     * @returns {string} 标准化后的SQL
     */
    normalizeWhitespace(sql) {
        return sql
            .replace(/\s+/g, ' ')           // 合并多个空白字符
            .replace(/\s*,\s*/g, ', ')      // 标准化逗号
            .replace(/\s*([=<>])\s*/g, ' $1 ') // 标准化操作符
            .replace(/\(\s+/g, '(')         // 删除左括号后的空格
            .replace(/\s+\)/g, ')')         // 删除右括号前的空格
            .trim();
    },

    /**
     * 将关键字转换为大写
     * @param {string} sql SQL语句
     * @returns {string} 转换后的SQL
     */
    uppercaseKeywords(sql) {
        const allKeywords = [
            ...this.KEYWORDS.COMMANDS,
            ...this.KEYWORDS.CLAUSES,
            ...this.KEYWORDS.OPERATORS,
            ...this.KEYWORDS.FUNCTIONS
        ];

        let result = sql;
        allKeywords.forEach(keyword => {
            const regex = new RegExp(`\\b${keyword}\\b`, 'gi');
            result = result.replace(regex, keyword.toUpperCase());
        });

        return result;
    },

    /**
     * 添加换行和缩进
     * @param {string} sql SQL语句
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    addLineBreaksAndIndentation(sql, options) {
        const clauses = this.KEYWORDS.CLAUSES;
        let lines = [];
        let currentLine = '';
        let indentLevel = 0;
        let tokens = sql.split(/\s+/);

        tokens.forEach((token, i) => {
            const isClause = clauses.includes(token.toUpperCase());
            const isFirstToken = i === 0;

            if (isClause && !isFirstToken) {
                lines.push(currentLine);
                currentLine = options.indent.repeat(indentLevel);
                if (options.linesBetweenClauses > 0) {
                    lines.push('');
                }
            }

            if (token === '(') {
                indentLevel++;
            } else if (token === ')') {
                indentLevel = Math.max(0, indentLevel - 1);
                if (currentLine.trim() !== '') {
                    lines.push(currentLine);
                    currentLine = options.indent.repeat(indentLevel);
                }
            }

            currentLine += (currentLine === '' ? '' : ' ') + token;
        });

        if (currentLine) {
            lines.push(currentLine);
        }

        return lines.join('\n');
    },

    /**
     * 格式化子查询
     * @param {string} sql SQL语句
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    formatSubqueries(sql, options) {
        let result = sql;
        let depth = 0;
        let inSubquery = false;
        let formatted = '';

        for (let i = 0; i < result.length; i++) {
            const char = result[i];
            
            if (char === '(') {
                depth++;
                if (this.isSubqueryStart(result, i)) {
                    inSubquery = true;
                    formatted += '(\n' + options.indent.repeat(depth);
                } else {
                    formatted += char;
                }
            } else if (char === ')') {
                depth = Math.max(0, depth - 1);
                if (inSubquery) {
                    formatted += '\n' + options.indent.repeat(depth) + ')';
                    inSubquery = false;
                } else {
                    formatted += char;
                }
            } else {
                formatted += char;
            }
        }

        return formatted;
    },

    /**
     * 判断是否是子查询的开始
     * @param {string} sql SQL语句
     * @param {number} position 当前位置
     * @returns {boolean} 是否是子查询
     */
    isSubqueryStart(sql, position) {
        const prevChar = sql[position - 1];
        if (!prevChar) return false;

        const nextChars = sql.substring(position + 1, position + 8).toUpperCase();
        return prevChar === ' ' && nextChars.trim().startsWith('SELECT');
    },

    /**
     * 对齐SELECT子句中的列
     * @param {string} sql SQL语句
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    alignSelectColumns(sql, options) {
        const lines = sql.split('\n');
        let inSelect = false;
        let selectLines = [];
        let maxLength = 0;
        let result = [];

        lines.forEach(line => {
            if (line.trim().toUpperCase().startsWith('SELECT')) {
                inSelect = true;
                selectLines = [];
                maxLength = 0;
            } else if (inSelect && line.trim().toUpperCase().startsWith('FROM')) {
                inSelect = false;
                // 对齐并添加选择列
                selectLines.forEach(selectLine => {
                    result.push(selectLine.padEnd(maxLength) + ',');
                });
                result[result.length - 1] = result[result.length - 1].slice(0, -1);
            }

            if (inSelect) {
                if (line.trim().startsWith('SELECT')) {
                    result.push(line);
                } else {
                    const trimmed = line.trim();
                    selectLines.push(options.indent + trimmed);
                    maxLength = Math.max(maxLength, trimmed.length + options.indent.length);
                }
            } else {
                result.push(line);
            }
        });

        return result.join('\n');
    },

    /**
     * 处理过长的行
     * @param {string} sql SQL语句
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    wrapLongLines(sql, options) {
        const lines = sql.split('\n');
        let result = [];

        lines.forEach(line => {
            if (line.length <= options.maxLineLength) {
                result.push(line);
                return;
            }

            let currentLine = '';
            const tokens = line.split(/\s+/);
            const indentation = line.match(/^\s*/)[0];

            tokens.forEach(token => {
                if (currentLine.length + token.length > options.maxLineLength) {
                    result.push(currentLine);
                    currentLine = indentation + options.indent + token;
                } else {
                    currentLine += (currentLine === '' ? '' : ' ') + token;
                }
            });

            if (currentLine) {
                result.push(currentLine);
            }
        });

        return result.join('\n');
    },

    /**
     * 解析SQL语句结构
     * @param {string} sql SQL语句
     * @returns {Object} SQL语句的结构信息
     */
    analyze(sql) {
        const analysis = {
            type: null,          // 查询类型
            tables: [],         // 涉及的表
            columns: [],        // 选择的列
            conditions: [],     // WHERE条件
            joins: [],          // 连接信息
            orderBy: [],        // 排序信息
            groupBy: [],        // 分组信息
            limit: null,        // LIMIT信息
            offset: null,       // OFFSET信息
            parameters: [],     // 参数信息
            subqueries: [],     // 子查询
            complexity: 0       // 查询复杂度
        };

        try {
            // 确定查询类型
            analysis.type = this.determineQueryType(sql);

            // 提取表名
            analysis.tables = this.extractTables(sql);

            // 提取列名
            analysis.columns = this.extractColumns(sql);

            // 提取条件
            analysis.conditions = this.extractConditions(sql);

            // 提取连接
            analysis.joins = this.extractJoins(sql);

            // 提取排序
            analysis.orderBy = this.extractOrderBy(sql);

            // 提取分组
            analysis.groupBy = this.extractGroupBy(sql);

            // 提取分页信息
            const pagination = this.extractPagination(sql);
            analysis.limit = pagination.limit;
            analysis.offset = pagination.offset;

            // 提取参数
            analysis.parameters = this.extractParameters(sql);

            // 提取子查询
            analysis.subqueries = this.extractSubqueries(sql);

            // 计算复杂度
            analysis.complexity = this.calculateComplexity(analysis);
        } catch (error) {
            console.error('SQL分析错误:', error);
            throw new Error('SQL语句分析失败');
        }

        return analysis;
    },

    /**
     * 确定查询类型
     * @param {string} sql SQL语句
     * @returns {string} 查询类型
     */
    determineQueryType(sql) {
        const firstWord = sql.trim().split(/\s+/)[0].toUpperCase();
        return this.KEYWORDS.COMMANDS.find(cmd => cmd === firstWord) || 'UNKNOWN';
    },

    /**
     * 提取表名
     * @param {string} sql SQL语句
     * @returns {Array} 表名列表
     */
    extractTables(sql) {
        const tables = [];
        const fromRegex = /FROM\s+([^WHERE|GROUP|ORDER|LIMIT|;]*)/i;
        const match = sql.match(fromRegex);

        if (match) {
            const tableString = match[1];
            const tableMatches = tableString.match(/([a-zA-Z_][a-zA-Z0-9_]*(\s+as\s+[a-zA-Z0-9_]+)?)/gi);
            
            if (tableMatches) {
                tableMatches.forEach(table => {
                    const parts = table.split(/\s+as\s+/i);
                    tables.push({
                        name: parts[0],
                        alias: parts[1] || null
                    });
                });
            }
        }

        return tables;
    },

    /**
     * 提取列名
     * @param {string} sql SQL语句
     * @returns {Array} 列名列表
     */
    extractColumns(sql) {
        const columns = [];
        const selectRegex = /SELECT\s+([^FROM]*)/i;
        const match = sql.match(selectRegex);

        if (match) {
            const columnString = match[1];
            const columnMatches = columnString.split(',');
            
            columnMatches.forEach(column => {
                const trimmed = column.trim();
                if (trimmed !== '*') {
                    const parts = trimmed.split(/\s+as\s+/i);
                    columns.push({
                        expression: parts[0],
                        alias: parts[1] || null
                    });
                }
            });
        }

        return columns;
    },

    /**
     * 提取条件
     * @param {string} sql SQL语句
     * @returns {Array} 条件列表
     */
    extractConditions(sql) {
        const conditions = [];
        const whereRegex = /WHERE\s+([^GROUP|ORDER|LIMIT|;]*)/i;
        const match = sql.match(whereRegex);

        if (match) {
            const conditionString = match[1];
            // 简单地按AND/OR拆分
            const conditionMatches = conditionString.split(/\s+(AND|OR)\s+/i);
            
            conditionMatches.forEach(condition => {
                conditions.push(condition.trim());
            });
        }

        return conditions;
    },

    /**
     * 提取连接信息
     * @param {string} sql SQL语句
     * @returns {Array} 连接信息列表
     */
    extractJoins(sql) {
        const joins = [];
        const joinRegex = /(\w+\s+)?JOIN\s+([^ON]*)\s+ON\s+([^WHERE|GROUP|ORDER|LIMIT|;]*)/gi;
        let match;

        while ((match = joinRegex.exec(sql)) !== null) {
            joins.push({
                type: (match[1] || '').trim() || 'INNER',
                table: match[2].trim(),
                condition: match[3].trim()
            });
        }

        return joins;
    },

    /**
     * 提取排序信息
     * @param {string} sql SQL语句
     * @returns {Array} 排序信息列表
     */
    extractOrderBy(sql) {
        const orderBy = [];
        const orderRegex = /ORDER\s+BY\s+([^LIMIT|;]*)/i;
        const match = sql.match(orderRegex);

        if (match) {
            const orderString = match[1];
            const orderMatches = orderString.split(',');
            
            orderMatches.forEach(order => {
                const parts = order.trim().split(/\s+/);
                orderBy.push({
                    column: parts[0],
                    direction: parts[1]?.toUpperCase() || 'ASC'
                });
            });
        }

        return orderBy;
    },

    /**
     * 提取分组信息
     * @param {string} sql SQL语句
     * @returns {Array} 分组信息列表
     */
    extractGroupBy(sql) {
        const groupBy = [];
        const groupRegex = /GROUP\s+BY\s+([^HAVING|ORDER|LIMIT|;]*)/i;
        const match = sql.match(groupRegex);

        if (match) {
            const groupString = match[1];
            const groupMatches = groupString.split(',');
            
            groupMatches.forEach(group => {
                groupBy.push(group.trim());
            });
        }

        return groupBy;
    },

    /**
     * 提取分页信息
     * @param {string} sql SQL语句
     * @returns {Object} 分页信息
     */
    extractPagination(sql) {
        const pagination = {
            limit: null,
            offset: null
        };

        const limitRegex = /LIMIT\s+(\d+)/i;
        const offsetRegex = /OFFSET\s+(\d+)/i;
        
        const limitMatch = sql.match(limitRegex);
        const offsetMatch = sql.match(offsetRegex);

        if (limitMatch) {
            pagination.limit = parseInt(limitMatch[1]);
        }

        if (offsetMatch) {
            pagination.offset = parseInt(offsetMatch[1]);
        }

        return pagination;
    },

    /**
     * 提取参数信息
     * @param {string} sql SQL语句
     * @returns {Array} 参数列表
     */
    extractParameters(sql) {
        const parameters = [];
        
        // 命名参数 :param
        const namedParams = sql.match(/:[a-zA-Z_][a-zA-Z0-9_]*/g) || [];
        namedParams.forEach(param => {
            parameters.push({
                name: param.substring(1),
                type: 'named'
            });
        });

        // 问号占位符 ?
        const placeholderParams = sql.match(/\?/g) || [];
        placeholderParams.forEach((_, index) => {
            parameters.push({
                name: `param${index + 1}`,
                type: 'placeholder'
            });
        });

        return parameters;
    },

    /**
     * 提取子查询
     * @param {string} sql SQL语句
     * @returns {Array} 子查询列表
     */
    extractSubqueries(sql) {
        const subqueries = [];
        let depth = 0;
        let currentSubquery = '';
        let inSubquery = false;

        for (let i = 0; i < sql.length; i++) {
            const char = sql[i];

            if (char === '(') {
                depth++;
                if (this.isSubqueryStart(sql, i)) {
                    inSubquery = true;
                    currentSubquery = '';
                }
            } else if (char === ')') {
                depth--;
                if (inSubquery && depth === 0) {
                    subqueries.push(currentSubquery.trim());
                    inSubquery = false;
                }
            }

            if (inSubquery) {
                currentSubquery += char;
            }
        }

        return subqueries;
    },

    /**
     * 计算查询复杂度
     * @param {Object} analysis 查询分析结果
     * @returns {number} 复杂度分数
     */
    calculateComplexity(analysis) {
        let score = 0;

        // 基础分数
        score += analysis.tables.length * 10;           // 每个表10分
        score += analysis.joins.length * 15;            // 每个连接15分
        score += analysis.conditions.length * 5;        // 每个条件5分
        score += analysis.groupBy.length * 10;          // 每个分组10分
        score += analysis.orderBy.length * 5;           // 每个排序5分
        score += analysis.subqueries.length * 20;       // 每个子查询20分

        // 特殊情况加分
        if (analysis.type !== 'SELECT') score += 10;    // 非SELECT语句加10分
        if (analysis.joins.some(j => j.type !== 'INNER')) score += 5;  // 外连接加5分
        if (analysis.groupBy.length > 0) score += 10;   // 有GROUP BY加10分

        return score;
    }
};

export default SqlFormatter;