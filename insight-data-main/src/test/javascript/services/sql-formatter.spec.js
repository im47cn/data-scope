import SqlFormatter from '../../../../insight-data-main/src/main/resources/static/js/services/sql-formatter';

describe('SqlFormatter', () => {
    describe('format', () => {
        it('should format basic SELECT query', () => {
            const sql = 'select id,name,age from users where age > 18 order by name';
            const formatted = SqlFormatter.format(sql);
            
            expect(formatted).toContain('SELECT');
            expect(formatted).toContain('FROM');
            expect(formatted).toContain('WHERE');
            expect(formatted).toContain('ORDER BY');
            expect(formatted.split('\n').length).toBeGreaterThan(1);
        });

        it('should handle complex queries', () => {
            const sql = `select u.id,u.name,count(o.id) as order_count from users u 
                        left join orders o on u.id=o.user_id where u.status='active' 
                        group by u.id,u.name having count(o.id)>0 order by order_count desc limit 10`;
            
            const formatted = SqlFormatter.format(sql);
            
            expect(formatted).toContain('LEFT JOIN');
            expect(formatted).toContain('GROUP BY');
            expect(formatted).toContain('HAVING');
            expect(formatted).toContain('ORDER BY');
            expect(formatted).toContain('LIMIT');
        });

        it('should format subqueries properly', () => {
            const sql = 'select * from (select id from users) as u';
            const formatted = SqlFormatter.format(sql);
            
            expect(formatted).toContain('(\n');
            expect(formatted).toContain(')\n');
        });

        it('should respect maxLineLength option', () => {
            const sql = 'select very_long_column_name_1, very_long_column_name_2, very_long_column_name_3 from very_long_table_name';
            const formatted = SqlFormatter.format(sql, { maxLineLength: 40 });
            
            const lines = formatted.split('\n');
            expect(lines.every(line => line.length <= 40)).toBe(true);
        });
    });

    describe('analyze', () => {
        it('should detect query type correctly', () => {
            expect(SqlFormatter.determineQueryType('SELECT * FROM users')).toBe('SELECT');
            expect(SqlFormatter.determineQueryType('INSERT INTO users VALUES (1)')).toBe('INSERT');
            expect(SqlFormatter.determineQueryType('UPDATE users SET name = "test"')).toBe('UPDATE');
            expect(SqlFormatter.determineQueryType('DELETE FROM users')).toBe('DELETE');
        });

        it('should extract tables correctly', () => {
            const sql = 'SELECT * FROM users u JOIN orders o ON u.id = o.user_id';
            const tables = SqlFormatter.extractTables(sql);
            
            expect(tables).toHaveLength(2);
            expect(tables[0].name).toBe('users');
            expect(tables[0].alias).toBe('u');
            expect(tables[1].name).toBe('orders');
            expect(tables[1].alias).toBe('o');
        });

        it('should extract columns correctly', () => {
            const sql = 'SELECT id, name as user_name, age FROM users';
            const columns = SqlFormatter.extractColumns(sql);
            
            expect(columns).toHaveLength(3);
            expect(columns[1].expression).toBe('name');
            expect(columns[1].alias).toBe('user_name');
        });

        it('should extract conditions correctly', () => {
            const sql = 'SELECT * FROM users WHERE age > 18 AND status = "active"';
            const conditions = SqlFormatter.extractConditions(sql);
            
            expect(conditions).toHaveLength(2);
            expect(conditions[0]).toContain('age > 18');
            expect(conditions[1]).toContain('status = "active"');
        });

        it('should extract joins correctly', () => {
            const sql = `
                SELECT * FROM users u 
                LEFT JOIN orders o ON u.id = o.user_id 
                INNER JOIN products p ON o.product_id = p.id
            `;
            const joins = SqlFormatter.extractJoins(sql);
            
            expect(joins).toHaveLength(2);
            expect(joins[0].type).toBe('LEFT');
            expect(joins[1].type).toBe('INNER');
        });

        it('should extract order by correctly', () => {
            const sql = 'SELECT * FROM users ORDER BY name ASC, age DESC';
            const orderBy = SqlFormatter.extractOrderBy(sql);
            
            expect(orderBy).toHaveLength(2);
            expect(orderBy[0].column).toBe('name');
            expect(orderBy[0].direction).toBe('ASC');
            expect(orderBy[1].direction).toBe('DESC');
        });

        it('should extract group by correctly', () => {
            const sql = 'SELECT dept, COUNT(*) FROM employees GROUP BY dept';
            const groupBy = SqlFormatter.extractGroupBy(sql);
            
            expect(groupBy).toHaveLength(1);
            expect(groupBy[0]).toBe('dept');
        });

        it('should extract pagination correctly', () => {
            const sql = 'SELECT * FROM users LIMIT 10 OFFSET 20';
            const pagination = SqlFormatter.extractPagination(sql);
            
            expect(pagination.limit).toBe(10);
            expect(pagination.offset).toBe(20);
        });

        it('should extract parameters correctly', () => {
            const sql = 'SELECT * FROM users WHERE id = :userId AND age > ?';
            const parameters = SqlFormatter.extractParameters(sql);
            
            expect(parameters).toHaveLength(2);
            expect(parameters[0].type).toBe('named');
            expect(parameters[0].name).toBe('userId');
            expect(parameters[1].type).toBe('placeholder');
        });

        it('should extract subqueries correctly', () => {
            const sql = `
                SELECT * FROM (
                    SELECT id FROM users WHERE age > 18
                ) as active_users
            `;
            const subqueries = SqlFormatter.extractSubqueries(sql);
            
            expect(subqueries).toHaveLength(1);
            expect(subqueries[0]).toContain('SELECT id FROM users');
        });

        it('should calculate complexity correctly', () => {
            const analysis = {
                tables: [{ name: 'users' }, { name: 'orders' }],
                joins: [{ type: 'LEFT' }],
                conditions: ['age > 18'],
                groupBy: ['dept'],
                orderBy: [{ column: 'name' }],
                subqueries: []
            };

            const complexity = SqlFormatter.calculateComplexity(analysis);
            expect(complexity).toBeGreaterThan(0);
        });
    });

    describe('Helper functions', () => {
        it('should normalize whitespace', () => {
            const sql = 'SELECT   id,    name   FROM    users';
            const normalized = SqlFormatter.normalizeWhitespace(sql);
            
            expect(normalized).toBe('SELECT id, name FROM users');
        });

        it('should uppercase keywords', () => {
            const sql = 'select id from users where age > 18';
            const uppercased = SqlFormatter.uppercaseKeywords(sql);
            
            expect(uppercased).toContain('SELECT');
            expect(uppercased).toContain('FROM');
            expect(uppercased).toContain('WHERE');
        });

        it('should identify subquery starts correctly', () => {
            expect(SqlFormatter.isSubqueryStart('SELECT * FROM (SELECT', 14)).toBe(true);
            expect(SqlFormatter.isSubqueryStart('SELECT * FROM(COUNT', 14)).toBe(false);
        });

        it('should validate date values', () => {
            expect(SqlFormatter.isValidDate('2025-03-17')).toBe(true);
            expect(SqlFormatter.isValidDate('invalid-date')).toBe(false);
        });
    });

    describe('Error handling', () => {
        it('should handle empty input', () => {
            expect(SqlFormatter.format('')).toBe('');
            expect(SqlFormatter.format(null)).toBe('');
            expect(SqlFormatter.format(undefined)).toBe('');
        });

        it('should handle invalid SQL gracefully', () => {
            const analysis = SqlFormatter.analyze('INVALID SQL STATEMENT');
            expect(analysis.type).toBe('UNKNOWN');
            expect(analysis.tables).toHaveLength(0);
        });
    });
});