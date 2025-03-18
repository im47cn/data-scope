package com.insightdata.domain.service;

import com.insightdata.domain.lowcode.model.App;

import java.util.List;
import java.util.Optional;

/**
 * 低代码应用服务接口
 */
public interface AppService {
    
    /**
     * 创建应用
     *
     * @param app 应用对象
     * @return 创建后的应用对象
     */
    App createApp(App app);
    
    /**
     * 更新应用
     *
     * @param app 应用对象
     * @return 更新后的应用对象
     */
    App updateApp(App app);
    
    /**
     * 根据ID查找应用
     *
     * @param id 应用ID
     * @return 应用对象
     */
    Optional<App> findAppById(String id);
    
    /**
     * 根据编码查找应用
     *
     * @param code 应用编码
     * @return 应用对象
     */
    Optional<App> findAppByCode(String code);
    
    /**
     * 获取所有应用
     *
     * @return 应用列表
     */
    List<App> findAllApps();
    
    /**
     * 根据类型查找应用
     *
     * @param type 应用类型
     * @return 应用列表
     */
    List<App> findAppsByType(String type);
    
    /**
     * 根据发布状态查找应用
     *
     * @param publishStatus 发布状态
     * @return 应用列表
     */
    List<App> findAppsByPublishStatus(Integer publishStatus);
    
    /**
     * 删除应用
     *
     * @param id 应用ID
     */
    void deleteApp(String id);
    
    /**
     * 发布应用
     *
     * @param id 应用ID
     * @return 发布后的应用对象
     */
    App publishApp(String id);
    
    /**
     * 下架应用
     *
     * @param id 应用ID
     * @return 下架后的应用对象
     */
    App unpublishApp(String id);
    
    /**
     * 复制应用
     *
     * @param id 原应用ID
     * @param newName 新应用名称
     * @param newCode 新应用编码
     * @return 复制后的新应用对象
     */
    App duplicateApp(String id, String newName, String newCode);
    
    /**
     * 检查应用编码是否已存在
     *
     * @param code 应用编码
     * @return 是否存在
     */
    boolean isCodeExists(String code);
}