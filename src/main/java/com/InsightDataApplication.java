package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 数据管理与查询系统应用程序入口
 */
@SpringBootApplication
@EnableCaching
@EnableAsync
@EnableScheduling
@MapperScan("com.domain.repository")
public class InsightDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(InsightDataApplication.class, args);
    }
}