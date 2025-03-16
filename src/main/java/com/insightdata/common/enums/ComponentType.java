package com.insightdata.common.enums;

/**
 * 组件类型枚举
 * 定义低代码平台中支持的所有组件类型
 */
public enum ComponentType {
    
    // 布局类组件
    CONTAINER("容器"),
    GRID("栅格布局"),
    FORM("表单"),
    TABS("选项卡"),
    CARD("卡片"),
    COLLAPSE("折叠面板"),
    MODAL("模态框"),
    DRAWER("抽屉"),
    STEPS("步骤条"),
    
    // 展示类组件
    TABLE("表格"),
    LIST("列表"),
    TREE("树形控件"),
    CHART("图表"),
    STATISTIC("统计数值"),
    DESCRIPTION("描述列表"),
    IMAGE("图片"),
    CAROUSEL("轮播图"),
    TIMELINE("时间轴"),
    AVATAR("头像"),
    BADGE("徽标"),
    TAG("标签"),
    DIVIDER("分割线"),
    
    // 表单类组件
    INPUT("输入框"),
    TEXTAREA("文本域"),
    NUMBER("数字输入框"),
    SELECT("下拉选择器"),
    RADIO("单选框"),
    CHECKBOX("复选框"),
    SWITCH("开关"),
    SLIDER("滑动输入条"),
    DATE_PICKER("日期选择器"),
    TIME_PICKER("时间选择器"),
    DATETIME_PICKER("日期时间选择器"),
    UPLOAD("上传"),
    RATE("评分"),
    COLOR_PICKER("颜色选择器"),
    CASCADER("级联选择器"),
    TRANSFER("穿梭框"),
    AUTOCOMPLETE("自动完成"),
    
    // 导航类组件
    MENU("菜单"),
    PAGINATION("分页"),
    BREADCRUMB("面包屑"),
    DROPDOWN("下拉菜单"),
    AFFIX("固钉"),
    
    // 反馈类组件
    ALERT("警告提示"),
    PROGRESS("进度条"),
    RESULT("结果"),
    SPIN("加载中"),
    SKELETON("骨架屏"),
    
    // 业务类组件
    QUERY_BUILDER("查询构建器"),
    NATURAL_LANGUAGE_QUERY("自然语言查询"),
    DATA_MAPPING("数据映射"),
    WORKFLOW("工作流"),
    REPORT("报表"),
    DASHBOARD("仪表盘"),
    
    // 自定义组件
    CUSTOM("自定义组件");
    
    private final String description;
    
    ComponentType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return this.name() + "(" + this.description + ")";
    }
}