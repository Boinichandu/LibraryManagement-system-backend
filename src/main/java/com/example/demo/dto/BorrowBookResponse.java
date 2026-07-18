package com.example.demo.dto;

import java.time.LocalDate;

public class BorrowBookResponse {

    private Long requestId;

    private Long bookId;

    private String title;

    private String author;

    private String isbn;

    private String image;

    private LocalDate borrowDate;

    private LocalDate dueDate;

    private LocalDate returnDate;

    private String status;

    private Double fine;

    private Integer readerScore;

    public BorrowBookResponse() {
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getFine() {
        return fine;
    }

    public void setFine(Double fine) {
        this.fine = fine;
    }

    public Integer getReaderScore() {
        return readerScore;
    }

    public void setReaderScore(Integer readerScore) {
        this.readerScore = readerScore;
    }
}
