/**
 * 元数据同步状态徽章组件
 * 用于显示同步状态和进度
 */
const SyncStatusBadge = {
    props: {
        // 同步状态对象
        status: {
            type: Object,
            required: true
        },
        // 是否显示进度
        showProgress: {
            type: Boolean,
            default: false
        },
        // 是否显示详情
        showDetail: {
            type: Boolean,
            default: false
        }
    },

    computed: {
        statusColor() {
            const map = {
                'PENDING': 'orange',
                'RUNNING': 'blue',
                'COMPLETED': 'green',
                'FAILED': 'red',
                'CANCELLED': 'grey',
                'UNKNOWN': 'default'
            };
            return map[this.status.status] || 'default';
        },

        statusIcon() {
            const map = {
                'PENDING': 'clock-circle',
                'RUNNING': 'sync',
                'COMPLETED': 'check-circle',
                'FAILED': 'close-circle',
                'CANCELLED': 'stop',
                'UNKNOWN': 'question-circle'
            };
            return map[this.status.status] || 'question-circle';
        },

        isInProgress() {
            return ['PENDING', 'RUNNING'].includes(this.status.status);
        },

        progressPercent() {
            return Math.round(this.status.progress || 0);
        },

        duration() {
            if (!this.status.startTime) return '';
            const start = moment(this.status.startTime);
            const end = this.status.endTime ? moment(this.status.endTime) : moment();
            const duration = moment.duration(end.diff(start));
            return `${duration.minutes()}分${duration.seconds()}秒`;
        },

        detail() {
            if (!this.status) return '';
            
            const parts = [];
            if (this.status.syncedTables) {
                parts.push(`已同步表：${this.status.syncedTables}/${this.status.totalTables}`);
            }
            if (this.status.syncedSchemas) {
                parts.push(`已同步模式：${this.status.syncedSchemas}/${this.status.totalSchemas}`);
            }
            if (this.duration) {
                parts.push(`耗时：${this.duration}`);
            }
            return parts.join(' | ');
        }
    },

    template: `
        <div class="sync-status-badge">
            <a-tooltip :title="status.message">
                <a-badge :status="statusColor">
                    <template v-if="showDetail">
                        <span>
                            <a-icon 
                                :type="statusIcon" 
                                :spin="isInProgress"
                                :style="{ marginRight: '8px' }"
                            />
                            {{ status.status }}
                            <span v-if="detail" style="margin-left: 8px; font-size: 12px; color: #999">
                                {{ detail }}
                            </span>
                        </span>
                    </template>
                    <template v-else>
                        <a-icon 
                            :type="statusIcon" 
                            :spin="isInProgress"
                        />
                    </template>
                </a-badge>
            </a-tooltip>
            <a-progress
                v-if="showProgress && isInProgress"
                :percent="progressPercent"
                :status="status.status === 'FAILED' ? 'exception' : 'active'"
                :showInfo="false"
                size="small"
                style="margin-top: 4px"
            />
        </div>
    `
};

// 注册组件
Vue.component('sync-status-badge', SyncStatusBadge);