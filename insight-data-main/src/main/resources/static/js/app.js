/**
 * 主应用程序
 */
const app = new Vue({
    router,
    data: {
        collapsed: false,
        menuItems: [
            {
                key: 'dashboard',
                icon: 'dashboard',
                title: '仪表盘',
                path: '/dashboard'
            },
            {
                key: 'datasource',
                icon: 'database',
                title: '数据源管理',
                path: '/datasource/list'
            },
            {
                key: 'query-builder',
                icon: 'build',
                title: '查询构建器',
                path: '/query-builder'
            },
            {
                key: 'nl-query',
                icon: 'robot',
                title: '自然语言查询',
                path: '/nl-query'
            },
            {
                key: 'low-code',
                icon: 'code',
                title: '低代码集成',
                path: '/low-code'
            },
            {
                key: 'table-relationships',
                icon: 'apartment',
                title: '表关系管理',
                path: '/table-relationships'
            },
            {
                key: 'settings',
                icon: 'setting',
                title: '系统设置',
                path: '/settings'
            }
        ]
    },
    computed: {
        activeMenuKey() {
            const path = this.$route.path;
            
            // 根据路径确定激活的菜单项
            if (path.startsWith('/dashboard')) {
                return 'dashboard';
            } else if (path.startsWith('/datasource')) {
                return 'datasource';
            } else if (path.startsWith('/query-builder')) {
                return 'query-builder';
            } else if (path.startsWith('/nl-query')) {
                return 'nl-query';
            } else if (path.startsWith('/low-code')) {
                return 'low-code';
            } else if (path.startsWith('/table-relationships')) {
                return 'table-relationships';
            } else if (path.startsWith('/settings')) {
                return 'settings';
            }
            
            return 'dashboard';
        }
    },
    methods: {
        toggleCollapsed() {
            this.collapsed = !this.collapsed;
        },
        handleMenuClick(item) {
            this.$router.push(item.path);
        }
    },
    template: `
        <a-layout id="app" style="min-height: 100vh">
            <a-layout-sider
                v-model="collapsed"
                collapsible
                breakpoint="lg"
                theme="dark"
            >
                <div class="logo">
                    <img src="/static/img/logo.png" alt="Logo" />
                    <h1 v-if="!collapsed">DataScope</h1>
                </div>
                <a-menu
                    theme="dark"
                    mode="inline"
                    :selectedKeys="[activeMenuKey]"
                >
                    <a-menu-item
                        v-for="item in menuItems"
                        :key="item.key"
                        @click="handleMenuClick(item)"
                    >
                        <a-icon :type="item.icon" />
                        <span>{{ item.title }}</span>
                    </a-menu-item>
                </a-menu>
            </a-layout-sider>
            
            <a-layout>
                <a-layout-header style="background: #fff; padding: 0">
                    <a-icon
                        class="trigger"
                        :type="collapsed ? 'menu-unfold' : 'menu-fold'"
                        @click="toggleCollapsed"
                    />
                </a-layout-header>
                
                <a-layout-content style="margin: 24px 16px 0">
                    <div class="content-container">
                        <router-view></router-view>
                    </div>
                </a-layout-content>
                
                <a-layout-footer style="text-align: center">
                    DataScope &copy; 2025 Created by YeeWorks
                </a-layout-footer>
            </a-layout>
        </a-layout>
    `
}).$mount('#app');
