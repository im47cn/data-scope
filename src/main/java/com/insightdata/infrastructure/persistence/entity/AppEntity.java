package com.insightdata.infrastructure.persistence.entity;

import com.insightdata.infrastructure.persistence.converter.JsonMapConverter;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 低代码应用实体
 */
@Entity
@Table(name = "app")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AppEntity {
    
    /**
     * 应用ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 应用编码（唯一标识）
     */
    @Column(unique = true, nullable = false, length = 100)
    private String code;
    
    /**
     * 应用名称
     */
    @Column(nullable = false, length = 100)
    private String name;
    
    /**
     * 应用描述
     */
    @Column(length = 500)
    private String description;
    
    /**
     * 应用图标
     */
    @Column(length = 255)
    private String icon;
    
    /**
     * 应用类型
     */
    @Column(length = 50)
    private String type;
    
    /**
     * 应用版本
     */
    @Column(length = 50)
    private String version;
    
    /**
     * 首页ID
     */
    @Column
    private Long homePageId;
    
    /**
     * 发布状态（0：未发布，1：已发布）
     */
    @Column
    private Integer publishStatus;
    
    /**
     * 发布时间
     */
    @Column
    private LocalDateTime publishedAt;
    
    /**
     * 主题配置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> theme;
    
    /**
     * 样式配置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> styleConfig;
    
    /**
     * 应用设置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> settings;
    
    /**
     * 权限配置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> permissions;
    
    /**
     * 路由配置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> routes;
    
    /**
     * 菜单配置 - 使用 JSON 列类型
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> menus;
    
    /**
     * 全局状态
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> globalState;
    
    /**
     * 关联的查询ID列表
     */
    @ElementCollection
    @CollectionTable(name = "app_query", joinColumns = @JoinColumn(name = "app_id"))
    @Column(name = "query_id")
    private List<Long> queryIds;
    
    /**
     * 关联的数据源ID列表
     */
    @ElementCollection
    @CollectionTable(name = "app_data_source", joinColumns = @JoinColumn(name = "app_id"))
    @Column(name = "data_source_id")
    private List<Long> dataSourceIds;
    
    /**
     * 自定义配置
     */
    @Column(columnDefinition = "json")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, String> customConfig;
    
    /**
     * 创建人ID
     */
    @CreatedBy
    private Long createdBy;
    
    /**
     * 创建时间
     */
    @CreatedDate
    @Column
    private LocalDateTime createdAt;
    
    /**
     * 更新人ID
     */
    @LastModifiedBy
    private Long updatedBy;
    
    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;
}