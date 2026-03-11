package com.example.restaurant.exceptionhandling;

public class EmployeeNotFoundException extends RuntimeException {
    private final Long employeeId;

    public EmployeeNotFoundException(Long id) {
        this.employeeId = id;
    }
    public Long getEmployeeId() {
        return employeeId;
    }
}
