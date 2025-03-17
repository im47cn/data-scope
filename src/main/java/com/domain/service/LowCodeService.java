package com.domain.service;

import com.domain.model.lowcode.App;
import com.domain.model.lowcode.Component;
import com.domain.model.lowcode.DataBinding;
import com.domain.model.lowcode.Page;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 低代码应用服务接口
 * 提供应用、页面、组件和数据绑定的管理功能
 */
public interface LowCodeService {

    // 应用相关方法
    
    /**
     * 创建应用
     *
     * @param app 应用信息
     * @return 创建的应用
     */
    App createApp(App app);
    
    /**
     * 更新应用
     *
     * @param app 应用信息
     * @return 更新后的应用
     */
    App updateApp(App app);
    
    /**
     * 删除应用
     *
     * @param appId 应用ID
     */
    void deleteApp(String appId);
    
    /**
     * 根据ID获取应用
     *
     * @param appId 应用ID
     * @return 应用信息
     */
    Optional<App> getAppById(String appId);
    
    /**
     * 根据编码获取应用
     *
     * @param code 应用编码
     * @return 应用信息
     */
    Optional<App> getAppByCode(String code);
    
    /**
     * 获取所有应用
     *
     * @return 应用列表
     */
    List<App> getAllApps();
    
    /**
     * 发布应用
     *
     * @param appId 应用ID
     * @return 发布后的应用
     */
    App publishApp(String appId);
    
    /**
     * 取消发布应用
     *
     * @param appId 应用ID
     * @return 取消发布后的应用
     */
    App unpublishApp(String appId);
    
    // 页面相关方法
    
    /**
     * 创建页面
     *
     * @param page 页面信息
     * @return 创建的页面
     */
    Page createPage(Page page);
    
    /**
     * 更新页面
     *
     * @param page 页面信息
     * @return 更新后的页面
     */
    Page updatePage(Page page);
    
    /**
     * 删除页面
     *
     * @param pageId 页面ID
     */
    void deletePage(String pageId);
    
    /**
     * 根据ID获取页面
     *
     * @param pageId 页面ID
     * @return 页面信息
     */
    Optional<Page> getPageById(String pageId);
    
    /**
     * 根据应用ID获取页面列表
     *
     * @param appId 应用ID
     * @return 页面列表
     */
    List<Page> getPagesByAppId(String appId);
    
    // 组件相关方法
    
    /**
     * 创建组件
     *
     * @param component 组件信息
     * @return 创建的组件
     */
    Component createComponent(Component component);
    
    /**
     * 更新组件
     *
     * @param component 组件信息
     * @return 更新后的组件
     */
    Component updateComponent(Component component);
    
    /**
     * 删除组件
     *
     * @param componentId 组件ID
     */
    void deleteComponent(String componentId);
    
    /**
     * 根据ID获取组件
     *
     * @param componentId 组件ID
     * @return 组件信息
     */
    Optional<Component> getComponentById(String componentId);
    
    /**
     * 根据页面ID获取组件列表
     *
     * @param pageId 页面ID
     * @return 组件列表
     */
    List<Component> getComponentsByPageId(String pageId);
    
    // 数据绑定相关方法
    
    /**
     * 创建数据绑定
     *
     * @param dataBinding 数据绑定信息
     * @return 创建的数据绑定
     */
    DataBinding createDataBinding(DataBinding dataBinding);
    
    /**
     * 更新数据绑定
     *
     * @param dataBinding 数据绑定信息
     * @return 更新后的数据绑定
     */
    DataBinding updateDataBinding(DataBinding dataBinding);
    
    /**
     * 删除数据绑定
     *
     * @param dataBindingId 数据绑定ID
     */
    void deleteDataBinding(String dataBindingId);
    
    /**
     * 根据ID获取数据绑定
     *
     * @param dataBindingId 数据绑定ID
     * @return 数据绑定信息
     */
    Optional<DataBinding> getDataBindingById(String dataBindingId);
    
    /**
     * 根据组件ID获取数据绑定列表
     *
     * @param componentId 组件ID
     * @return 数据绑定列表
     */
    List<DataBinding> getDataBindingsByComponentId(String componentId);
    
    /**
     * 根据查询ID获取数据绑定列表
     *
     * @param queryId 查询ID
     * @return 数据绑定列表
     */
    List<DataBinding> getDataBindingsByQueryId(String queryId);
    
    /**
     * 根据应用ID导出应用配置
     *
     * @param appId 应用ID
     * @return 应用配置JSON
     */
    String exportAppConfig(String appId);
    
    /**
     * 导入应用配置
     *
     * @param appConfig 应用配置JSON
     * @return 导入的应用
     */
    App importAppConfig(String appConfig);
    
    /**
     * 为低代码组件生成查询条件表单
     *
     * @param queryId 查询ID
     * @return 表单组件配置
     */
    Component generateQueryForm(String queryId);
    
    /**
     * 为查询结果生成表格组件
     *
     * @param queryId 查询ID
     * @return 表格组件配置
     */
    Component generateResultTable(String queryId);
    
    /**
     * 根据查询ID预览应用
     *
     * @param queryId 查询ID
     * @return 预览URL
     */
    String previewAppByQueryId(String queryId);
    
    /**
     * 智能推荐组件配置
     * 
     * @param queryId 查询ID
     * @param context 上下文信息
     * @return 推荐的组件配置
     */
    Map<String, Object> recommendComponentConfig(String queryId, Map<String, Object> context);
}