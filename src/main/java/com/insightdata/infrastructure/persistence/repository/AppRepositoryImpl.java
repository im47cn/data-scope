package com.insightdata.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.insightdata.domain.model.lowcode.App;
import com.insightdata.domain.repository.AppRepository;
import com.insightdata.infrastructure.persistence.entity.AppEntity;
import com.insightdata.infrastructure.persistence.mapper.AppMapper;

import lombok.RequiredArgsConstructor;

/**
 * App仓储的MyBatis实现
 */
@Repository
@RequiredArgsConstructor
public class AppRepositoryImpl implements AppRepository {
    
    private final AppMapper appMapper;
    
    @Override
    public App save(App app) {
        AppEntity entity = toEntity(app);
        
        if (entity.getId() == null) {
            appMapper.insert(entity);
        } else {
            appMapper.update(entity);
        }
        
        // 重新设置ID，因为insert操作会生成新ID
        app.setId(entity.getId());
        return app;
    }
    
    @Override
    public Optional<App> findById(Long id) {
        AppEntity entity = appMapper.findById(id);
        return Optional.ofNullable(entity).map(this::toDomain);
    }
    
    @Override
    public Optional<App> findByCode(String code) {
        AppEntity entity = appMapper.findByCode(code);
        return Optional.ofNullable(entity).map(this::toDomain);
    }
    
    @Override
    public List<App> findAll() {
        return appMapper.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteById(Long id) {
        appMapper.deleteById(id);
    }
    
    @Override
    public List<App> findByNameContaining(String name) {
        return appMapper.findByNameContaining(name).stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public boolean existsByCode(String code) {
        return appMapper.existsByCode(code);
    }
    
    /**
     * 将领域模型转换为实体模型
     */
    private AppEntity toEntity(App app) {
        return AppEntity.builder()
                .id(app.getId())
                .code(app.getCode())
                .name(app.getName())
                .description(app.getDescription())
                .icon(app.getIcon())
                .type(app.getType())
                .version(app.getVersion())
                .homePageId(app.getHomePageId())
                .publishStatus(app.getPublishStatus())
                .publishedAt(app.getPublishedAt())
                .theme(app.getTheme())
                .styleConfig(app.getStyleConfig())
                .settings(app.getSettings())
                .permissions(app.getPermissions())
                .routes(app.getRoutes())
                .menus(app.getMenus())
                .globalState(app.getGlobalState())
                .queryIds(app.getQueryIds())
                .dataSourceIds(app.getDataSourceIds())
                .customConfig(app.getCustomConfig())
                .createdBy(app.getCreatedBy())
                .createdAt(app.getCreatedAt())
                .updatedBy(app.getUpdatedBy())
                .updatedAt(app.getUpdatedAt())
                .build();
    }
    
    /**
     * 将实体模型转换为领域模型
     */
    private App toDomain(AppEntity entity) {
        App app = new App();
        app.setId(entity.getId());
        app.setCode(entity.getCode());
        app.setName(entity.getName());
        app.setDescription(entity.getDescription());
        app.setIcon(entity.getIcon());
        app.setType(entity.getType());
        app.setVersion(entity.getVersion());
        app.setHomePageId(entity.getHomePageId());
        app.setPublishStatus(entity.getPublishStatus());
        app.setPublishedAt(entity.getPublishedAt());
        app.setTheme(entity.getTheme());
        app.setStyleConfig(entity.getStyleConfig());
        app.setSettings(entity.getSettings());
        app.setPermissions(entity.getPermissions());
        app.setRoutes(entity.getRoutes());
        app.setMenus(entity.getMenus());
        app.setGlobalState(entity.getGlobalState());
        app.setQueryIds(entity.getQueryIds());
        app.setDataSourceIds(entity.getDataSourceIds());
        app.setCustomConfig(entity.getCustomConfig());
        app.setCreatedBy(entity.getCreatedBy());
        app.setCreatedAt(entity.getCreatedAt());
        app.setUpdatedBy(entity.getUpdatedBy());
        app.setUpdatedAt(entity.getUpdatedAt());
        return app;
    }
}