package com.example.demo.dto;

public class LoginResponse {

    private String message;
    private String role;
    private Long userId;

    public LoginResponse() {
    }

    public LoginResponse(String message, String role, Long userId) {
        this.message = message;
        this.role = role;
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}