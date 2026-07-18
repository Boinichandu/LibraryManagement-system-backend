package com.example.demo.dto;

public class UserProfileResponse {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private Integer totalBorrowed;
    private Integer currentlyBorrowed;
    private Integer overdueBooks;
    private Integer readerScore;

    public UserProfileResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(Integer totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    public Integer getCurrentlyBorrowed() {
        return currentlyBorrowed;
    }

    public void setCurrentlyBorrowed(Integer currentlyBorrowed) {
        this.currentlyBorrowed = currentlyBorrowed;
    }

    public Integer getOverdueBooks() {
        return overdueBooks;
    }

    public void setOverdueBooks(Integer overdueBooks) {
        this.overdueBooks = overdueBooks;
    }

    public Integer getReaderScore() {
        return readerScore;
    }

    public void setReaderScore(Integer readerScore) {
        this.readerScore = readerScore;
    }
}
