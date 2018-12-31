package com.lixiaocong.cms.utils;

import java.util.LinkedList;
import java.util.List;

public class PageInfo<T> {
    public long totalItems;
    public long totalPages;
    public List<T> items;

    public PageInfo() {
        this.items = new LinkedList<T>();
        this.totalItems = 0;
        this.totalPages = 0;
    }

    public PageInfo(long totalItems, long totalPages, List<T> items) {
        this.totalItems = totalItems;
        this.totalPages = totalPages;
        this.items = items;
    }
}
