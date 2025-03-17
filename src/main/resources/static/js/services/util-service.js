/**
 * 工具服务
 * 提供共享的工具方法
 */
const UtilService = {
    /**
     * 格式化日期时间
     * @param {string|Date} date 日期时间
     * @param {string} format 格式化模式，默认 YYYY-MM-DD HH:mm:ss
     * @returns {string} 格式化后的日期时间字符串
     */
    formatDateTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
        if (!date) return '-';
        return moment(date).format(format);
    },

    /**
     * 计算时间差
     * @param {string|Date} startTime 开始时间
     * @param {string|Date} endTime 结束时间
     * @returns {string} 格式化后的时间差
     */
    formatDuration(startTime, endTime) {
        if (!startTime || !endTime) return '-';
        const duration = moment.duration(moment(endTime).diff(moment(startTime)));
        return this.formatDurationFromMillis(duration.asMilliseconds());
    },

    /**
     * 将毫秒数格式化为时间差字符串
     * @param {number} milliseconds 毫秒数
     * @returns {string} 格式化后的时间差
     */
    formatDurationFromMillis(milliseconds) {
        if (milliseconds < 0) return '-';
        
        const duration = moment.duration(milliseconds);
        const parts = [];

        if (duration.days() > 0) {
            parts.push(`${duration.days()}天`);
        }
        if (duration.hours() > 0) {
            parts.push(`${duration.hours()}小时`);
        }
        if (duration.minutes() > 0) {
            parts.push(`${duration.minutes()}分`);
        }
        if (duration.seconds() > 0 || parts.length === 0) {
            parts.push(`${duration.seconds()}秒`);
        }

        return parts.join('');
    },

    /**
     * 文件大小格式化
     * @param {number} bytes 字节数
     * @returns {string} 格式化后的文件大小
     */
    formatFileSize(bytes) {
        if (bytes === 0) return '0 B';
        const k = 1024;
        const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return `${parseFloat((bytes / Math.pow(k, i)).toFixed(2))} ${sizes[i]}`;
    },

    /**
     * 生成唯一ID
     * @returns {string} 唯一ID
     */
    generateUniqueId() {
        return 'id-' + Math.random().toString(36).substr(2, 9);
    },

    /**
     * 深拷贝对象
     * @param {Object} obj 要拷贝的对象
     * @returns {Object} 拷贝后的对象
     */
    deepClone(obj) {
        if (obj === null || typeof obj !== 'object') return obj;
        if (obj instanceof Date) return new Date(obj);
        if (obj instanceof Array) return obj.map(item => this.deepClone(item));
        if (obj instanceof Object) {
            const copy = {};
            Object.keys(obj).forEach(key => {
                copy[key] = this.deepClone(obj[key]);
            });
            return copy;
        }
        return obj;
    },

    /**
     * 防抖函数
     * @param {Function} fn 要执行的函数
     * @param {number} delay 延迟时间（毫秒）
     * @returns {Function} 防抖后的函数
     */
    debounce(fn, delay) {
        let timer = null;
        return function(...args) {
            if (timer) clearTimeout(timer);
            timer = setTimeout(() => {
                fn.apply(this, args);
            }, delay);
        };
    },

    /**
     * 节流函数
     * @param {Function} fn 要执行的函数
     * @param {number} limit 时间限制（毫秒）
     * @returns {Function} 节流后的函数
     */
    throttle(fn, limit) {
        let inThrottle;
        return function(...args) {
            if (!inThrottle) {
                fn.apply(this, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    },

    /**
     * 下载文件
     * @param {string} url 文件URL
     * @param {string} filename 文件名
     */
    downloadFile(url, filename) {
        const link = document.createElement('a');
        link.href = url;
        link.download = filename;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    },

    /**
     * 复制文本到剪贴板
     * @param {string} text 要复制的文本
     * @returns {Promise} 复制结果
     */
    async copyToClipboard(text) {
        try {
            await navigator.clipboard.writeText(text);
            return true;
        } catch (error) {
            console.error('复制到剪贴板失败:', error);
            return false;
        }
    },

    /**
     * 格式化SQL语句
     * @param {string} sql SQL语句
     * @returns {string} 格式化后的SQL语句
     */
    formatSQL(sql) {
        if (!sql) return '';
        // 这里可以使用SQL格式化库，如sql-formatter
        // 当前使用简单的格式化
        return sql
            .replace(/\s+/g, ' ')
            .replace(/ (SELECT|FROM|WHERE|GROUP BY|HAVING|ORDER BY|LIMIT|INSERT|UPDATE|DELETE) /gi, '\n$1 ')
            .replace(/ (INNER|LEFT|RIGHT|OUTER|CROSS) JOIN /gi, '\n  $1 JOIN ')
            .replace(/ ON /gi, '\n    ON ')
            .replace(/ AND /gi, '\n    AND ')
            .replace(/ OR /gi, '\n    OR ')
            .trim();
    }
};

export default UtilService;