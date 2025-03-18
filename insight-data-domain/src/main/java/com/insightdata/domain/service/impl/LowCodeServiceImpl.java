package com.insightdata.domain.service.impl;

import com.insightdata.domain.exception.InsightDataException;
import com.insightdata.domain.lowcode.model.App;
import com.insightdata.domain.lowcode.model.Component;
import com.insightdata.domain.lowcode.model.DataBinding;
import com.insightdata.domain.lowcode.model.Page;
import com.insightdata.domain.repository.AppRepository;
import com.insightdata.domain.repository.ComponentRepository;
import com.insightdata.domain.repository.DataBindingRepository;
import com.insightdata.domain.repository.PageRepository;
import com.insightdata.domain.service.LowCodeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 低代码应用服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LowCodeServiceImpl implements LowCodeService {

    private final AppRepository appRepository;
    private final PageRepository pageRepository;
    private final ComponentRepository componentRepository;
    private final DataBindingRepository dataBindingRepository;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public App createApp(App app) {
        log.info("Creating new app: {}", app.getName());
        validateApp(app);
        
        app.setCreatedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        app.setPublishStatus(0); // 未发布状态
        
        return appRepository.save(app);
    }

    @Override
    @Transactional
    public App updateApp(App app) {
        log.info("Updating app: {}", app.getId());
        validateApp(app);
        
        App existingApp = appRepository.findById(app.getId())
            .orElseThrow(() -> new InsightDataException("App not found: " + app.getId()));
        
        app.setUpdatedAt(LocalDateTime.now());
        app.setCreatedAt(existingApp.getCreatedAt());
        
        return appRepository.save(app);
    }

    @Override
    @Transactional
    public void deleteApp(String appId) {
        log.info("Deleting app: {}", appId);
        appRepository.findById(appId)
            .orElseThrow(() -> new InsightDataException("App not found: " + appId));
        
        // 删除关联的页面、组件和数据绑定
        List<Page> pages = pageRepository.findByAppId(appId);
        for (Page page : pages) {
            deletePage(page.getId());
        }
        
        appRepository.deleteById(appId);
    }

    @Override
    public Optional<App> getAppById(String appId) {
        return appRepository.findById(appId);
    }

    @Override
    public Optional<App> getAppByCode(String code) {
        return appRepository.findByCode(code);
    }

    @Override
    public List<App> getAllApps() {
        return appRepository.findAll();
    }

    @Override
    @Transactional
    public App publishApp(String appId) {
        log.info("Publishing app: {}", appId);
        App app = appRepository.findById(appId)
            .orElseThrow(() -> new InsightDataException("App not found: " + appId));
        
        app.setPublishStatus(1);
        app.setPublishedAt(LocalDateTime.now());
        app.setUpdatedAt(LocalDateTime.now());
        
        return appRepository.save(app);
    }

    @Override
    @Transactional
    public App unpublishApp(String appId) {
        log.info("Unpublishing app: {}", appId);
        App app = appRepository.findById(appId)
            .orElseThrow(() -> new InsightDataException("App not found: " + appId));
        
        app.setPublishStatus(0);
        app.setUpdatedAt(LocalDateTime.now());
        
        return appRepository.save(app);
    }

    @Override
    @Transactional
    public Page createPage(Page page) {
        log.info("Creating new page for app: {}", page.getAppId());
        validatePage(page);
        
        page.setCreatedAt(LocalDateTime.now());
        page.setUpdatedAt(LocalDateTime.now());
        
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public Page updatePage(Page page) {
        log.info("Updating page: {}", page.getId());
        validatePage(page);
        
        Page existingPage = pageRepository.findById(page.getId())
            .orElseThrow(() -> new InsightDataException("Page not found: " + page.getId()));
        
        page.setUpdatedAt(LocalDateTime.now());
        page.setCreatedAt(existingPage.getCreatedAt());
        
        return pageRepository.save(page);
    }

    @Override
    @Transactional
    public void deletePage(String pageId) {
        log.info("Deleting page: {}", pageId);
        Page page = pageRepository.findById(pageId)
            .orElseThrow(() -> new InsightDataException("Page not found: " + pageId));
        
        // 删除页面下的所有组件
        List<Component> components = componentRepository.findByPageId(pageId);
        for (Component component : components) {
            deleteComponent(component.getId());
        }
        
        pageRepository.deleteById(pageId);
    }

    @Override
    public Optional<Page> getPageById(String pageId) {
        return pageRepository.findById(pageId);
    }

    @Override
    public List<Page> getPagesByAppId(String appId) {
        return pageRepository.findByAppId(appId);
    }

    @Override
    @Transactional
    public Component createComponent(Component component) {
        log.info("Creating new component: {}", component.getType());
        validateComponent(component);
        return componentRepository.save(component);
    }

    @Override
    @Transactional
    public Component updateComponent(Component component) {
        log.info("Updating component: {}", component.getId());
        validateComponent(component);
        
        componentRepository.findById(component.getId())
            .orElseThrow(() -> new InsightDataException("Component not found: " + component.getId()));
        
        return componentRepository.save(component);
    }

    @Override
    @Transactional
    public void deleteComponent(String componentId) {
        log.info("Deleting component: {}", componentId);
        Component component = componentRepository.findById(componentId)
            .orElseThrow(() -> new InsightDataException("Component not found: " + componentId));
        
        // 删除组件相关的数据绑定
        List<DataBinding> dataBindings = dataBindingRepository.findByComponentId(componentId);
        for (DataBinding binding : dataBindings) {
            deleteDataBinding(binding.getId());
        }
        
        componentRepository.deleteById(componentId);
    }

    @Override
    public Optional<Component> getComponentById(String componentId) {
        return componentRepository.findById(componentId);
    }

    @Override
    public List<Component> getComponentsByPageId(String pageId) {
        return componentRepository.findByPageId(pageId);
    }

    @Override
    @Transactional
    public DataBinding createDataBinding(DataBinding dataBinding) {
        log.info("Creating new data binding for component: {}", dataBinding.getComponentId());
        validateDataBinding(dataBinding);
        
        dataBinding.setCreatedAt(LocalDateTime.now());
        dataBinding.setUpdatedAt(LocalDateTime.now());
        
        return dataBindingRepository.save(dataBinding);
    }

    @Override
    @Transactional
    public DataBinding updateDataBinding(DataBinding dataBinding) {
        log.info("Updating data binding: {}", dataBinding.getId());
        validateDataBinding(dataBinding);
        
        DataBinding existingBinding = dataBindingRepository.findById(dataBinding.getId())
            .orElseThrow(() -> new InsightDataException("DataBinding not found: " + dataBinding.getId()));
        
        dataBinding.setUpdatedAt(LocalDateTime.now());
        dataBinding.setCreatedAt(existingBinding.getCreatedAt());
        
        return dataBindingRepository.save(dataBinding);
    }

    @Override
    @Transactional
    public void deleteDataBinding(String dataBindingId) {
        log.info("Deleting data binding: {}", dataBindingId);
        dataBindingRepository.findById(dataBindingId)
            .orElseThrow(() -> new InsightDataException("DataBinding not found: " + dataBindingId));
        
        dataBindingRepository.deleteById(dataBindingId);
    }

    @Override
    public Optional<DataBinding> getDataBindingById(String dataBindingId) {
        return dataBindingRepository.findById(dataBindingId);
    }

    @Override
    public List<DataBinding> getDataBindingsByComponentId(String componentId) {
        return dataBindingRepository.findByComponentId(componentId);
    }

    @Override
    public List<DataBinding> getDataBindingsByQueryId(String queryId) {
        return dataBindingRepository.findByQueryId(queryId);
    }

    @Override
    public String exportAppConfig(String appId) {
        log.info("Exporting app configuration: {}", appId);
        App app = appRepository.findById(appId)
            .orElseThrow(() -> new InsightDataException("App not found: " + appId));
        
        try {
            return objectMapper.writeValueAsString(app);
        } catch (Exception e) {
            throw new InsightDataException("Failed to export app configuration", e);
        }
    }

    @Override
    @Transactional
    public App importAppConfig(String appConfig) {
        log.info("Importing app configuration");
        try {
            App app = objectMapper.readValue(appConfig, App.class);
            app.setId(null); // 清除ID，作为新应用导入
            return createApp(app);
        } catch (Exception e) {
            throw new InsightDataException("Failed to import app configuration", e);
        }
    }

    @Override
    public Component generateQueryForm(String queryId) {
        log.info("Generating query form for query: {}", queryId);
        // TODO: 实现查询表单生成逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Component generateResultTable(String queryId) {
        log.info("Generating result table for query: {}", queryId);
        // TODO: 实现结果表格生成逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String previewAppByQueryId(String queryId) {
        log.info("Generating preview URL for query: {}", queryId);
        // TODO: 实现应用预览逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Map<String, Object> recommendComponentConfig(String queryId, Map<String, Object> context) {
        log.info("Generating component recommendations for query: {}", queryId);
        // TODO: 实现组件配置推荐逻辑
        throw new UnsupportedOperationException("Not implemented yet");
    }

    private void validateApp(App app) {
        if (app.getName() == null || app.getName().trim().isEmpty()) {
            throw new InsightDataException("App name is required");
        }
        if (app.getCode() == null || app.getCode().trim().isEmpty()) {
            throw new InsightDataException("App code is required");
        }
        // TODO: 添加更多验证逻辑
    }

    private void validatePage(Page page) {
        if (page.getAppId() == null) {
            throw new InsightDataException("Page must belong to an app");
        }
        if (page.getTitle() == null || page.getTitle().trim().isEmpty()) {
            throw new InsightDataException("Page title is required");
        }
        // TODO: 添加更多验证逻辑
    }

    private void validateComponent(Component component) {
        if (component.getType() == null || component.getType().trim().isEmpty()) {
            throw new InsightDataException("Component type is required");
        }
        // TODO: 添加更多验证逻辑
    }

    private void validateDataBinding(DataBinding dataBinding) {
        if (dataBinding.getComponentId() == null) {
            throw new InsightDataException("DataBinding must be associated with a component");
        }
        if (dataBinding.getType() == null || dataBinding.getType().trim().isEmpty()) {
            throw new InsightDataException("DataBinding type is required");
        }
        // TODO: 添加更多验证逻辑
    }
}