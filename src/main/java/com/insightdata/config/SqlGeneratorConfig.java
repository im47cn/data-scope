 package com.insightdata.config;

 import com.insightdata.nlquery.sql.EnhancedSqlGenerator;
 import com.insightdata.nlquery.sql.SqlGenerator;
 import org.springframework.context.annotation.Bean;
 import org.springframework.context.annotation.Configuration;
 import org.springframework.context.annotation.Primary;

/**
 * SQL生成器配置类
 */
@Configuration
public class SqlGeneratorConfig {
    
    /**
     * 注册增强的SQL生成器作为主要实现
     */
    @Bean
    @Primary
    public SqlGenerator sqlGenerator(EnhancedSqlGenerator enhancedSqlGenerator) {
        return enhancedSqlGenerator;
    }
}