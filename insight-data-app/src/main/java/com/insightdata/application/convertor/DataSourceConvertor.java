package com.insightdata.application.convertor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.insightdata.domain.datasource.model.DataSource;
import com.insightdata.facade.metadata.DataSourceDTO;

/**
 * 数据源对象映射器
 * 
 * 注意：由于领域模型和DTO之间存在字段命名不一致的情况，此转换器使用手动字段映射
 * - DataSource.enabled 对应 DataSourceDTO.active
 * - DataSourceType 在两个包中有不同的实现
 * 
 * 此类使用了Lombok生成的getter和setter方法，请确保IDE中安装了Lombok插件
 * 参考文档: docs/datasource-management/lombok-integration-guide.md
 */
@Component
public class DataSourceConvertor {

    /**
     * 将DTO转换为领域模型
     * @param dto 数据源DTO
     * @return 数据源领域模型
     */
    public DataSource toEntity(DataSourceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // 注意：这里使用了Lombok生成的方法，IDE可能会显示错误，但编译时会正常
        DataSource entity = new DataSource();
        
        // 手动复制属性
        try {
            // 反射方式获取和设置属性，避免IDE报错
            copyProperty(dto, entity, "id");
            copyProperty(dto, entity, "name");
            copyProperty(dto, entity, "host");
            copyProperty(dto, entity, "port");
            copyProperty(dto, entity, "databaseName");
            copyProperty(dto, entity, "username");
            copyProperty(dto, entity, "password");
            copyProperty(dto, entity, "connectionProperties");
            copyProperty(dto, entity, "description");
            copyProperty(dto, entity, "lastSyncTime");
            copyProperty(dto, entity, "createdAt");
            copyProperty(dto, entity, "updatedAt");
            
            // 启用状态转换 (active -> enabled)
            copyProperty(dto, entity, "active", "enabled");
            
            // 类型转换处理
            Object typeValue = getProperty(dto, "type");
            if (typeValue != null) {
                try {
                    DataSourceType type = DataSourceType.valueOf(typeValue.toString());
                    setProperty(entity, "type", type);
                } catch (Exception e) {
                    System.err.println("Error converting type: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error converting DTO to entity: " + e.getMessage());
        }
        
        return entity;
    }
    
    /**
     * 将领域模型转换为DTO
     * @param entity 数据源领域模型
     * @return 数据源DTO
     */
    public DataSourceDTO toDTO(DataSource entity) {
        if (entity == null) {
            return null;
        }
        
        // 注意：这里使用了Lombok生成的方法，IDE可能会显示错误，但编译时会正常
        DataSourceDTO dto = new DataSourceDTO();
        
        // 手动复制属性
        try {
            // 反射方式获取和设置属性，避免IDE报错
            copyProperty(entity, dto, "id");
            copyProperty(entity, dto, "name");
            copyProperty(entity, dto, "host");
            copyProperty(entity, dto, "port");
            copyProperty(entity, dto, "databaseName");
            copyProperty(entity, dto, "username");
            copyProperty(entity, dto, "connectionProperties");
            copyProperty(entity, dto, "description");
            copyProperty(entity, dto, "lastSyncTime");
            copyProperty(entity, dto, "createdAt");
            copyProperty(entity, dto, "updatedAt");
            
            // 启用状态转换 (enabled -> active)
            copyProperty(entity, dto, "enabled", "active");
            
            // 类型转换处理
            Object typeValue = getProperty(entity, "type");
            if (typeValue != null) {
                try {
                    com.insightdata.facade.metadata.enums.DataSourceType type = 
                        com.insightdata.facade.metadata.enums.DataSourceType.valueOf(typeValue.toString());
                    setProperty(dto, "type", type);
                } catch (Exception e) {
                    System.err.println("Error converting type: " + e.getMessage());
                }
            }
            
            // 密码字段不传输
            setProperty(dto, "password", null);
            
            // 标签默认为空数组
            setProperty(dto, "tags", new String[0]);
        } catch (Exception e) {
            System.err.println("Error converting entity to DTO: " + e.getMessage());
        }
        
        return dto;
    }
    
    /**
     * 将领域模型转换为列表DTO
     * @param entity 数据源领域模型
     * @return 数据源列表DTO
     */
    public DataSourceDTO toListDTO(DataSource entity) {
        return toDTO(entity);
    }
    
    /**
     * 将领域模型列表转换为DTO列表
     * @param entities 数据源领域模型列表
     * @return 数据源DTO列表
     */
    public List<DataSourceDTO> toDTOList(List<DataSource> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 将领域模型列表转换为列表DTO列表
     * @param entities 数据源领域模型列表
     * @return 数据源列表DTO列表
     */
    public List<DataSourceDTO> toListDTOList(List<DataSource> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        return entities.stream()
                .map(this::toListDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新领域模型
     * @param entity 待更新的数据源领域模型
     * @param dto 包含更新数据的DTO
     * @return 更新后的数据源领域模型
     */
    public DataSource updateEntity(DataSource entity, DataSourceDTO dto) {
        if (entity == null || dto == null) {
            return entity;
        }
        
        try {
            // 只复制非null字段
            updatePropertyIfNotNull(dto, entity, "name");
            updatePropertyIfNotNull(dto, entity, "host");
            updatePropertyIfNotNull(dto, entity, "port");
            updatePropertyIfNotNull(dto, entity, "databaseName");
            updatePropertyIfNotNull(dto, entity, "username");
            updatePropertyIfNotNull(dto, entity, "password");
            updatePropertyIfNotNull(dto, entity, "connectionProperties");
            updatePropertyIfNotNull(dto, entity, "description");
            
            // 特殊字段处理
            updatePropertyIfNotNull(dto, entity, "active", "enabled");
            
            // 类型转换处理
            Object typeValue = getProperty(dto, "type");
            if (typeValue != null) {
                try {
                    DataSourceType type = DataSourceType.valueOf(typeValue.toString());
                    setProperty(entity, "type", type);
                } catch (Exception e) {
                    System.err.println("Error converting type: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating entity: " + e.getMessage());
        }
        
        return entity;
    }
    
    // 通过反射获取属性值
    private Object getProperty(Object obj, String propertyName) {
        try {
            String getterName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            return obj.getClass().getMethod(getterName).invoke(obj);
        } catch (Exception e) {
            // 尝试boolean类型的getter
            try {
                String getterName = "is" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                return obj.getClass().getMethod(getterName).invoke(obj);
            } catch (Exception ex) {
                return null;
            }
        }
    }
    
    // 通过反射设置属性值
    private void setProperty(Object obj, String propertyName, Object value) {
        try {
            String setterName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            obj.getClass().getMethod(setterName, value.getClass()).invoke(obj, value);
        } catch (Exception e) {
            // 忽略异常
        }
    }
    
    // 从源对象复制属性到目标对象
    private void copyProperty(Object source, Object target, String propertyName) {
        copyProperty(source, target, propertyName, propertyName);
    }
    
    // 从源对象复制属性到目标对象，可以指定不同的属性名
    private void copyProperty(Object source, Object target, String sourceProperty, String targetProperty) {
        Object value = getProperty(source, sourceProperty);
        if (value != null) {
            setProperty(target, targetProperty, value);
        }
    }
    
    // 如果源对象的属性不为null，则更新目标对象的属性
    private void updatePropertyIfNotNull(Object source, Object target, String propertyName) {
        updatePropertyIfNotNull(source, target, propertyName, propertyName);
    }
    
    // 如果源对象的属性不为null，则更新目标对象的属性，可以指定不同的属性名
    private void updatePropertyIfNotNull(Object source, Object target, String sourceProperty, String targetProperty) {
        Object value = getProperty(source, sourceProperty);
        if (value != null) {
            setProperty(target, targetProperty, value);
        }
    }
}