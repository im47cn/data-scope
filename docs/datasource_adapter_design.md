# 数据源适配器设计文档

## 1. 设计目标

数据源适配器模块是系统的核心组件之一，负责将不同类型的数据库系统统一到一个共同的接口后，供系统其他模块使用。设计目标包括：

1. **统一接口**：提供统一的接口，屏蔽不同数据库系统的差异
2. **可扩展性**：支持轻松添加新的数据库类型
3. **功能完整**：提供连接管理、查询执行、元数据提取等完整功能
4. **高性能**：优化连接管理和查询执行效率
5. **安全可靠**：提供安全的凭证管理和错误处理机制

## 2. 技术选择

- **设计模式**：适配器模式 + 工厂模式
- **连接池**：HikariCP
- **JDBC扩展**：使用各数据库驱动的特有功能
- **元数据API**：JDBC DatabaseMetaData + 特定数据库系统表查询
- **错误处理**：统一的异常层次结构

## 3. 核心接口设计

```java
/**
 * 数据源适配器接口
 * 定义所有数据库类型必须实现的统一操作
 */
public interface DataSourceAdapter {
    /**
     * 获取适配器类型
     */
    DataSourceType getType();
    
    /**
     * 测试连接
     */
    ConnectionTestResult testConnection(DataSourceConnectionInfo connectionInfo);
    
    /**
     * 获取连接
     */
    Connection getConnection(DataSourceConnectionInfo connectionInfo);
    
    /**
     * 释放连接
     */
    void releaseConnection(Connection connection);
    
    /**
     * 关闭连接池
     */
    void closeConnectionPool(Long dataSourceId);
    
    /**
     * 执行查询并返回结果集
     */
    QueryResult executeQuery(String sql, Map<String, Object> parameters, 
                            QueryOptions options, DataSourceConnectionInfo connectionInfo);
    
    /**
     * 执行更新操作并返回影响行数
     */
    int executeUpdate(String sql, Map<String, Object> parameters, 
                     DataSourceConnectionInfo connectionInfo);
    
    /**
     * 提取数据库元数据
     */
    MetadataResult extractMetadata(DataSourceConnectionInfo connectionInfo, 
                                  MetadataExtractionOptions options);
    
    /**
     * 获取数据库特定功能
     */
    <T> T getDatabaseSpecificFeature(Class<T> featureClass);
}
```

## 4. 适配器工厂设计

适配器工厂负责创建和管理数据源适配器实例。

```java
/**
 * 数据源适配器工厂接口
 */
public interface DataSourceAdapterFactory {
    /**
     * 根据数据源类型获取适配器
     */
    DataSourceAdapter getAdapter(DataSourceType type);
    
    /**
     * 注册适配器
     */
    void registerAdapter(DataSourceType type, DataSourceAdapter adapter);
    
    /**
     * 获取所有支持的适配器类型
     */
    List<DataSourceType> getSupportedTypes();
}

/**
 * 适配器工厂实现
 */
@Service
public class DefaultDataSourceAdapterFactory implements DataSourceAdapterFactory {
    private final Map<DataSourceType, DataSourceAdapter> adapters = new ConcurrentHashMap<>();
    
    @Autowired
    public DefaultDataSourceAdapterFactory(List<DataSourceAdapter> adapterList) {
        adapterList.forEach(adapter -> adapters.put(adapter.getType(), adapter));
    }
    
    @Override
    public DataSourceAdapter getAdapter(DataSourceType type) {
        DataSourceAdapter adapter = adapters.get(type);
        if (adapter == null) {
            throw new UnsupportedDataSourceTypeException("不支持的数据源类型: " + type);
        }
        return adapter;
    }
    
    @Override
    public void registerAdapter(DataSourceType type, DataSourceAdapter adapter) {
        adapters.put(type, adapter);
    }
    
    @Override
    public List<DataSourceType> getSupportedTypes() {
        return new ArrayList<>(adapters.keySet());
    }
}
```

## 5. MySQL适配器实现

MySQL适配器是第一阶段优先实现的适配器。

```java
/**
 * MySQL适配器实现
 */
@Component
public class MySQLAdapter implements DataSourceAdapter {
    private static final Logger log = LoggerFactory.getLogger(MySQLAdapter.class);
    
    private final Map<Long, HikariDataSource> dataSourcePools = new ConcurrentHashMap<>();
    
    @Override
    public DataSourceType getType() {
        return DataSourceType.MYSQL;
    }
    
    @Override
    public ConnectionTestResult testConnection(DataSourceConnectionInfo connectionInfo) {
        try (Connection conn = createRawConnection(connectionInfo)) {
            boolean valid = conn.isValid(5);
            if (!valid) {
                return ConnectionTestResult.failed("连接无效");
            }
            
            // 获取数据库版本信息
            DatabaseMetaData metaData = conn.getMetaData();
            String versionInfo = metaData.getDatabaseProductName() + " " + 
                               metaData.getDatabaseProductVersion();
                               
            return ConnectionTestResult.successful(versionInfo);
        } catch (SQLException e) {
            log.error("MySQL连接测试失败", e);
            return ConnectionTestResult.failed(e.getMessage());
        }
    }
    
    @Override
    public Connection getConnection(DataSourceConnectionInfo connectionInfo) {
        try {
            Long dataSourceId = connectionInfo.getDataSourceId();
            HikariDataSource hikariDataSource = dataSourcePools.computeIfAbsent(
                dataSourceId, k -> createHikariDataSource(connectionInfo));
                
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error("获取MySQL连接失败", e);
            throw new DataSourceConnectionException("获取MySQL连接失败", e);
        }
    }
    
    @Override
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.warn("关闭MySQL连接失败", e);
            }
        }
    }
    
    @Override
    public void closeConnectionPool(Long dataSourceId) {
        HikariDataSource dataSource = dataSourcePools.remove(dataSourceId);
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    @Override
    public QueryResult executeQuery(String sql, Map<String, Object> parameters, 
                                  QueryOptions options, DataSourceConnectionInfo connectionInfo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = getConnection(connectionInfo);
            
            // 设置查询超时
            if (options.getTimeout() > 0) {
                conn.setNetworkTimeout(Executors.newSingleThreadExecutor(), 
                                     options.getTimeout() * 1000);
            }
            
            stmt = conn.prepareStatement(sql);
            
            // 绑定参数
            if (parameters != null) {
                bindParameters(stmt, parameters);
            }
            
            // 设置最大行数
            if (options.getMaxRows() > 0) {
                stmt.setMaxRows(options.getMaxRows());
            }
            
            // 执行查询
            long startTime = System.currentTimeMillis();
            rs = stmt.executeQuery();
            long endTime = System.currentTimeMillis();
            
            // 处理结果集
            QueryResultBuilder resultBuilder = new QueryResultBuilder();
            resultBuilder.executionTime(endTime - startTime);
            
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            
            // 添加列信息
            for (int i = 1; i <= columnCount; i++) {
                resultBuilder.addColumn(
                    metaData.getColumnName(i),
                    metaData.getColumnTypeName(i),
                    metaData.getColumnDisplaySize(i)
                );
            }
            
            // 添加数据行
            int rowCount = 0;
            while (rs.next() && rowCount < options.getMaxRows()) {
                RowData rowData = new RowData();
                
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    rowData.put(columnName, value);
                }
                
                resultBuilder.addRow(rowData);
                rowCount++;
            }
            
            // 设置是否有更多数据
            resultBuilder.hasMoreData(rs.next());
            
            return resultBuilder.build();
        } catch (SQLException e) {
            log.error("执行MySQL查询失败", e);
            throw new QueryExecutionException("执行MySQL查询失败", e);
        } finally {
            closeResources(rs, stmt, conn);
        }
    }
    
    @Override
    public int executeUpdate(String sql, Map<String, Object> parameters, 
                           DataSourceConnectionInfo connectionInfo) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = getConnection(connectionInfo);
            stmt = conn.prepareStatement(sql);
            
            // 绑定参数
            if (parameters != null) {
                bindParameters(stmt, parameters);
            }
            
            // 执行更新
            return stmt.executeUpdate();
        } catch (SQLException e) {
            log.error("执行MySQL更新失败", e);
            throw new QueryExecutionException("执行MySQL更新失败", e);
        } finally {
            closeResources(null, stmt, conn);
        }
    }
    
    @Override
    public MetadataResult extractMetadata(DataSourceConnectionInfo connectionInfo, 
                                        MetadataExtractionOptions options) {
        Connection conn = null;
        try {
            conn = getConnection(connectionInfo);
            MetadataExtractor extractor = new MySQLMetadataExtractor(conn);
            
            // 根据options决定提取哪些元数据
            MetadataResult.Builder builder = MetadataResult.builder();
            
            // 提取模式信息
            if (options.isIncludeSchemas()) {
                List<SchemaInfo> schemas = extractor.extractSchemas(
                    options.getSchemaPattern());
                builder.schemas(schemas);
            }
            
            // 提取表信息
            if (options.isIncludeTables()) {
                List<TableInfo> tables = extractor.extractTables(
                    options.getSchemaPattern(),
                    options.getTablePattern(),
                    options.getTableTypes());
                builder.tables(tables);
            }
            
            // 提取列信息
            if (options.isIncludeColumns()) {
                List<ColumnInfo> columns = extractor.extractColumns(
                    options.getSchemaPattern(),
                    options.getTablePattern(),
                    options.getColumnPattern());
                builder.columns(columns);
            }
            
            // 提取索引信息
            if (options.isIncludeIndexes()) {
                List<IndexInfo> indexes = extractor.extractIndexes(
                    options.getSchemaPattern(),
                    options.getTablePattern());
                builder.indexes(indexes);
            }
            
            return builder.build();
        } catch (SQLException e) {
            log.error("提取MySQL元数据失败", e);
            throw new MetadataExtractionException("提取MySQL元数据失败", e);
        } finally {
            if (conn != null) {
                releaseConnection(conn);
            }
        }
    }
    
    @Override
    public <T> T getDatabaseSpecificFeature(Class<T> featureClass) {
        // MySQL特定功能实现
        if (featureClass == BatchInsertSupport.class) {
            return (T) new MySQLBatchInsertSupport();
        }
        
        return null;
    }
    
    // 私有辅助方法
    
    private Connection createRawConnection(DataSourceConnectionInfo connectionInfo) 
        throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", connectionInfo.getUsername());
        props.setProperty("password", connectionInfo.getDecryptedPassword());
        
        // 附加连接属性
        if (connectionInfo.getProperties() != null) {
            props.putAll(connectionInfo.getProperties());
        }
        
        String url = String.format("jdbc:mysql://%s:%d/%s",
            connectionInfo.getHost(),
            connectionInfo.getPort(),
            connectionInfo.getDatabase());
            
        return DriverManager.getConnection(url, props);
    }
    
    private HikariDataSource createHikariDataSource(DataSourceConnectionInfo connectionInfo) {
        HikariConfig config = new HikariConfig();
        
        String url = String.format("jdbc:mysql://%s:%d/%s",
            connectionInfo.getHost(),
            connectionInfo.getPort(),
            connectionInfo.getDatabase());
            
        config.setJdbcUrl(url);
        config.setUsername(connectionInfo.getUsername());
        config.setPassword(connectionInfo.getDecryptedPassword());
        
        // 连接池配置
        config.setPoolName("mysql-pool-" + connectionInfo.getDataSourceId());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(600000); // 10分钟
        config.setMaxLifetime(1800000); // 30分钟
        config.setConnectionTimeout(30000); // 30秒
        
        // 性能优化
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        
        // 附加连接属性
        if (connectionInfo.getProperties() != null) {
            connectionInfo.getProperties().forEach((key, value) -> {
                config.addDataSourceProperty(key.toString(), value.toString());
            });
        }
        
        return new HikariDataSource(config);
    }
    
    private void bindParameters(PreparedStatement stmt, Map<String, Object> parameters) 
        throws SQLException {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            // 支持命名参数，将命名参数转换为位置参数
            // 这里假设SQL中的命名参数格式为:name，需要解析出位置
            String paramName = entry.getKey();
            Object paramValue = entry.getValue();
            
            // 这里需要根据实际情况实现参数绑定逻辑
            // 简化示例，假设参数名称就是参数位置
            int position = Integer.parseInt(paramName);
            stmt.setObject(position, paramValue);
        }
    }
    
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.warn("关闭ResultSet失败", e);
            }
        }
        
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.warn("关闭Statement失败", e);
            }
        }
        
        if (conn != null) {
            releaseConnection(conn);
        }
    }
}
```

## 6. DB2适配器实现要点

DB2适配器将在MySQL适配器架构验证后实现，需要考虑以下特殊点：

1. **连接URL格式**：DB2的JDBC URL格式为`jdbc:db2://hostname:port/databaseName`

2. **连接池配置**：
   ```java
   config.addDataSourceProperty("currentSchema", connectionInfo.getSchema());
   config.addDataSourceProperty("retrieveMessagesFromServerOnGetMessage", "true");
   ```

3. **元数据查询**：
   - DB2的系统表结构不同于MySQL
   - 需要使用SYSIBM.SYSTABLES, SYSIBM.SYSCOLUMNS等系统视图
   - 需要处理DB2特有的模式概念

4. **分页查询**：
   ```sql
   -- DB2分页语法
   SELECT * FROM (
       SELECT ROW_NUMBER() OVER(ORDER BY id) AS rn, t.* 
       FROM table t
   ) WHERE rn BETWEEN ? AND ?
   ```

5. **数据类型映射**：
   - DB2特有的DECFLOAT数据类型
   - DB2的TIMESTAMP格式处理

6. **锁管理与事务**：
   - 需要优化隔离级别和锁定超时设置
   - 配置适当的死锁重试逻辑

## 7. 抽象通用功能

为了最大化代码复用，将创建一个抽象基类提取各数据库适配器的通用功能：

```java
/**
 * 数据源适配器抽象基类
 * 实现通用功能，特定数据库适配器只需实现差异部分
 */
public abstract class AbstractDataSourceAdapter implements DataSourceAdapter {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    protected final Map<Long, HikariDataSource> dataSourcePools = new ConcurrentHashMap<>();
    
    /**
     * 获取数据库特定的JDBC URL
     */
    protected abstract String buildJdbcUrl(DataSourceConnectionInfo connectionInfo);
    
    /**
     * 配置数据库特定的连接池属性
     */
    protected abstract void configureDataSourceProperties(HikariConfig config, 
                                                       DataSourceConnectionInfo connectionInfo);
    
    /**
     * 创建数据库特定的元数据提取器
     */
    protected abstract MetadataExtractor createMetadataExtractor(Connection connection);
    
    @Override
    public Connection getConnection(DataSourceConnectionInfo connectionInfo) {
        try {
            Long dataSourceId = connectionInfo.getDataSourceId();
            HikariDataSource hikariDataSource = dataSourcePools.computeIfAbsent(
                dataSourceId, k -> createHikariDataSource(connectionInfo));
                
            return hikariDataSource.getConnection();
        } catch (SQLException e) {
            log.error("获取数据库连接失败", e);
            throw new DataSourceConnectionException("获取数据库连接失败", e);
        }
    }
    
    @Override
    public void releaseConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                log.warn("关闭数据库连接失败", e);
            }
        }
    }
    
    @Override
    public void closeConnectionPool(Long dataSourceId) {
        HikariDataSource dataSource = dataSourcePools.remove(dataSourceId);
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
    
    // 其他通用实现...
    
    protected HikariDataSource createHikariDataSource(DataSourceConnectionInfo connectionInfo) {
        HikariConfig config = new HikariConfig();
        
        // 设置数据库URL
        config.setJdbcUrl(buildJdbcUrl(connectionInfo));
        config.setUsername(connectionInfo.getUsername());
        config.setPassword(connectionInfo.getDecryptedPassword());
        
        // 通用连接池配置
        config.setPoolName(getType().name().toLowerCase() + "-pool-" + connectionInfo.getDataSourceId());
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);
        
        // 数据库特定配置
        configureDataSourceProperties(config, connectionInfo);
        
        // 附加连接属性
        if (connectionInfo.getProperties() != null) {
            connectionInfo.getProperties().forEach((key, value) -> {
                config.addDataSourceProperty(key.toString(), value.toString());
            });
        }
        
        return new HikariDataSource(config);
    }
}
```

## 8. 并发与性能优化

1. **连接池监控**：
   - 配置HikariCP的指标收集
   - 通过JMX暴露监控指标
   - 实现动态调整连接池大小的机制

2. **查询优化**：
   - 使用PreparedStatement提高查询效率
   - 实现查询缓存机制
   - 支持批量查询操作

3. **资源管理**：
   - 实现闲置连接池自动回收
   - 监控长时间运行的查询
   - 实现查询超时和自动取消机制

## 9. 安全考虑

1. **凭证保护**：
   - 数据源密码加密存储
   - 内存中密码的安全处理

2. **SQL注入防护**：
   - 使用参数化查询
   - SQL语句验证

3. **访问控制**：
   - 支持数据库角色和权限
   - 支持只读连接模式

## 10. 扩展规划

未来版本计划支持的数据库类型：

1. PostgreSQL
2. Oracle
3. SQL Server
4. MongoDB (NoSQL)
5. Hive (大数据)

## 11. 测试策略

1. **单元测试**：
   - 使用H2内存数据库测试通用功能
   - 使用Mockito模拟JDBC连接

2. **集成测试**：
   - 使用TestContainers启动实际数据库容器
   - 测试完整的元数据提取和查询执行流程

3. **性能测试**：
   - 测试连接池在高负载下的性能
   - 测试大结果集处理能力
   - 测试并发查询性能