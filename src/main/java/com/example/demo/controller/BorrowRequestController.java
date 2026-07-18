package com.example.demo.controller;

import com.example.demo.dto.BorrowBookResponse;
import com.example.demo.dto.BorrowRequestAdminResponse;
import com.example.demo.dto.NotificationResponse;
import com.example.demo.model.BorrowRequest;
import com.example.demo.service.BorrowRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/borrow")
@CrossOrigin(origins = "*")
public class BorrowRequestController {

    @Autowired
    private BorrowRequestService borrowRequestService;

    // User requests a book
    @PostMapping("/request")
    public String requestBook(@RequestBody BorrowRequest request) {
        return borrowRequestService.requestBook(request);
    }

    // View all borrow requests
    @GetMapping
    public List<BorrowRequest> getAllRequests() {
        return borrowRequestService.getAllRequests();
    }

    @GetMapping("/admin/borrow-requests")
    public List<BorrowRequestAdminResponse> getAdminBorrowRequests() {
        return borrowRequestService.getAdminBorrowRequests();
    }

    @GetMapping("/admin/return-requests")
    public List<BorrowRequestAdminResponse> getAdminReturnRequests() {
        return borrowRequestService.getAdminReturnRequests();
    }

    // Current borrowed books of a user
    @GetMapping("/user/{userId}")
    public List<BorrowBookResponse> getCurrentBooks(@PathVariable Long userId) {
        return borrowRequestService.getCurrentBooks(userId);
    }

    @GetMapping("/notifications/{userId}")
    public List<NotificationResponse> getNotifications(@PathVariable Long userId) {
        return borrowRequestService.getNotifications(userId);
    }

    // Admin approves request
    @PutMapping("/approve/{id}")
    public String approveRequest(@PathVariable Long id) {
        return borrowRequestService.approveRequest(id);
    }

    // Admin rejects request
    @PutMapping("/reject/{id}")
    public String rejectRequest(@PathVariable Long id) {
        return borrowRequestService.rejectRequest(id);
    }

    // User sends return request
    @PutMapping("/return/{id}")
    public String returnBook(@PathVariable Long id) {
        return borrowRequestService.returnBook(id);
    }

    // Admin approves return
    @PutMapping("/approve-return/{id}")
    public String approveReturn(@PathVariable Long id) {
        return borrowRequestService.approveReturn(id);
    }
}