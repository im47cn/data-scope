package com.insightdata.common.exception;

/**
 * 查询超时异常
 * 当查询执行时间超过配置的超时时间时抛出此异常
 */
public class QueryTimeoutException extends InsightDataException {

    private static final long serialVersionUID = 1L;

    public QueryTimeoutException(String message) {
        super(message);
    }

    public QueryTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}