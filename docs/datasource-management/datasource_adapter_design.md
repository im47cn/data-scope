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