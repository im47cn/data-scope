package com.insightdata.domain.adapter;

import java.lang.reflect.Field;
import java.util.Map;

import com.insightdata.domain.metadata.enums.DataSourceType;
import com.insightdata.domain.metadata.model.DataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据源适配器帮助类
 *
 * 由于IDE可能无法正确识别Lombok生成的getter/setter方法，
 * 这个辅助类通过反射访问字段提供必要的访问方法。
 */
@Slf4j
public class DataSourceAdapterHelper {

    /**
     * 获取数据源类型
     */
    public static DataSourceType getType(DataSource dataSource) {
        if (dataSource == null) return null;
        return (DataSourceType) getFieldValue(dataSource, "type");
    }
    
    /**
     * 获取数据源ID
     */
    public static String getId(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "id");
    }
    
    /**
     * 获取数据源名称
     */
    public static String getName(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "name");
    }
    
    /**
     * 获取数据源驱动类名
     */
    public static String getDriverClassName(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "driverClassName");
    }
    
    /**
     * 获取数据源JDBC URL
     */
    public static String getJdbcUrl(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "jdbcUrl");
    }
    
    /**
     * 获取数据源用户名
     */
    public static String getUsername(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "username");
    }
    
    /**
     * 获取数据源密码
     */
    public static String getPassword(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "password");
    }
    
    /**
     * 获取数据源加密密码
     */
    public static String getEncryptedPassword(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "encryptedPassword");
    }
    
    /**
     * 获取数据源连接属性
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getConnectionProperties(DataSource dataSource) {
        if (dataSource == null) return null;
        return (Map<String, String>) getFieldValue(dataSource, "connectionProperties");
    }
    
    /**
     * 获取数据源数据库名称
     */
    public static String getDatabaseName(DataSource dataSource) {
        if (dataSource == null) return null;
        return (String) getFieldValue(dataSource, "databaseName");
    }
    
    /**
     * 通过反射获取字段值
     */
    private static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            log.warn("Failed to get field " + fieldName + " from " + obj.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }
}