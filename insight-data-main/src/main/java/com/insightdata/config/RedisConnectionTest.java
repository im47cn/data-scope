package com.insightdata.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis连接测试类
 * 仅在开发环境中启用
 */
@Component
@Profile("dev")
public class RedisConnectionTest implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(RedisConnectionTest.class);

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(String... args) {
        try {
            log.info("Testing Redis connection...");
            
            // 测试连接
            redisConnectionFactory.getConnection().ping();
            log.info("Redis connection test successful!");
            
            // 测试读写操作
            String testKey = "test:connection";
            redisTemplate.opsForValue().set(testKey, "Connected");
            Object value = redisTemplate.opsForValue().get(testKey);
            log.info("Redis read/write test: {}", value);
            
            // 清理测试数据
            redisTemplate.delete(testKey);
        } catch (Exception e) {
            log.error("Redis connection test failed!", e);
            log.error("Please ensure Redis server is running and accessible.");
            log.error("Check your Redis configuration in application.yml");
        }
    }
}