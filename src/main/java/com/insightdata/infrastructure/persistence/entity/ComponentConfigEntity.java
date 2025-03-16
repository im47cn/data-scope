package com.insightdata.infrastructure.persistence.entity;

import com.insightdata.infrastructure.persistence.converter.JsonMapConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 组件配置实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "component_config")
public class ComponentConfigEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "type", nullable = false, length = 50)
    private String type;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "query_id")
    private Long queryId;
    
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "properties", columnDefinition = "json")
    private Map<String, Object> properties;
    
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "style", columnDefinition = "json")
    private Map<String, Object> style;
    
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "data_source_config", columnDefinition = "json")
    private Map<String, Object> dataSourceConfig;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "created_by", length = 100)
    private String createdBy;
    
    @Column(name = "updated_by", length = 100)
    private String updatedBy;
}