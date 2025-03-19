package com.insightdata.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.insightdata.service.CacheDemoService;

/**
 * 缓存演示控制器
 * 用于演示缓存功能
 */
@RestController
@RequestMapping("/api/cache-demo")
public class CacheDemoController {

    private static final Logger log = LoggerFactory.getLogger(CacheDemoController.class);

    @Autowired
    private CacheDemoService cacheDemoService;

    /**
     * 获取数据（使用缓存）
     * 第一次调用时会从"数据源"获取数据，后续调用会从缓存中获取
     * 
     * @param id 数据ID
     * @return 数据
     */
    @GetMapping("/data/{id}")
    public Map<String, Object> getData(@PathVariable String id) {
        log.info("请求数据: id={}", id);
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> data = cacheDemoService.getData(id);
        
        // 添加执行时间信息
        long executionTime = System.currentTimeMillis() - startTime;
        Map<String, Object> result = new HashMap<>(data);
        result.put("executionTimeMs", executionTime);
        
        return result;
    }
    
    /**
     * 获取查询结果（使用缓存）
     * 第一次调用时会执行查询，后续相同参数的调用会从缓存中获取
     * 
     * @param type 查询类型
     * @param params 查询参数
     * @return 查询结果
     */
    @GetMapping("/query")
    public Map<String, Object> getQueryResult(
            @RequestParam(defaultValue = "default") String type,
            @RequestParam(defaultValue = "") String params) {
        log.info("请求查询: type={}, params={}", type, params);
        long startTime = System.currentTimeMillis();
        
        Map<String, Object> data = cacheDemoService.getQueryResult(type, params);
        
        // 添加执行时间信息
        long executionTime = System.currentTimeMillis() - startTime;
        Map<String, Object> result = new HashMap<>(data);
        result.put("apiExecutionTimeMs", executionTime);
        
        return result;
    }
}