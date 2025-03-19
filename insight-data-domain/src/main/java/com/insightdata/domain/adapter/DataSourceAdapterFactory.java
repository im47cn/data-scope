package com.insightdata.domain.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;

/**
 * 数据源适配器工厂
 */
@Component
public class DataSourceAdapterFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAdapterFactory.class);
    private final DB2DataSourceAdapter db2DataSourceAdapter;
    private final MySQLDataSourceAdapter mysqlDataSourceAdapter;
    
    @Autowired
    public DataSourceAdapterFactory(DB2DataSourceAdapter db2DataSourceAdapter,
            MySQLDataSourceAdapter mysqlDataSourceAdapter) {
        this.db2DataSourceAdapter = db2DataSourceAdapter;
        this.mysqlDataSourceAdapter = mysqlDataSourceAdapter;
    }
    
    /**
     * 获取数据源适配器
     * @param type 数据源类型
     * @return 基本数据源适配器
     */
    public DataSourceAdapter getAdapter(DataSourceType type) {
        if (type == null) {
            throw new IllegalArgumentException("数据源类型不能为空");
        }
        
        LOGGER.debug("Getting adapter for data source type: {}", type);
        
        switch (type) {
            case MYSQL:
                return mysqlDataSourceAdapter;
            case DB2:
                return db2DataSourceAdapter;
            case ORACLE:
                throw new UnsupportedOperationException("Oracle数据源暂不支持");
            case POSTGRESQL:
                throw new UnsupportedOperationException("PostgreSQL数据源暂不支持");
            case SQLSERVER:
                throw new UnsupportedOperationException("SQL Server数据源暂不支持");
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }
    
    /**
     * 获取数据源适配器
     * @param dataSource 数据源
     * @return 基本数据源适配器
     */
    public DataSourceAdapter getAdapter(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        return getAdapter(DataSourceAdapterHelper.getType(dataSource));
    }
    
    /**
     * 获取增强型数据源适配器
     * @param type 数据源类型
     * @return 增强型数据源适配器
     */
    public EnhancedDataSourceAdapter getEnhancedAdapter(DataSourceType type) {
        DataSourceAdapter adapter = getAdapter(type);
        if (adapter instanceof EnhancedDataSourceAdapter) {
            return (EnhancedDataSourceAdapter) adapter;
        }
        throw new UnsupportedOperationException("不支持的增强型数据源适配器: " + type);
    }
    
    /**
     * 获取增强型数据源适配器
     * @param dataSource 数据源
     * @return 增强型数据源适配器
     */
    public EnhancedDataSourceAdapter getEnhancedAdapter(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        return getEnhancedAdapter(DataSourceAdapterHelper.getType(dataSource));
    }
    
    /**
     * 是否支持指定类型的数据源
     */
    public boolean supportsType(DataSourceType type) {
        if (type == null) {
            return false;
        }
        
        switch (type) {
            case MYSQL:
            case DB2:
                return true;
            default:
                return false;
        }
    }
    
    /**
     * 获取支持的数据源类型列表
     */
    public DataSourceType[] getSupportedTypes() {
        return new DataSourceType[] {
            DataSourceType.MYSQL,
            DataSourceType.DB2
        };
    }
    
    /**
     * 获取指定类型的默认端口号
     */
    public int getDefaultPort(DataSourceType type) {
        if (type == null) {
            throw new IllegalArgumentException("数据源类型不能为空");
        }
        
        switch (type) {
            case MYSQL:
                return 3306;
            case DB2:
                return 50000;
            case ORACLE:
                return 1521;
            case POSTGRESQL:
                return 5432;
            case SQLSERVER:
                return 1433;
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }
    
    /**
     * 获取指定类型的默认数据库名
     */
    public String getDefaultDatabaseName(DataSourceType type) {
        if (type == null) {
            throw new IllegalArgumentException("数据源类型不能为空");
        }
        
        switch (type) {
            case MYSQL:
                return "mysql";
            case DB2:
                return "sample";
            case ORACLE:
                return "orcl";
            case POSTGRESQL:
                return "postgres";
            case SQLSERVER:
                return "master";
            default:
                throw new IllegalArgumentException("不支持的数据源类型: " + type);
        }
    }
}