/**
 * 路由配置
 */

const routes = [
    {
        path: '/',
        redirect: '/datasource'
    },
    {
        path: '/datasource',
        component: () => ({
            component: new Promise(resolve => {
                import('./datasource/datasource-list.js').then(() => {
                    resolve(Vue.component('datasource-list'));
                });
            })
        }),
        children: [
            {
                path: 'add',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./datasource/datasource-form.js').then(() => {
                            resolve(Vue.component('datasource-form'));
                        });
                    })
                })
            },
            {
                path: 'edit/:id',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./datasource/datasource-form.js').then(() => {
                            resolve(Vue.component('datasource-form'));
                        });
                    })
                })
            },
            {
                path: 'view/:id',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./datasource/datasource-detail.js').then(() => {
                            resolve(Vue.component('datasource-detail'));
                        });
                    })
                })
            }
        ]
    },
    {
        path: '/query',
        component: () => ({
            component: new Promise(resolve => {
                import('./query-builder/query-builder-page.js').then(() => {
                    resolve(Vue.component('query-builder-page'));
                });
            })
        }),
        children: [
            {
                path: ':dataSourceId',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./query-builder/query-workspace.js').then(() => {
                            resolve(Vue.component('query-workspace'));
                        });
                    })
                })
            }
        ]
    },
    {
        path: '/nlquery',
        component: () => ({
            component: new Promise(resolve => {
                import('./nl-query/nl-query.js').then(() => {
                    resolve(Vue.component('nl-query'));
                });
            })
        })
    },
    {
        path: '/dashboard',
        component: () => ({
            component: new Promise(resolve => {
                import('./dashboard/dashboard-page.js').then(() => {
                    resolve(Vue.component('dashboard-page'));
                });
            })
        }),
        children: [
            {
                path: 'create',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./dashboard/dashboard-editor.js').then(() => {
                            resolve(Vue.component('dashboard-editor'));
                        });
                    })
                })
            },
            {
                path: 'edit/:id',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./dashboard/dashboard-editor.js').then(() => {
                            resolve(Vue.component('dashboard-editor'));
                        });
                    })
                })
            },
            {
                path: 'view/:id',
                component: () => ({
                    component: new Promise(resolve => {
                        import('./dashboard/dashboard-viewer.js').then(() => {
                            resolve(Vue.component('dashboard-viewer'));
                        });
                    })
                })
            }
        ]
    }
];

// 创建路由实例
const router = new VueRouter({
    mode: 'history',
    base: '/',
    routes
});

// 全局前置守卫
router.beforeEach((to, from, next) => {
    // 设置页面标题
    const title = to.meta.title || 'InsightData';
    document.title = title;

    // 检查权限
    const requiresAuth = to.matched.some(record => record.meta.requiresAuth);
    if (requiresAuth && !isAuthenticated()) {
        next({
            path: '/login',
            query: { redirect: to.fullPath }
        });
    } else {
        next();
    }
});

// 全局后置钩子
router.afterEach((to, from) => {
    // 滚动到顶部
    window.scrollTo(0, 0);
    
    // 记录访问历史
    const excludePaths = ['/login', '/404', '/500'];
    if (!excludePaths.includes(to.path)) {
        const history = JSON.parse(localStorage.getItem('navigationHistory') || '[]');
        history.unshift({
            path: to.path,
            name: to.meta.title || to.name || to.path,
            timestamp: Date.now()
        });
        // 只保留最近的10条记录
        history.splice(10);
        localStorage.setItem('navigationHistory', JSON.stringify(history));
    }
});

/**
 * 检查用户是否已认证
 * @returns {boolean}
 */
function isAuthenticated() {
    const token = localStorage.getItem('token');
    return !!token;
}

export default router;
