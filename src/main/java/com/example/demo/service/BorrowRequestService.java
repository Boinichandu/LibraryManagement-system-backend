package com.example.demo.service;

import com.example.demo.dto.BorrowBookResponse;
import com.example.demo.dto.BorrowRequestAdminResponse;
import com.example.demo.dto.NotificationResponse;
import com.example.demo.model.Book;
import com.example.demo.model.BorrowRequest;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowRequestRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BorrowRequestService {

    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    // User requests a book
    public String requestBook(BorrowRequest request) {

        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());

        if (optionalBook.isEmpty()) {
            return "Book Not Found";
        }

        Book book = optionalBook.get();

        if (book.getAvailableCopies() <= 0) {
            return "Book Not Available";
        }

        request.setStatus("PENDING");
        request.setBorrowDate(LocalDate.now());
        request.setDueDate(null);
        request.setReturnDate(null);
        request.setFine(0.0);
        request.setReaderScore(0);

        borrowRequestRepository.save(request);

        return "Borrow Request Sent Successfully";
    }

    // View all requests
    public List<BorrowRequest> getAllRequests() {
        return borrowRequestRepository.findAll();
    }

    public List<BorrowRequestAdminResponse> getAdminBorrowRequests() {
        List<BorrowRequest> requests = borrowRequestRepository.findAll();
        List<BorrowRequestAdminResponse> response = new ArrayList<>();

        for (BorrowRequest request : requests) {
            BorrowRequestAdminResponse dto = new BorrowRequestAdminResponse();
            dto.setRequestId(request.getId());
            dto.setUserId(request.getUserId());

            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            if (optionalUser.isPresent()) {
                dto.setUserName(optionalUser.get().getFullName());
                dto.setEmail(optionalUser.get().getEmail());
            }

            Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
            if (optionalBook.isPresent()) {
                dto.setTitle(optionalBook.get().getTitle());
                dto.setIsbn(optionalBook.get().getIsbn());
            }

            dto.setRequestDate(request.getBorrowDate());
            dto.setStatus(request.getStatus());
            response.add(dto);
        }

        return response;
    }

    public List<BorrowRequestAdminResponse> getAdminReturnRequests() {
        List<BorrowRequest> requests = borrowRequestRepository.findByStatus("RETURN_PENDING");
        List<BorrowRequestAdminResponse> response = new ArrayList<>();

        for (BorrowRequest request : requests) {
            BorrowRequestAdminResponse dto = new BorrowRequestAdminResponse();
            dto.setRequestId(request.getId());
            dto.setUserId(request.getUserId());

            Optional<User> optionalUser = userRepository.findById(request.getUserId());
            if (optionalUser.isPresent()) {
                dto.setUserName(optionalUser.get().getFullName());
                dto.setEmail(optionalUser.get().getEmail());
            }

            Optional<Book> optionalBook = bookRepository.findById(request.getBookId());
            if (optionalBook.isPresent()) {
                dto.setTitle(optionalBook.get().getTitle());
                dto.setIsbn(optionalBook.get().getIsbn());
            }

            dto.setRequestDate(request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now());
            dto.setStatus(request.getStatus());
            response.add(dto);
        }

        return response;
    }

    // Notification feed for a user
    public List<NotificationResponse> getNotifications(Long userId) {
        List<BorrowRequest> requests = borrowRequestRepository.findByUserId(userId);
        List<NotificationResponse> notifications = new ArrayList<>();

        for (BorrowRequest request : requests) {
            if ("APPROVED".equals(request.getStatus())) {
                LocalDate dueDate = request.getDueDate();
                long daysLeft = dueDate != null ? DAYS.between(LocalDate.now(), dueDate) : -1;

                String title = "Due Date Reminder";
                String message = "Your borrowed book is due soon. Please return it on time.";
                String type = "warning";

                if (dueDate == null) {
                    title = "Due Date Reminder";
                    message = "Your borrowed book has no due date yet.";
                    type = "info";
                } else if (daysLeft < 0) {
                    title = "Overdue Book Alert";
                    message = "Your borrowed book is overdue. Please return it immediately.";
                    type = "danger";
                } else if (daysLeft == 1) {
                    title = "Due Tomorrow Reminder";
                    message = "Your borrowed book is due tomorrow. Please return it on time.";
                    type = "warning";
                } else if (daysLeft <= 2) {
                    message = "Your borrowed book is due in " + daysLeft + " day(s). Please return it on time.";
                }

                notifications.add(new NotificationResponse(title, message, type, dueDate));
            }

            if ("RETURN_PENDING".equals(request.getStatus())) {
                notifications.add(new NotificationResponse(
                        "Return Request Status",
                        "Your return request is waiting for library confirmation.",
                        "info",
                        request.getReturnDate() != null ? request.getReturnDate() : LocalDate.now()));
            }
        }

        if (notifications.isEmpty()) {
            notifications.add(new NotificationResponse(
                    "No Notifications",
                    "You currently have no library alerts.",
                    "info",
                    null));
        }

        return notifications;
    }

    // Get all currently borrowed books of a user
    public List<BorrowBookResponse> getCurrentBooks(Long userId) {

        List<BorrowRequest> requests = borrowRequestRepository.findByUserIdAndStatus(userId, "APPROVED");

        List<BorrowBookResponse> response = new ArrayList<>();

        for (BorrowRequest request : requests) {

            Book book = bookRepository.findById(request.getBookId()).orElse(null);

            if (book != null) {

                BorrowBookResponse dto = new BorrowBookResponse();

                dto.setRequestId(request.getId());
                dto.setBookId(book.getId());

                dto.setTitle(book.getTitle());
                dto.setAuthor(book.getAuthor());
                dto.setIsbn(book.getIsbn());
                dto.setImage(book.getImage());

                dto.setBorrowDate(request.getBorrowDate());
                dto.setDueDate(request.getDueDate());
                dto.setReturnDate(request.getReturnDate());

                dto.setStatus(request.getStatus());
                dto.setFine(request.getFine());
                dto.setReaderScore(request.getReaderScore());

                response.add(dto);
            }
        }

        return response;
    }

    // Admin Approves Request
    public String approveRequest(Long requestId) {

        Optional<BorrowRequest> optionalRequest = borrowRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return "Request Not Found";
        }

        BorrowRequest request = optionalRequest.get();

        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());

        if (optionalBook.isEmpty()) {
            return "Book Not Found";
        }

        Book book = optionalBook.get();

        if (book.getAvailableCopies() <= 0) {
            return "Book Not Available";
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        request.setStatus("APPROVED");
        request.setBorrowDate(LocalDate.now());
        request.setDueDate(LocalDate.now().plusDays(15));

        borrowRequestRepository.save(request);

        return "Request Approved Successfully";
    }

    // Admin Rejects Request
    public String rejectRequest(Long requestId) {

        Optional<BorrowRequest> optionalRequest = borrowRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return "Request Not Found";
        }

        BorrowRequest request = optionalRequest.get();

        request.setStatus("REJECTED");

        borrowRequestRepository.save(request);

        return "Request Rejected";
    }

    // User requests return
    public String returnBook(Long requestId) {

        Optional<BorrowRequest> optionalRequest = borrowRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return "Request Not Found";
        }

        BorrowRequest request = optionalRequest.get();

        if (!"APPROVED".equals(request.getStatus())) {
            return "Request is not currently borrowed";
        }

        request.setStatus("RETURN_PENDING");

        borrowRequestRepository.save(request);

        return "Return Request Sent";
    }

    // Admin approves return
    public String approveReturn(Long requestId) {

        Optional<BorrowRequest> optionalRequest = borrowRequestRepository.findById(requestId);

        if (optionalRequest.isEmpty()) {
            return "Request Not Found";
        }

        BorrowRequest request = optionalRequest.get();

        if (!"RETURN_PENDING".equals(request.getStatus())) {
            return "Request is not currently borrowed";
        }

        Optional<Book> optionalBook = bookRepository.findById(request.getBookId());

        if (optionalBook.isEmpty()) {
            return "Book Not Found";
        }

        Book book = optionalBook.get();

        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        request.setReturnDate(LocalDate.now());
        request.setStatus("RETURNED");

        if (request.getDueDate() != null) {
            long lateDays = ChronoUnit.DAYS.between(
                    request.getDueDate(),
                    request.getReturnDate());

            if (lateDays > 0) {
                request.setFine(lateDays * 10.0);
                request.setReaderScore(50);
            } else {
                request.setFine(0.0);
                request.setReaderScore(100);
            }
        } else {
            request.setFine(0.0);
            request.setReaderScore(100);
        }

        borrowRequestRepository.save(request);

        return "Book Returned Successfully";
    }

}