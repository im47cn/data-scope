package com.insightdata.facade.common;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Generic paged response
 * @param <T> Type of items in the page
 */
@Getter
@Setter
public class PageResponse<T> {
    private List<T> items = new ArrayList<>();
    private long total;
    private int page;
    private int size;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public PageResponse() {
    }
    
    public PageResponse(List<T> items, long total, int page, int size, int totalPages, boolean hasNext, boolean hasPrevious) {
        this.items = items != null ? items : new ArrayList<>();
        this.total = total;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
        this.hasPrevious = hasPrevious;
    }
    
    public static <T> PageResponse<T> empty() {
        return new PageResponse<>(new ArrayList<>(), 0L, 0, 0, 0, false, false);
    }
    
    public static <T> PageResponse<T> of(List<T> items, long total, int page, int size) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);
        boolean hasNext = page < totalPages - 1;
        boolean hasPrevious = page > 0;
        return new PageResponse<>(items, total, page, size, totalPages, hasNext, hasPrevious);
    }
}