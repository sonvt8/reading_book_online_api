package com.cyber.online_books.entity;

import lombok.Data;

@Data
public class PageInfo {
    private int number;
    private long totalElements;
    private int totalPages;
    private int size;

    public PageInfo(int number, long totalElements, int totalPages, int size) {
        this.number = number;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.size = size;
    }
}