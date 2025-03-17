module.exports = {
    moduleFileExtensions: ['js', 'json', 'vue'],
    transform: {
        '^.+\\.js$': 'babel-jest',
        '^.+\\.vue$': 'vue-jest'
    },
    moduleNameMapper: {
        '^@/(.*)$': '<rootDir>/src/main/resources/static/js/$1'
    },
    testMatch: [
        '**/src/test/javascript/**/*.spec.js'
    ],
    setupFiles: ['<rootDir>/src/test/javascript/setup.js'],
    collectCoverage: true,
    collectCoverageFrom: [
        'src/main/resources/static/js/**/*.{js,vue}',
        '!src/main/resources/static/js/vendor/**'
    ],
    coverageReporters: ['text', 'html'],
    coverageDirectory: 'target/test-coverage',
    testEnvironment: 'jsdom',
    globals: {
        Vue: require('vue'),
        moment: require('moment')
    },
    verbose: true
};