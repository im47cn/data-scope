package com.infrastructure.persistence.repository;

import com.domain.model.lowcode.App;
import com.domain.repository.AppRepository;
import com.infrastructure.persistence.entity.AppEntity;
import com.infrastructure.persistence.mapper.AppMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        
        return toDomain(entity);
    }
    
    @Override
    public Optional<App> findById(String id) {
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
    public List<App> findPublished() {
        return appMapper.findAllPublished().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
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
    
    @Override
    public void deleteById(String id) {
        appMapper.deleteById(id);
    }
    
    /**
     * 将领域模型转换为实体模型
     */
    private AppEntity toEntity(App app) {
        if (app == null) {
            return null;
        }
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
}