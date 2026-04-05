package com.example.restaurant.exceptionhandling;

public class TableNotFoundException extends RuntimeException{
    private final Long tableId;
    public TableNotFoundException(long id) {
        this.tableId = id;
    }
    public Long getTableId() {
        return tableId;
    }
}
