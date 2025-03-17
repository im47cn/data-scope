package com.infrastructure.adapter;

import com.common.enums.DataSourceType;
import com.domain.model.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 数据源适配器工厂
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataSourceAdapterFactory {
    
    private final DB2DataSourceAdapter db2DataSourceAdapter;
    private final MySQLDataSourceAdapter mysqlDataSourceAdapter;
    
    /**
     * 获取数据源适配器
     */
    public DataSourceAdapter getAdapter(DataSourceType type) {
        if (type == null) {
            throw new IllegalArgumentException("数据源类型不能为空");
        }
        
        log.debug("Getting adapter for data source type: {}", type);
        
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
     */
    public DataSourceAdapter getAdapter(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("数据源不能为空");
        }
        return getAdapter(dataSource.getType());
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