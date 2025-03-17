package com.domain.service;

import com.domain.model.lowcode.AppConfig;
import com.domain.model.lowcode.ComponentConfig;
import com.domain.model.lowcode.DataBinding;
import com.domain.model.lowcode.PageLayout;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 低代码应用服务接口
 * 提供管理低代码应用的核心功能
 */
public interface LowCodeAppService {
    
    /**
     * 创建应用
     * @param appConfig 应用配置
     * @return 创建的应用配置
     */
    AppConfig createApp(AppConfig appConfig);
    
    /**
     * 更新应用
     * @param appConfig 应用配置
     * @return 更新后的应用配置
     */
    AppConfig updateApp(AppConfig appConfig);
    
    /**
     * 删除应用
     * @param appId 应用ID
     */
    void deleteApp(Long appId);
    
    /**
     * 获取应用
     * @param appId 应用ID
     * @return 应用配置
     */
    Optional<AppConfig> getApp(Long appId);
    
    /**
     * 获取所有应用
     * @return 应用配置列表
     */
    List<AppConfig> getAllApps();
    
    /**
     * 发布应用
     * @param appId 应用ID
     * @return 发布后的应用配置
     */
    AppConfig publishApp(Long appId);
    
    /**
     * 取消发布应用
     * @param appId 应用ID
     * @return 取消发布后的应用配置
     */
    AppConfig unpublishApp(Long appId);
    
    /**
     * 创建页面
     * @param appId 应用ID
     * @param pageLayout 页面布局
     * @return 创建的页面布局
     */
    PageLayout createPage(Long appId, PageLayout pageLayout);
    
    /**
     * 更新页面
     * @param pageLayout 页面布局
     * @return 更新后的页面布局
     */
    PageLayout updatePage(PageLayout pageLayout);
    
    /**
     * 删除页面
     * @param pageId 页面ID
     */
    void deletePage(Long pageId);
    
    /**
     * 获取页面
     * @param pageId 页面ID
     * @return 页面布局
     */
    Optional<PageLayout> getPage(Long pageId);
    
    /**
     * 获取应用的所有页面
     * @param appId 应用ID
     * @return 页面布局列表
     */
    List<PageLayout> getAppPages(Long appId);
    
    /**
     * 创建组件
     * @param pageId 页面ID
     * @param componentConfig 组件配置
     * @return 创建的组件配置
     */
    ComponentConfig createComponent(Long pageId, ComponentConfig componentConfig);
    
    /**
     * 更新组件
     * @param componentConfig 组件配置
     * @return 更新后的组件配置
     */
    ComponentConfig updateComponent(ComponentConfig componentConfig);
    
    /**
     * 删除组件
     * @param componentId 组件ID
     */
    void deleteComponent(Long componentId);
    
    /**
     * 获取组件
     * @param componentId 组件ID
     * @return 组件配置
     */
    Optional<ComponentConfig> getComponent(Long componentId);
    
    /**
     * 获取页面的所有组件
     * @param pageId 页面ID
     * @return 组件配置列表
     */
    List<ComponentConfig> getPageComponents(Long pageId);
    
    /**
     * 创建数据绑定
     * @param componentId 组件ID
     * @param dataBinding 数据绑定
     * @return 创建的数据绑定
     */
    DataBinding createDataBinding(Long componentId, DataBinding dataBinding);
    
    /**
     * 更新数据绑定
     * @param dataBinding 数据绑定
     * @return 更新后的数据绑定
     */
    DataBinding updateDataBinding(DataBinding dataBinding);
    
    /**
     * 删除数据绑定
     * @param dataBindingId 数据绑定ID
     */
    void deleteDataBinding(Long dataBindingId);
    
    /**
     * 获取数据绑定
     * @param dataBindingId 数据绑定ID
     * @return 数据绑定
     */
    Optional<DataBinding> getDataBinding(Long dataBindingId);
    
    /**
     * 执行组件的数据查询
     * @param componentId 组件ID
     * @param parameters 查询参数
     * @return 查询结果数据
     */
    Map<String, Object> executeComponentQuery(Long componentId, Map<String, Object> parameters);
    
    /**
     * 生成AI辅助的组件配置建议
     * @param context 上下文信息
     * @return 配置建议
     */
    Map<String, Object> generateAIComponentSuggestion(Map<String, Object> context);
}