package com.insightdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

/**
 * 缓存回退配置类
 * 当Redis缓存管理器无法创建时，提供一个简单的内存缓存作为回退选项
 */
@Configuration
public class CacheFallbackConfig {

    private static final Logger log = LoggerFactory.getLogger(CacheFallbackConfig.class);

    /**
     * 创建一个简单的内存缓存管理器作为回退选项
     * 仅在没有RedisCacheManager时创建
     */
    @Bean
    @ConditionalOnMissingBean(RedisCacheManager.class)
    public CacheManager fallbackCacheManager() {
        log.warn("Redis缓存管理器不可用，使用内存缓存作为回退选项");
        log.warn("请确保Redis服务器正在运行，并且可以从应用程序访问");
        log.warn("内存缓存不适合生产环境，仅用于开发和测试");
        
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(java.util.Arrays.asList("metadata", "query-result"));
        return cacheManager;
    }
}