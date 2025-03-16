/**
 * 前端路由配置
 */
const router = new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/dashboard',
            component: {
                template: '<dashboard></dashboard>'
            }
        },
        {
            path: '/datasource/list',
            component: {
                template: '<datasource-list></datasource-list>'
            }
        },
        {
            path: '/datasource/add',
            component: {
                template: '<datasource-form></datasource-form>'
            }
        },
        {
            path: '/datasource/edit/:id',
            component: {
                template: '<datasource-form :data-source-id="$route.params.id"></datasource-form>'
            },
            props: true
        },
        {
            path: '/datasource/view/:id',
            component: {
                template: '<datasource-form :data-source-id="$route.params.id" :readonly="true"></datasource-form>'
            },
            props: true
        },
        {
            path: '/datasource/:id',
            component: {
                template: '<datasource-detail :data-source-id="$route.params.id"></datasource-detail>'
            },
            props: true
        },
        {
            path: '/datasource/:dataSourceId/schema/:schemaName/table/:tableName',
            component: {
                template: '<table-detail :data-source-id="$route.params.dataSourceId" :schema-name="$route.params.schemaName" :table-name="$route.params.tableName"></table-detail>'
            },
            props: true
        },
        {
            path: '/query-builder',
            component: {
                template: '<query-builder :data-source-id="$route.query.dataSourceId" :schema="$route.query.schema" :table="$route.query.table"></query-builder>'
            }
        },
        {
            path: '/nl-query',
            component: {
                template: '<nl-query></nl-query>'
            }
        },
        {
            path: '/low-code',
            component: {
                template: '<low-code></low-code>'
            }
        },
        {
            path: '/settings',
            component: {
                template: '<settings></settings>'
            }
        },
        {
            path: '/table-relationships',
            component: {
                template: '<table-relationship-page></table-relationship-page>'
            }
        },
        {
            path: '/datasource/:dataSourceId/table-relationships',
            component: {
                template: '<table-relationship-page :data-source-id="$route.params.dataSourceId"></table-relationship-page>'
            },
            props: true
        },
        {
            path: '*',
            redirect: '/dashboard'
        }
    ]
});
