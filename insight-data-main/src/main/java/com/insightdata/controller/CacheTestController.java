package com.insightdata.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 缓存测试控制器
 * 用于测试缓存是否正常工作
 */
@RestController
@RequestMapping("/api/cache-test")
public class CacheTestController {

    private static final Logger log = LoggerFactory.getLogger(CacheTestController.class);

    @Autowired
    private CacheManager cacheManager;

    /**
     * 测试缓存状态
     * @return 缓存状态信息
     */
    @GetMapping("/status")
    public Map<String, Object> getCacheStatus() {
        log.info("测试缓存状态");
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取缓存管理器信息
            result.put("cacheManager", cacheManager.getClass().getName());
            
            // 获取所有缓存名称
            Set<String> cacheNames = cacheManager.getCacheNames()
                    .stream()
                    .collect(Collectors.toSet());
            result.put("cacheNames", cacheNames);
            
            // 测试写入缓存
            Cache testCache = cacheManager.getCache("metadata");
            if (testCache != null) {
                testCache.put("test-key", "test-value-" + System.currentTimeMillis());
                Object value = testCache.get("test-key", String.class);
                result.put("testWrite", value != null);
                result.put("testValue", value);
            } else {
                result.put("testWrite", false);
                result.put("error", "无法获取metadata缓存");
            }
            
            result.put("status", "success");
        } catch (Exception e) {
            log.error("缓存测试失败", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getName());
        }
        
        return result;
    }
    
    /**
     * 清除所有缓存
     * @return 操作结果
     */
    @GetMapping("/clear")
    public Map<String, Object> clearCache() {
        log.info("清除所有缓存");
        Map<String, Object> result = new HashMap<>();
        
        try {
            cacheManager.getCacheNames().forEach(name -> {
                Cache cache = cacheManager.getCache(name);
                if (cache != null) {
                    cache.clear();
                }
            });
            result.put("status", "success");
            result.put("message", "所有缓存已清除");
        } catch (Exception e) {
            log.error("清除缓存失败", e);
            result.put("status", "error");
            result.put("error", e.getMessage());
        }
        
        return result;
    }
}