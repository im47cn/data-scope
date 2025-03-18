/**
 * Jest configuration for testing frontend components
 */
module.exports = {
    // 测试环境
    testEnvironment: 'jsdom',
    
    // 测试文件匹配模式
    testMatch: [
        '<rootDir>/src/test/javascript/**/*.spec.js',
        '<rootDir>/src/test/javascript/**/*.test.js'
    ],
    
    // 模块文件扩展名
    moduleFileExtensions: ['js', 'json', 'vue'],
    
    // 转换器配置
    transform: {
        '^.+\\.js$': 'babel-jest',
        '^.+\\.vue$': '@vue/vue2-jest'
    },
    
    // 模块名映射
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/main/resources/static/js/$1',
        '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
        '\\.(jpg|jpeg|png|gif|svg)$': '<rootDir>/src/test/javascript/__mocks__/fileMock.js'
    },
    
    // 测试覆盖率配置
    collectCoverage: true,
    collectCoverageFrom: [
        'src/main/resources/static/js/**/*.{js,vue}',
        '!src/main/resources/static/js/vendor/**',
        '!**/node_modules/**'
    ],
    coverageReporters: ['text', 'lcov', 'html'],
    coverageDirectory: 'target/test-coverage',
    
    // 测试设置文件
    setupFiles: [
        '<rootDir>/src/test/javascript/setup.js'
    ],
    
    // 快照序列化器
    snapshotSerializers: [
        'jest-serializer-vue'
    ],
    
    // 全局变量
    globals: {
        Vue: require('vue'),
        axios: require('axios'),
        moment: require('moment')
    },
    
    // 测试超时设置
    testTimeout: 10000,
    
    // 并行执行测试
    maxConcurrency: 4,
    
    // 错误报告配置
    verbose: true,
    bail: false,
    
    // 监听配置
    watchPathIgnorePatterns: [
        '<rootDir>/node_modules/',
        '<rootDir>/target/'
    ],
    
    // 缓存配置
    cacheDirectory: '<rootDir>/target/jest-cache',
    
    // 自定义报告器
    reporters: [
        'default',
        [
            'jest-junit',
            {
                outputDirectory: 'target/test-results/jest',
                outputName: 'junit.xml',
                classNameTemplate: '{filepath}',
                titleTemplate: '{title}',
                ancestorSeparator: ' › ',
                suiteNameTemplate: '{filepath}',
                usePathForSuiteName: true
            }
        ]
    ]
};