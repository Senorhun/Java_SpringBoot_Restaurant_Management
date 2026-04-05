package com.example.restaurant.exceptionhandling;

public class TableNumberDuplicationException extends RuntimeException {
    private final Integer number;

    public TableNumberDuplicationException(Integer number) {
        this.number = number;
    }

    public Integer getNumber() {
        return number;
    }
}
