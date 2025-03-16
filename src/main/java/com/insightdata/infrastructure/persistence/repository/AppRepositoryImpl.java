package com.insightdata.infrastructure.persistence.repository;

import com.insightdata.domain.model.lowcode.App;
import com.insightdata.domain.repository.AppRepository;
import com.insightdata.infrastructure.persistence.entity.AppEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

/**
 * 低代码应用仓库实现
 */
@Repository
public class AppRepositoryImpl implements AppRepository {
    
    private final JpaAppRepository jpaAppRepository;
    
    public AppRepositoryImpl(JpaAppRepository jpaAppRepository) {
        this.jpaAppRepository = jpaAppRepository;
    }
    
    @Override
    public App save(App app) {
        AppEntity entity = toEntity(app);
        AppEntity savedEntity = jpaAppRepository.save(entity);
        return toDomain(savedEntity);
    }
    
    @Override
    public Optional<App> findById(Long id) {
        return jpaAppRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public Optional<App> findByCode(String code) {
        return jpaAppRepository.findByCode(code).map(this::toDomain);
    }
    
    @Override
    public List<App> findAll() {
        return jpaAppRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<App> findPublished() {
        return jpaAppRepository.findAllPublished().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        jpaAppRepository.deleteById(id);
    }
    
    @Override
    public List<App> findByNameContaining(String name) {
        return jpaAppRepository.findByNameContaining(name).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByCode(String code) {
        return jpaAppRepository.existsByCode(code);
    }
    
    /**
     * 将应用实体转换为应用领域模型
     * 
     * @param entity 应用实体
     * @return 应用领域模型
     */
    private App toDomain(AppEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return App.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .icon(entity.getIcon())
                .type(entity.getType())
                .version(entity.getVersion())
                .homePageId(entity.getHomePageId())
                .publishStatus(entity.getPublishStatus())
                .publishedAt(entity.getPublishedAt())
                .theme(entity.getTheme())
                .styleConfig(entity.getStyleConfig())
                .settings(entity.getSettings())
                .permissions(entity.getPermissions())
                .routes(entity.getRoutes())
                .menus(entity.getMenus())
                .globalState(entity.getGlobalState())
                .queryIds(entity.getQueryIds())
                .dataSourceIds(entity.getDataSourceIds())
                .customConfig(entity.getCustomConfig())
                .createdBy(entity.getCreatedBy())
                .createdAt(entity.getCreatedAt())
                .updatedBy(entity.getUpdatedBy())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    /**
     * 将应用领域模型转换为应用实体
     * 
     * @param domain 应用领域模型
     * @return 应用实体
     */
    private AppEntity toEntity(App domain) {
        if (domain == null) {
            return null;
        }
        
        return AppEntity.builder()
                .id(domain.getId())
                .code(domain.getCode())
                .name(domain.getName())
                .description(domain.getDescription())
                .icon(domain.getIcon())
                .type(domain.getType())
                .version(domain.getVersion())
                .homePageId(domain.getHomePageId())
                .publishStatus(domain.getPublishStatus())
                .publishedAt(domain.getPublishedAt())
                .theme(domain.getTheme())
                .styleConfig(domain.getStyleConfig())
                .settings(domain.getSettings())
                .permissions(domain.getPermissions())
                .routes(domain.getRoutes())
                .menus(domain.getMenus())
                .globalState(domain.getGlobalState())
                .queryIds(domain.getQueryIds())
                .dataSourceIds(domain.getDataSourceIds())
                .customConfig(domain.getCustomConfig())
                .createdBy(domain.getCreatedBy())
                .createdAt(domain.getCreatedAt())
                .updatedBy(domain.getUpdatedBy())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
}