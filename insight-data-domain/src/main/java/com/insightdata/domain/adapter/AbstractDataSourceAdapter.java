package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.domain.datasource.model.SchemaInfo;
import com.insightdata.domain.datasource.model.TableInfo;
import com.insightdata.domain.exception.DataSourceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 数据源适配器的抽象基类
 * 
 * 提供DataSourceAdapter和EnhancedDataSourceAdapter接口的默认实现，
 * 封装通用逻辑，便于具体适配器实现类复用代码。
 */
@Slf4j
public abstract class AbstractDataSourceAdapter implements EnhancedDataSourceAdapter {

    protected Connection connection;
    protected DataSource currentDataSource;
    
    @Override
    public void connect(DataSource config) throws Exception {
        try {
            // 加载驱动
            Class.forName(config.getDriverClassName());
            
            // 设置连接属性
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword()); // 使用普通密码，非加密
            
            // 添加自定义连接属性
            if (config.getConnectionProperties() != null) {
                config.getConnectionProperties().forEach(props::setProperty);
            }
            
            // 获取连接
            connection = DriverManager.getConnection(config.getJdbcUrl(), props);
            currentDataSource = config;
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Failed to connect to database: {}", config.getName(), e);
            throw new DataSourceException("Failed to connect to database: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void disconnect() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            connection = null;
        }
    }
    
    @Override
    public boolean testConnection(DataSource config) throws Exception {
        try (Connection conn = getTestConnection(config)) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            log.error("Failed to test connection to database: {}", config.getName(), e);
            throw e;
        }
    }
    
    @Override
    public Connection getConnection() {
        return connection;
    }
    
    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        List<SchemaInfo> schemas = new ArrayList<>();
        List<String> schemaNames = getSchemas(dataSource.getDatabaseName());
        
        for (String schemaName : schemaNames) {
            SchemaInfo schema = new SchemaInfo();
            schema.setName(schemaName);
            schema.setDataSourceId(dataSource.getId());
            schemas.add(schema);
        }
        
        return schemas;
    }
    
    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        SchemaInfo schema = new SchemaInfo();
        schema.setName(schemaName);
        schema.setDataSourceId(dataSource.getId());
        
        // 获取此schema的表信息
        List<TableInfo> tables = getTables(dataSource, schemaName);
        schema.setTables(tables);
        
        return schema;
    }
    
    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schema) throws Exception {
        if (currentDataSource == null || !dataSource.getId().equals(currentDataSource.getId())) {
            connect(dataSource);
        }
        
        return getTables(dataSource.getDatabaseName(), schema);
    }
    
    // 保护方法，供子类使用
    
    /**
     * 创建测试连接
     * 
     * @param dataSource 数据源配置
     * @return 数据库连接
     */
    protected Connection getTestConnection(DataSource dataSource) throws SQLException, ClassNotFoundException {
        // 加载驱动
        Class.forName(dataSource.getDriverClassName());
        
        // 设置连接属性
        Properties props = new Properties();
        props.setProperty("user", dataSource.getUsername());
        props.setProperty("password", dataSource.getPassword());
        
        // 添加自定义连接属性
        if (dataSource.getConnectionProperties() != null) {
            dataSource.getConnectionProperties().forEach(props::setProperty);
        }
        
        // 获取连接
        return DriverManager.getConnection(dataSource.getJdbcUrl(), props);
    }
    
    /**
     * 检查数据库连接是否有效
     */
    protected void checkConnection() throws DataSourceException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new DataSourceException("Database connection is not established or has been closed");
            }
        } catch (SQLException e) {
            throw new DataSourceException("Error checking database connection status", e);
        }
    }
}