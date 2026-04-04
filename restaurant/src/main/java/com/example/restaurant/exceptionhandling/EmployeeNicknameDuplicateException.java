package com.example.restaurant.exceptionhandling;

public class EmployeeNicknameDuplicateException extends RuntimeException {
    private final String nickname;
    public EmployeeNicknameDuplicateException(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }
}
