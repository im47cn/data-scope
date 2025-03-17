import Vue from 'vue';
import Antd from 'ant-design-vue';
import moment from 'moment';

// Configure Vue
Vue.config.productionTip = false;
Vue.use(Antd);

// Add globals that components expect
global.moment = moment;

// Mock router and message globally
Vue.prototype.$router = {
    push: jest.fn(),
    replace: jest.fn(),
    back: jest.fn()
};

Vue.prototype.$message = {
    success: jest.fn(),
    error: jest.fn(),
    warning: jest.fn(),
    info: jest.fn(),
    loading: jest.fn(),
    destroy: jest.fn()
};

Vue.prototype.$confirm = jest.fn(({ onOk }) => onOk && onOk());

Vue.prototype.$notification = {
    success: jest.fn(),
    error: jest.fn(),
    warning: jest.fn(),
    info: jest.fn(),
    open: jest.fn(),
    close: jest.fn(),
    destroy: jest.fn()
};

// Mock window methods
global.scrollTo = jest.fn();

// Mock Ant Design components globally
const antComponents = [
    'a-button',
    'a-icon',
    'a-input',
    'a-input-search',
    'a-input-password',
    'a-input-number',
    'a-select',
    'a-select-option',
    'a-table',
    'a-tabs',
    'a-tab-pane',
    'a-form',
    'a-form-item',
    'a-textarea',
    'a-switch',
    'a-space',
    'a-tag',
    'a-badge',
    'a-tooltip',
    'a-spin',
    'a-card',
    'a-descriptions',
    'a-descriptions-item',
    'a-statistic',
    'a-row',
    'a-col',
    'a-tree',
    'a-empty'
];

antComponents.forEach(component => {
    Vue.component(component, {
        template: `<div class="${component}"><slot></slot></div>`,
        props: {
            value: [String, Number, Boolean, Array, Object],
            loading: Boolean,
            spinning: Boolean,
            pagination: Object,
            columns: Array,
            dataSource: Array,
            rowKey: [String, Function],
            treeData: Array
        },
        methods: {
            focus: jest.fn(),
            blur: jest.fn()
        }
    });
});

// Add a global error handler
global.console.error = jest.fn();

// Add ResizeObserver mock
global.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn()
}));