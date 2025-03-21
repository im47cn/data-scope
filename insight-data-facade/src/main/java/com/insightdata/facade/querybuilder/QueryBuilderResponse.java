package com.insightdata.facade.querybuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response wrapper for QueryBuilder API
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryBuilderResponse<T> {
    
    /**
     * Response status code
     */
    private int code;
    
    /**
     * Response message
     */
    private String message;
    
    /**
     * Response data
     */
    private T data;
    
    /**
     * Error details if any
     */
    private Object errors;
    
    /**
     * Create a successful response
     */
    public static <T> QueryBuilderResponse<T> success(T data) {
        QueryBuilderResponse<T> response = new QueryBuilderResponse<>();
        response.code = 200;
        response.message = "Success";
        response.data = data;
        return response;
    }
    
    /**
     * Create an error response
     */
    public static <T> QueryBuilderResponse<T> error(int code, String message, Object errors) {
        QueryBuilderResponse<T> response = new QueryBuilderResponse<>();
        response.code = code;
        response.message = message;
        response.errors = errors;
        return response;
    }
    
    /**
     * Create a validation error response
     */
    public static <T> QueryBuilderResponse<T> validationError(Object errors) {
        return error(400, "Validation Error", errors);
    }
    
    /**
     * Create a not found response
     */
    public static <T> QueryBuilderResponse<T> notFound(String message) {
        return error(404, message, null);
    }
    
    /**
     * Create a server error response
     */
    public static <T> QueryBuilderResponse<T> serverError(String message) {
        return error(500, message, null);
    }
}