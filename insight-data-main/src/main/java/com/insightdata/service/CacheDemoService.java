package com.insightdata.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 缓存演示服务
 * 用于演示如何使用缓存
 */
@Service
public class CacheDemoService {

    private static final Logger log = LoggerFactory.getLogger(CacheDemoService.class);

    /**
     * 获取数据（使用缓存）
     * 该方法的结果将被缓存在"metadata"缓存中，键为方法参数
     * 
     * @param id 数据ID
     * @return 数据
     */
    @Cacheable(value = "metadata", key = "#id")
    public Map<String, Object> getData(String id) {
        log.info("从数据源获取数据: id={}", id);
        
        // 模拟从数据源获取数据（耗时操作）
        try {
            Thread.sleep(1000); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 创建示例数据
        Map<String, Object> data = new HashMap<>();
        data.put("id", id);
        data.put("name", "示例数据-" + id);
        data.put("timestamp", System.currentTimeMillis());
        data.put("uuid", UUID.randomUUID().toString());
        
        return data;
    }
    
    /**
     * 获取查询结果（使用缓存）
     * 该方法的结果将被缓存在"query-result"缓存中，键为方法参数组合
     * 
     * @param queryType 查询类型
     * @param params 查询参数
     * @return 查询结果
     */
    @Cacheable(value = "query-result", key = "#queryType + '-' + #params")
    public Map<String, Object> getQueryResult(String queryType, String params) {
        log.info("执行查询: type={}, params={}", queryType, params);
        
        // 模拟查询执行（耗时操作）
        try {
            Thread.sleep(1500); // 模拟耗时操作
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // 创建示例结果
        Map<String, Object> result = new HashMap<>();
        result.put("queryType", queryType);
        result.put("params", params);
        result.put("resultCount", (int) (Math.random() * 100));
        result.put("executionTime", System.currentTimeMillis());
        result.put("cached", false);
        
        return result;
    }
}