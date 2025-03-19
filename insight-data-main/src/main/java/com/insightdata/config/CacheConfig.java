package com.insightdata.config;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 简化的缓存配置类
 * 提供基本的Redis配置和内存缓存回退选项
 */
@Configuration
public class CacheConfig extends CachingConfigurerSupport {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    @Value("${spring.cache.type:redis}")
    private String cacheType;

    @PostConstruct
    public void init() {
        log.info("初始化缓存配置: type={}, redisHost={}, redisPort={}", cacheType, redisHost, redisPort);
    }

    /**
     * Redis连接工厂
     */
    @Bean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
    public RedisConnectionFactory redisConnectionFactory() {
        log.info("创建Redis连接工厂: {}:{}", redisHost, redisPort);
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(config);
    }

    /**
     * Redis模板
     */
    @Bean
    @ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        log.info("创建RedisTemplate");
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 缓存管理器
     * 如果spring.cache.type=redis，则使用Redis缓存
     * 否则使用内存缓存
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        if ("redis".equals(cacheType)) {
            try {
                log.info("尝试创建RedisCacheManager");
                RedisConnectionFactory factory = redisConnectionFactory();
                org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder builder = 
                    org.springframework.data.redis.cache.RedisCacheManager.builder(factory);
                return builder.build();
            } catch (Exception e) {
                log.error("创建RedisCacheManager失败，回退到内存缓存", e);
                return createSimpleCacheManager();
            }
        } else {
            log.info("使用内存缓存");
            return createSimpleCacheManager();
        }
    }

    private CacheManager createSimpleCacheManager() {
        log.info("创建内存缓存管理器");
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager();
        cacheManager.setCacheNames(java.util.Arrays.asList("metadata", "query-result"));
        return cacheManager;
    }
}