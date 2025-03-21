package com.insightdata.domain.adapter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public void connect(DataSource config) throws DataSourceException {
        try {
            // Use existing JDBC URL if provided, otherwise build it
            String jdbcUrl = config.getJdbcUrl();
            if (jdbcUrl == null || jdbcUrl.isEmpty()) {
                jdbcUrl = String.format("jdbc:%s://%s:%d/%s",
                    config.getType().getProtocol(),
                    config.getHost(),
                    config.getPort(),
                    config.getDatabaseName());
            }
            
            // Load driver if class name is provided
            String driverClassName = config.getDriverClassName();
            if (driverClassName != null && !driverClassName.isEmpty()) {
                Class.forName(driverClassName);
            }
            
            // Set connection properties
            Properties props = new Properties();
            props.setProperty("user", config.getUsername());
            props.setProperty("password", config.getPassword());
            
            // Add custom connection properties
            Map<String, String> connProps = config.getConnectionProperties();
            if (connProps != null) {
                connProps.forEach(props::setProperty);
            }
            
            // Get connection
            connection = DriverManager.getConnection(jdbcUrl, props);
            currentDataSource = config;
            
            log.info("Successfully connected to database: {}", config.getName());
        } catch (SQLException e) {
            String errorMsg = String.format("Failed to connect to database %s: %s",
                config.getName(), e.getMessage());
            log.error(errorMsg, e);
            throw new DataSourceException(errorMsg, e);
        } catch (ClassNotFoundException e) {
            String errorMsg = String.format("Database driver %s not found: %s",
                config.getDriverClassName(), e.getMessage());
            log.error(errorMsg, e);
            throw new DataSourceException(errorMsg, e);
        }
    }
    
    @Override
    public void disconnect() throws DataSourceException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            throw new DataSourceException("Error closing database connection", e);
        }
    }
    
    @Override
    public boolean testConnection(DataSource config) throws DataSourceException {
        try (Connection testConn = getTestConnection(config)) {
            boolean isValid = testConn != null && !testConn.isClosed();
            if (isValid) {
                log.info("Successfully tested connection to database: {}", config.getName());
            } else {
                log.warn("Connection test failed for database: {}", config.getName());
            }
            return isValid;
        } catch (SQLException | ClassNotFoundException e) {
            String errorMsg = String.format("Failed to test connection to database %s: %s",
                config.getName(), e.getMessage());
            log.error(errorMsg, e);
            throw new DataSourceException(errorMsg, e);
        }
    }
    
    @Override
    public Connection getConnection() throws DataSourceException {
        try {
            if (connection == null || connection.isClosed()) {
                throw new DataSourceException("No active connection available");
            }
            return connection;
        } catch (SQLException e) {
            throw new DataSourceException("Error checking connection status", e);
        }
    }
    
    @Override
    public List<SchemaInfo> getSchemas(DataSource dataSource) throws DataSourceException {
        ensureConnection(dataSource);
        
        List<SchemaInfo> schemas = new ArrayList<>();
        try {
            List<String> schemaNames = getSchemas(dataSource.getDatabaseName());
            
            for (String schemaName : schemaNames) {
                SchemaInfo schema = SchemaInfo.builder()
                    .name(schemaName)
                    .dataSourceId(dataSource.getId())
                    .build();
                schemas.add(schema);
            }
            
            return schemas;
        } catch (Exception e) {
            throw new DataSourceException("Failed to get schemas: " + e.getMessage(), e);
        }
    }
    
    @Override
    public SchemaInfo getSchema(DataSource dataSource, String schemaName) throws DataSourceException {
        ensureConnection(dataSource);
        
        try {
            List<TableInfo> tables = getTables(dataSource.getDatabaseName(), schemaName);
            
            return SchemaInfo.builder()
                .name(schemaName)
                .dataSourceId(dataSource.getId())
                .tables(tables)
                .build();
        } catch (Exception e) {
            throw new DataSourceException("Failed to get schema: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<TableInfo> getTables(DataSource dataSource, String schema) throws DataSourceException {
        ensureConnection(dataSource);
        return getTables(dataSource.getDatabaseName(), schema);
    }
    
    protected void ensureConnection(DataSource dataSource) throws DataSourceException {
        if (currentDataSource == null ||
            !dataSource.getId().equals(currentDataSource.getId()) ||
            connection == null) {
            connect(dataSource);
        }
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