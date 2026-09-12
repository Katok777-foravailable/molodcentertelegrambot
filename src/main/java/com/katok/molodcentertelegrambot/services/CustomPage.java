package com.katok.molodcentertelegrambot.services;

import lombok.Data;

import java.util.List;

@Data
public class CustomPage<T> {
    private List<T> content;
    private int number;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
