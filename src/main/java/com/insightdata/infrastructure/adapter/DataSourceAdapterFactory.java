package com.insightdata.infrastructure.adapter;

import com.insightdata.common.enums.DataSourceType;
import com.insightdata.common.exception.DataSourceException;
import com.insightdata.domain.adapter.DataSourceAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 数据源适配器工厂
 * 用于根据数据源类型创建对应的适配器
 */
@Component
public class DataSourceAdapterFactory {
    
    private final Map<String, DataSourceAdapter> adapterMap = new HashMap<>();
    
    @Autowired
    public DataSourceAdapterFactory(List<DataSourceAdapter> adapters) {
        // 注册所有适配器
        for (DataSourceAdapter adapter : adapters) {
            adapterMap.put(adapter.getType(), adapter);
        }
    }
    
    /**
     * 获取适配器
     *
     * @param type 数据源类型
     * @return 数据源适配器
     */
    public DataSourceAdapter getAdapter(DataSourceType type) {
        DataSourceAdapter adapter = adapterMap.get(type.name());
        if (adapter == null) {
            throw DataSourceException.invalidConfig("Unsupported data source type: " + type);
        }
        return adapter;
    }
    
    /**
     * 获取适配器
     *
     * @param typeName 数据源类型名称
     * @return 数据源适配器
     */
    public DataSourceAdapter getAdapter(String typeName) {
        DataSourceAdapter adapter = adapterMap.get(typeName);
        if (adapter == null) {
            throw DataSourceException.invalidConfig("Unsupported data source type: " + typeName);
        }
        return adapter;
    }
    
    /**
     * 注册适配器
     *
     * @param adapter 数据源适配器
     */
    public void registerAdapter(DataSourceAdapter adapter) {
        adapterMap.put(adapter.getType(), adapter);
    }
    
    /**
     * 获取所有支持的数据源类型
     *
     * @return 数据源类型列表
     */
    public List<String> getSupportedTypes() {
        return Collections.unmodifiableList(new ArrayList<>(adapterMap.keySet()));
    }
}