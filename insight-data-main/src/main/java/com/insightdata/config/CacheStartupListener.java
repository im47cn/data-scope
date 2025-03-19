package com.insightdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

/**
 * 缓存启动监听器
 * 在应用程序启动时检查缓存配置
 */
@Component
public class CacheStartupListener implements ApplicationListener<ApplicationStartedEvent> {

    private static final Logger log = LoggerFactory.getLogger(CacheStartupListener.class);

    @Autowired
    private CacheManager cacheManager;

    @Autowired(required = false)
    private RedisConnectionFactory redisConnectionFactory;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        log.info("应用程序启动完成，检查缓存配置");
        
        // 检查缓存管理器
        log.info("缓存管理器类型: {}", cacheManager.getClass().getName());
        log.info("可用缓存: {}", cacheManager.getCacheNames());
        
        // 检查Redis连接
        if (redisConnectionFactory != null) {
            try {
                log.info("Redis连接工厂类型: {}", redisConnectionFactory.getClass().getName());
                RedisConnection connection = redisConnectionFactory.getConnection();
                connection.ping();
                log.info("Redis连接测试成功");
                connection.close();
            } catch (Exception e) {
                log.error("Redis连接测试失败", e);
                log.warn("如果您不需要使用Redis缓存，可以在application.yml中将spring.cache.type设置为simple");
            }
        } else {
            log.warn("未找到RedisConnectionFactory，Redis缓存可能不可用");
            log.info("当前使用的是内存缓存");
        }
    }
}