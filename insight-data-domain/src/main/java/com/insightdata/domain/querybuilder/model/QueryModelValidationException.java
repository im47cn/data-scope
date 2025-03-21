package com.insightdata.domain.querybuilder.model;

/**
 * Exception thrown when a query model fails validation
 */
public class QueryModelValidationException extends Exception {
    public QueryModelValidationException(String message) {
        super(message);
    }

    public QueryModelValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}