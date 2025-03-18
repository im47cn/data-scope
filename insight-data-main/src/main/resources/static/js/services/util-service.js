/**
 * 工具服务
 * 提供通用的工具函数
 */
const UtilService = {
    /**
     * 防抖函数
     * @param {Function} func 要执行的函数
     * @param {number} wait 等待时间（毫秒）
     * @param {boolean} immediate 是否立即执行
     * @returns {Function} 防抖处理后的函数
     */
    debounce(func, wait = 300, immediate = false) {
        let timeout;
        return function executedFunction(...args) {
            const context = this;
            const later = () => {
                timeout = null;
                if (!immediate) func.apply(context, args);
            };
            const callNow = immediate && !timeout;
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
            if (callNow) func.apply(context, args);
        };
    },

    /**
     * 节流函数
     * @param {Function} func 要执行的函数
     * @param {number} limit 限制时间（毫秒）
     * @returns {Function} 节流处理后的函数
     */
    throttle(func, limit = 300) {
        let inThrottle;
        return function executedFunction(...args) {
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => (inThrottle = false), limit);
            }
        };
    },

    /**
     * 深拷贝对象
     * @param {*} obj 要拷贝的对象
     * @returns {*} 拷贝后的对象
     */
    deepClone(obj) {
        if (obj === null || typeof obj !== 'object') {
            return obj;
        }

        if (obj instanceof Date) {
            return new Date(obj.getTime());
        }

        if (obj instanceof Array) {
            return obj.map(item => this.deepClone(item));
        }

        if (obj instanceof Object) {
            const copy = {};
            Object.keys(obj).forEach(key => {
                copy[key] = this.deepClone(obj[key]);
            });
            return copy;
        }

        throw new Error('无法复制对象');
    },

    /**
     * 格式化日期时间
     * @param {Date|string|number} date 日期对象、时间戳或日期字符串
     * @param {string} format 格式化模板
     * @returns {string} 格式化后的日期字符串
     */
    formatDateTime(date, format = 'YYYY-MM-DD HH:mm:ss') {
        if (!date) return '';
        
        const d = new Date(date);
        if (isNaN(d.getTime())) return '';

        const pad = (num) => String(num).padStart(2, '0');
        
        const replacements = {
            'YYYY': d.getFullYear(),
            'MM': pad(d.getMonth() + 1),
            'DD': pad(d.getDate()),
            'HH': pad(d.getHours()),
            'mm': pad(d.getMinutes()),
            'ss': pad(d.getSeconds()),
            'SSS': String(d.getMilliseconds()).padStart(3, '0')
        };

        return format.replace(/YYYY|MM|DD|HH|mm|ss|SSS/g, match => replacements[match]);
    },

    /**
     * 复制文本到剪贴板
     * @param {string} text 要复制的文本
     * @returns {Promise} Promise对象
     */
    async copyToClipboard(text) {
        try {
            await navigator.clipboard.writeText(text);
            return true;
        } catch (err) {
            // 降级处理
            const textarea = document.createElement('textarea');
            textarea.value = text;
            textarea.style.position = 'fixed';
            textarea.style.opacity = '0';
            document.body.appendChild(textarea);
            textarea.select();
            try {
                document.execCommand('copy');
                return true;
            } catch (e) {
                console.error('复制失败:', e);
                return false;
            } finally {
                document.body.removeChild(textarea);
            }
        }
    },

    /**
     * 下载文件
     * @param {string} url 文件URL
     * @param {string} filename 文件名
     * @returns {Promise}
     */
    async downloadFile(url, filename) {
        try {
            const response = await fetch(url);
            if (!response.ok) {
                throw new Error('下载失败');
            }
            const blob = await response.blob();
            const downloadUrl = window.URL.createObjectURL(blob);
            const link = document.createElement('a');
            link.href = downloadUrl;
            link.download = filename;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            window.URL.revokeObjectURL(downloadUrl);
            return true;
        } catch (error) {
            console.error('下载文件失败:', error);
            throw error;
        }
    },

    /**
     * 格式化文件大小
     * @param {number} bytes 字节数
     * @returns {string} 格式化后的大小
     */
    formatFileSize(bytes) {
        if (bytes === 0) return '0 B';
        const k = 1024;
        const sizes = ['B', 'KB', 'MB', 'GB', 'TB'];
        const i = Math.floor(Math.log(bytes) / Math.log(k));
        return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    /**
     * 生成唯一ID
     * @returns {string} 唯一ID
     */
    generateUniqueId() {
        return Date.now().toString(36) + Math.random().toString(36).substring(2);
    },

    /**
     * 计算字符串相似度（使用Levenshtein距离）
     * @param {string} str1 第一个字符串
     * @param {string} str2 第二个字符串
     * @returns {number} 相似度（0-1之间）
     */
    stringSimilarity(str1, str2) {
        if (str1 === str2) return 1;
        if (!str1 || !str2) return 0;

        const len1 = str1.length;
        const len2 = str2.length;
        const matrix = Array(len1 + 1).fill(null).map(() => Array(len2 + 1).fill(null));

        for (let i = 0; i <= len1; i++) matrix[i][0] = i;
        for (let j = 0; j <= len2; j++) matrix[0][j] = j;

        for (let i = 1; i <= len1; i++) {
            for (let j = 1; j <= len2; j++) {
                const cost = str1[i - 1] === str2[j - 1] ? 0 : 1;
                matrix[i][j] = Math.min(
                    matrix[i - 1][j] + 1,
                    matrix[i][j - 1] + 1,
                    matrix[i - 1][j - 1] + cost
                );
            }
        }

        const distance = matrix[len1][len2];
        const maxLength = Math.max(len1, len2);
        return 1 - distance / maxLength;
    },

    /**
     * 解析URL查询参数
     * @param {string} url URL字符串
     * @returns {Object} 参数对象
     */
    parseQueryParams(url) {
        const params = {};
        const searchParams = new URLSearchParams(
            url.includes('?') ? url.split('?')[1] : url
        );
        for (const [key, value] of searchParams) {
            params[key] = value;
        }
        return params;
    },

    /**
     * 构建URL查询字符串
     * @param {Object} params 参数对象
     * @returns {string} 查询字符串
     */
    buildQueryString(params) {
        return Object.entries(params)
            .filter(([, value]) => value != null)
            .map(([key, value]) => `${encodeURIComponent(key)}=${encodeURIComponent(value)}`)
            .join('&');
    }
};

export default UtilService;