package com.insightdata.domain.model.lowcode;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据绑定模型
 * 定义组件与数据源之间的绑定关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBinding {
    
    /**
     * 数据绑定ID
     */
    private Long id;
    
    /**
     * 绑定的组件ID
     */
    private Long componentId;
    
    /**
     * 组件属性路径
     * 表示绑定到组件的哪个属性
     */
    private String propertyPath;
    
    /**
     * 绑定类型
     * 如：static(静态数据)、variable(变量)、query(查询)、
     * expression(表达式)、api(远程API)等
     */
    private String type;
    
    /**
     * 绑定值
     * 根据绑定类型不同，可能是静态值、变量名、查询ID、表达式或API路径
     */
    private String value;
    
    /**
     * 数据转换器
     * 定义如何转换数据源数据为组件所需格式的脚本
     */
    private String transformer;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 数据源ID
     * 当绑定类型为查询时使用
     */
    private Long dataSourceId;
    
    /**
     * 是否自动加载
     * 组件初始化时是否自动加载数据
     */
    private boolean autoLoad;
    
    /**
     * 加载条件
     * 定义何时加载数据的条件表达式
     */
    private String loadCondition;
    
    /**
     * 重新加载触发器
     * 定义何时需要重新加载数据
     */
    private String reloadTrigger;
    
    /**
     * 依赖变量
     * 数据绑定依赖的变量列表
     */
    @Builder.Default
    private Map<String, Object> dependencies = new HashMap<>();
    
    /**
     * 绑定参数
     * 用于传递给查询或API的参数
     */
    @Builder.Default
    private Map<String, Object> parameters = new HashMap<>();
    
    /**
     * 缓存设置
     * 数据缓存的相关设置
     */
    @Builder.Default
    private Map<String, Object> cacheSettings = new HashMap<>();
    
    /**
     * 与查询列表映射相关的配置
     */
    private Long queryId;
    
    /**
     * 映射配置
     * 详细定义字段映射规则
     */
    @Builder.Default
    private Map<String, Object> mappingConfig = new HashMap<>();
    
    /**
     * 显示配置
     * 如何在组件中展示数据的配置
     */
    @Builder.Default
    private Map<String, Object> displayConfig = new HashMap<>();
    
    /**
     * 自定义配置
     */
    @Builder.Default
    private Map<String, Object> customConfig = new HashMap<>();
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}