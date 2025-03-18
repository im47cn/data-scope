package com.insightdata.config;

import com.insightdata.domain.nlquery.sql.DefaultSqlGenerator;
import com.insightdata.domain.nlquery.sql.SqlGenerator;
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
    public SqlGenerator sqlGenerator(DefaultSqlGenerator defaultSqlGenerator) {
        return defaultSqlGenerator;
    }
}