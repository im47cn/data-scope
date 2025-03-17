/**
 * Jest测试环境配置
 */

// 配置Vue
const Vue = require('vue');
const VueRouter = require('vue-router');
const Antd = require('ant-design-vue');

// 安装Vue插件
Vue.use(VueRouter);
Vue.use(Antd);

// 配置CodeMirror
global.CodeMirror = require('codemirror');
require('codemirror/mode/sql/sql');
require('codemirror/addon/hint/show-hint');
require('codemirror/addon/hint/sql-hint');

// Mock浏览器API
global.document.createRange = () => ({
    setStart: () => {},
    setEnd: () => {},
    commonAncestorContainer: {
        nodeName: 'BODY',
        ownerDocument: document
    }
});

// Mock Clipboard API
Object.assign(navigator, {
    clipboard: {
        writeText: jest.fn().mockResolvedValue(undefined),
        readText: jest.fn().mockResolvedValue('')
    }
});

// Mock 本地存储
const localStorageMock = {
    getItem: jest.fn(),
    setItem: jest.fn(),
    removeItem: jest.fn(),
    clear: jest.fn()
};
global.localStorage = localStorageMock;

// Mock Fetch API
global.fetch = jest.fn().mockImplementation(() =>
    Promise.resolve({
        ok: true,
        json: () => Promise.resolve({}),
        blob: () => Promise.resolve(new Blob()),
        text: () => Promise.resolve('')
    })
);

// Mock Console方法
global.console = {
    ...console,
    error: jest.fn(),
    warn: jest.fn(),
    log: jest.fn(),
    info: jest.fn(),
    debug: jest.fn()
};

// Mock ResizeObserver
global.ResizeObserver = class ResizeObserver {
    constructor(callback) {
        this.callback = callback;
    }
    observe() {}
    unobserve() {}
    disconnect() {}
};

// Mock IntersectionObserver
global.IntersectionObserver = class IntersectionObserver {
    constructor(callback) {
        this.callback = callback;
    }
    observe() {}
    unobserve() {}
    disconnect() {}
};

// 配置Jest匹配器
expect.extend({
    toBeValidSql(received) {
        try {
            // 简单的SQL语法检查
            const hasSelect = /SELECT/i.test(received);
            const hasFrom = /FROM/i.test(received);
            const hasValidEnd = /;?\s*$/.test(received);
            
            return {
                pass: hasSelect && hasFrom && hasValidEnd,
                message: () => 'Expected to be valid SQL statement'
            };
        } catch (error) {
            return {
                pass: false,
                message: () => `SQL validation failed: ${error.message}`
            };
        }
    }
});

// 配置Vue Test Utils
const { config } = require('@vue/test-utils');

config.mocks.$message = {
    success: jest.fn(),
    error: jest.fn(),
    warning: jest.fn(),
    info: jest.fn()
};

config.mocks.$confirm = jest.fn().mockImplementation(
    (options) => new Promise((resolve) => resolve(options.onOk()))
);

config.mocks.$notification = {
    success: jest.fn(),
    error: jest.fn(),
    warning: jest.fn(),
    info: jest.fn()
};

// 配置全局工具函数
global.sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

global.createWrapper = (component, options = {}) => {
    return mount(component, {
        localVue: Vue,
        router: new VueRouter(),
        ...options
    });
};

// 配置全局错误处理
window.onerror = (msg, url, line, col, error) => {
    console.error('Global error:', { msg, url, line, col, error });
};

// 配置异步操作超时
jest.setTimeout(10000);

// 配置快照序列化
expect.addSnapshotSerializer({
    test: (val) => val && val.nodeType === 1,
    print: (val) => val.outerHTML
});

// 清理函数
afterEach(() => {
    // 清理所有计时器
    jest.clearAllTimers();
    
    // 清理所有Mock
    jest.clearAllMocks();
    
    // 清理localStorage
    localStorage.clear();
    
    // 清理文档body
    document.body.innerHTML = '';
});

// 配置Moment.js
const moment = require('moment');
moment.locale('zh-cn');

// 导出工具函数
module.exports = {
    sleep,
    createWrapper
};