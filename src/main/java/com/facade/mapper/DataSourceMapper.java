package com.facade.mapper;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.domain.model.DataSource;
import com.facade.dto.DataSourceDTO;
import com.facade.dto.DataSourceListDTO;

/**
 * 数据源对象映射器
 */
@Component
public class DataSourceMapper {

    /**
     * 将DTO转换为领域模型
     * @param dto 数据源DTO
     * @return 数据源领域模型
     */
    public DataSource toEntity(DataSourceDTO dto) {
        if (dto == null) {
            return null;
        }
        
        DataSource entity = new DataSource();
        BeanUtils.copyProperties(dto, entity);
        
        // 标签处理，从字符串数组转为Set<String>
        if (dto.getTags() != null) {
            Set<String> tagSet = Stream.of(dto.getTags())
                    .filter(tag -> tag != null && !tag.isEmpty())
                    .collect(Collectors.toSet());
            entity.setTags(tagSet);
        } else {
            entity.setTags(Collections.emptySet());
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
        
        DataSourceDTO dto = new DataSourceDTO();
        BeanUtils.copyProperties(entity, dto);
        
        // 密码字段不传输
        dto.setPassword(null);
        
        // 标签处理，从Set<String>转为字符串数组
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            dto.setTags(entity.getTags().toArray(new String[0]));
        } else {
            dto.setTags(new String[0]);
        }
        
        return dto;
    }
    
    /**
     * 将领域模型转换为列表DTO
     * @param entity 数据源领域模型
     * @return 数据源列表DTO
     */
    public DataSourceListDTO toListDTO(DataSource entity) {
        if (entity == null) {
            return null;
        }
        
        DataSourceListDTO dto = new DataSourceListDTO();
        BeanUtils.copyProperties(entity, dto);
        
        // 标签处理，从Set<String>转为字符串数组
        if (entity.getTags() != null && !entity.getTags().isEmpty()) {
            dto.setTags(entity.getTags().toArray(new String[0]));
        } else {
            dto.setTags(new String[0]);
        }
        
        return dto;
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
    public List<DataSourceListDTO> toListDTOList(List<DataSource> entities) {
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
        
        // 只复制非null字段
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getType() != null) entity.setType(dto.getType());
        if (dto.getHost() != null) entity.setHost(dto.getHost());
        if (dto.getPort() != null) entity.setPort(dto.getPort());
        if (dto.getDatabaseName() != null) entity.setDatabaseName(dto.getDatabaseName());
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null) entity.setPassword(dto.getPassword());
        if (dto.getConnectionProperties() != null) entity.setConnectionProperties(dto.getConnectionProperties());
        if (dto.getEnabled() != null) entity.setEnabled(dto.getEnabled());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        
        // 标签更新
        if (dto.getTags() != null) {
            Set<String> tagSet = Stream.of(dto.getTags())
                    .filter(tag -> tag != null && !tag.isEmpty())
                    .collect(Collectors.toSet());
            entity.setTags(tagSet);
        }
        
        return entity;
    }
}