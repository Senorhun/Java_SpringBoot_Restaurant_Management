package com.example.restaurant.exceptionhandling;

public class EmployeeEmailDuplicateException extends RuntimeException {
    private final String email;
    public EmployeeEmailDuplicateException(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
