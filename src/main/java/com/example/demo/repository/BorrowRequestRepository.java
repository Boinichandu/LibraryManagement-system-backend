package com.example.demo.repository;

import com.example.demo.model.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Long> {

    // Get all requests of a user
    List<BorrowRequest> findByUserId(Long userId);

    // Get all requests of a book
    List<BorrowRequest> findByBookId(Long bookId);

    // Get all requests by status
    List<BorrowRequest> findByStatus(String status);

    // Get requests of a user by status
    List<BorrowRequest> findByUserIdAndStatus(Long userId, String status);

}