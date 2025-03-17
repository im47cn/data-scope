/**
 * SQL格式化服务
 * 提供SQL格式化、高亮和解析等功能
 */
const SqlFormatter = {
    /**
     * 格式化SQL语句
     * @param {string} sql 原始SQL
     * @param {Object} options 格式化选项
     * @returns {string} 格式化后的SQL
     */
    format(sql, options = {}) {
        const defaultOptions = {
            uppercase: true,    // 是否大写关键字
            indent: '    ',    // 缩进字符
            linesBetweenQueries: 2  // 查询之间的空行数
        };

        const config = { ...defaultOptions, ...options };
        
        if (!sql) {
            return '';
        }

        // 将SQL转换为大写（如果配置要求）
        let formattedSql = sql;
        if (config.uppercase) {
            formattedSql = this._uppercaseKeywords(formattedSql);
        }

        // 分解SQL语句
        formattedSql = this._breakLines(formattedSql);

        // 添加适当的缩进
        formattedSql = this._addIndentation(formattedSql, config.indent);

        // 处理多个查询之间的空行
        formattedSql = this._addQuerySpacing(formattedSql, config.linesBetweenQueries);

        return formattedSql;
    },

    /**
     * SQL关键字大写转换
     * @private
     * @param {string} sql SQL语句
     * @returns {string} 转换后的SQL
     */
    _uppercaseKeywords(sql) {
        const keywords = [
            'SELECT', 'FROM', 'WHERE', 'AND', 'OR', 'INNER JOIN', 'LEFT JOIN', 'RIGHT JOIN',
            'ORDER BY', 'GROUP BY', 'HAVING', 'LIMIT', 'OFFSET', 'INSERT', 'UPDATE', 'DELETE',
            'INTO', 'VALUES', 'SET', 'CREATE', 'ALTER', 'DROP', 'TABLE', 'INDEX', 'VIEW',
            'UNION', 'UNION ALL', 'INTERSECT', 'EXCEPT', 'AS', 'ON', 'IN', 'BETWEEN', 'LIKE',
            'IS NULL', 'IS NOT NULL', 'ASC', 'DESC', 'DISTINCT', 'CASE', 'WHEN', 'THEN', 'ELSE', 'END'
        ];

        let result = sql;
        keywords.forEach(keyword => {
            const regex = new RegExp('\\b' + keyword + '\\b', 'gi');
            result = result.replace(regex, keyword);
        });

        return result;
    },

    /**
     * 分解SQL语句到多行
     * @private
     * @param {string} sql SQL语句
     * @returns {string} 处理后的SQL
     */
    _breakLines(sql) {
        const breakPoints = [
            ' FROM ',
            ' WHERE ',
            ' AND ',
            ' OR ',
            ' INNER JOIN ',
            ' LEFT JOIN ',
            ' RIGHT JOIN ',
            ' ORDER BY ',
            ' GROUP BY ',
            ' HAVING ',
            ' UNION ',
            ' UNION ALL ',
            ' INTERSECT ',
            ' EXCEPT '
        ];

        let result = sql;
        breakPoints.forEach(point => {
            const regex = new RegExp(point, 'gi');
            result = result.replace(regex, '\n' + point.trim() + ' ');
        });

        return result;
    },

    /**
     * 添加适当的缩进
     * @private
     * @param {string} sql SQL语句
     * @param {string} indent 缩进字符
     * @returns {string} 处理后的SQL
     */
    _addIndentation(sql, indent) {
        const lines = sql.split('\n');
        let indentLevel = 0;

        return lines.map(line => {
            line = line.trim();
            
            // 减少缩进的情况
            if (line.match(/^(FROM|WHERE|ORDER BY|GROUP BY|HAVING)/i)) {
                indentLevel = 1;
            } else if (line.match(/^(AND|OR)/i)) {
                indentLevel = 2;
            } else if (line.match(/^(INNER JOIN|LEFT JOIN|RIGHT JOIN)/i)) {
                indentLevel = 2;
            }

            return indent.repeat(indentLevel) + line;
        }).join('\n');
    },

    /**
     * 处理多个查询之间的空行
     * @private
     * @param {string} sql SQL语句
     * @param {number} lineCount 空行数
     * @returns {string} 处理后的SQL
     */
    _addQuerySpacing(sql, lineCount) {
        return sql.replace(/;(\s*\n\s*)/g, ';\n' + '\n'.repeat(lineCount));
    },

    /**
     * 解析SQL参数
     * @param {string} sql SQL语句
     * @returns {Array} 参数列表
     */
    extractParameters(sql) {
        const params = [];
        const regex = /:(\w+)/g;
        let match;

        while ((match = regex.exec(sql)) !== null) {
            if (!params.includes(match[1])) {
                params.push(match[1]);
            }
        }

        return params;
    },

    /**
     * 替换SQL参数
     * @param {string} sql SQL语句
     * @param {Object} params 参数值
     * @returns {string} 替换后的SQL
     */
    replaceParameters(sql, params) {
        let result = sql;
        Object.entries(params).forEach(([key, value]) => {
            const regex = new RegExp(':' + key + '\\b', 'g');
            result = result.replace(regex, this._formatParamValue(value));
        });
        return result;
    },

    /**
     * 格式化参数值
     * @private
     * @param {*} value 参数值
     * @returns {string} 格式化后的值
     */
    _formatParamValue(value) {
        if (value === null) {
            return 'NULL';
        }
        if (typeof value === 'string') {
            return `'${value.replace(/'/g, "''")}'`;
        }
        if (value instanceof Date) {
            return `'${value.toISOString()}'`;
        }
        if (Array.isArray(value)) {
            return '(' + value.map(v => this._formatParamValue(v)).join(', ') + ')';
        }
        return value.toString();
    },

    /**
     * 语法高亮
     * @param {string} sql SQL语句
     * @returns {string} 带有高亮标记的HTML
     */
    highlight(sql) {
        // 替换HTML特殊字符
        sql = sql.replace(/&/g, '&amp;')
                 .replace(/</g, '&lt;')
                 .replace(/>/g, '&gt;');

        // 高亮关键字
        const keywords = {
            'statement': /\b(SELECT|INSERT|UPDATE|DELETE|CREATE|ALTER|DROP|TRUNCATE|GRANT|REVOKE)\b/gi,
            'clause': /\b(FROM|WHERE|GROUP BY|HAVING|ORDER BY|LIMIT|OFFSET|SET|VALUES|INTO)\b/gi,
            'join': /\b(INNER JOIN|LEFT JOIN|RIGHT JOIN|FULL JOIN|CROSS JOIN|ON)\b/gi,
            'operator': /\b(AND|OR|NOT|IN|BETWEEN|LIKE|IS NULL|IS NOT NULL|ANY|ALL|EXISTS)\b/gi,
            'function': /\b(COUNT|SUM|AVG|MIN|MAX|COALESCE|NULLIF|CASE|WHEN|THEN|ELSE|END)\b/gi
        };

        Object.entries(keywords).forEach(([type, regex]) => {
            sql = sql.replace(regex, match => `<span class="sql-${type}">${match}</span>`);
        });

        // 高亮字符串
        sql = sql.replace(/'([^']*?)'/g, '<span class="sql-string">\'$1\'</span>');

        // 高亮数字
        sql = sql.replace(/\b(\d+)\b/g, '<span class="sql-number">$1</span>');

        // 高亮参数
        sql = sql.replace(/:(\w+)/g, '<span class="sql-parameter">:$1</span>');

        // 高亮注释
        sql = sql.replace(/--.*$/gm, match => `<span class="sql-comment">${match}</span>`);
        sql = sql.replace(/\/\*[\s\S]*?\*\//g, match => `<span class="sql-comment">${match}</span>`);

        return sql;
    }
};

export default SqlFormatter;