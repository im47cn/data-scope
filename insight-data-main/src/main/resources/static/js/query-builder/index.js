/**
 * 查询构建器组件集合
 * 统一导出所有相关组件
 */

// 导入组件
import './query-builder-page.js';
import './query-workspace.js';
import './query-template.js';
import './parameter-input.js';
import './condition-builder.js';
import './join-panel.js';
import './query-history.js';
import './query-plan.js';
import './results-preview.js';
import './sql-preview.js';

// 导入样式
import '../css/query-builder.css';
import '../css/query-workspace.css';
import '../css/query-template.css';
import '../css/parameter-input.css';
import '../css/condition-builder.css';
import '../css/join-panel.css';
import '../css/query-history.css';
import '../css/query-plan.css';
import '../css/results-preview.css';
import '../css/sql-preview.css';
import '../css/sql-highlight.css';

/**
 * 注册全局过滤器
 */
Vue.filter('formatDateTime', (value) => {
    if (!value) return '';
    return new Date(value).toLocaleString();
});

Vue.filter('truncate', (value, length = 50) => {
    if (!value) return '';
    if (value.length <= length) return value;
    return value.substring(0, length) + '...';
});

Vue.filter('highlight', (value, search) => {
    if (!value || !search) return value;
    const regex = new RegExp(`(${search})`, 'gi');
    return value.replace(regex, '<mark>$1</mark>');
});

/**
 * 注册全局指令
 */
Vue.directive('sql-highlight', {
    inserted(el, binding) {
        if (binding.value) {
            el.innerHTML = SqlFormatter.highlight(binding.value);
        }
    },
    update(el, binding) {
        if (binding.value !== binding.oldValue) {
            el.innerHTML = SqlFormatter.highlight(binding.value);
        }
    }
});

Vue.directive('click-outside', {
    bind(el, binding) {
        el.clickOutsideEvent = function(event) {
            if (!(el === event.target || el.contains(event.target))) {
                binding.value(event);
            }
        };
        document.addEventListener('click', el.clickOutsideEvent);
    },
    unbind(el) {
        document.removeEventListener('click', el.clickOutsideEvent);
    }
});

/**
 * 注册全局混入
 */
Vue.mixin({
    methods: {
        /**
         * 复制文本到剪贴板
         */
        async copyToClipboard(text) {
            try {
                await navigator.clipboard.writeText(text);
                this.$message.success('复制成功');
            } catch (error) {
                console.error('复制失败:', error);
                this.$message.error('复制失败');
            }
        },

        /**
         * 下载文件
         */
        async downloadFile(url, filename) {
            try {
                const response = await fetch(url);
                const blob = await response.blob();
                const link = document.createElement('a');
                link.href = URL.createObjectURL(blob);
                link.download = filename;
                link.click();
                URL.revokeObjectURL(link.href);
            } catch (error) {
                console.error('下载失败:', error);
                this.$message.error('下载失败');
            }
        }
    }
});