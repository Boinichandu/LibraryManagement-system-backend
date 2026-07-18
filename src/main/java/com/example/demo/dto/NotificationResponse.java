package com.example.demo.dto;

import java.time.LocalDate;

public class NotificationResponse {

    private String title;
    private String message;
    private String type;
    private LocalDate dueDate;

    public NotificationResponse() {
    }

    public NotificationResponse(String title, String message, String type, LocalDate dueDate) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.dueDate = dueDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
