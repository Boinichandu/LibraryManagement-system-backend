package com.example.demo.service;

import com.example.demo.dto.AdminDashboardSummary;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.BorrowRequest;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.BorrowRequestRepository;
import com.example.demo.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void ensureAdminAccount() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setFullName("Library Admin");
            admin.setEmail("admin@library.com");
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setPhone("0000000000");
            admin.setRole("ADMIN");
            admin.setStatus("ACTIVE");
            userRepository.save(admin);
        }
    }

    @Autowired
    private BorrowRequestRepository borrowRequestRepository;

    @Autowired
    private BookRepository bookRepository;

    // Registration
    public String registerUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return "Email already exists";
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return "Username already exists";
        }

        userRepository.save(user);

        return "Registration Successful";
    }

    // Login
    public LoginResponse loginUser(String username, String password) {

        Optional<User> user = userRepository.findByUsernameAndPassword(username, password);

        if (user.isPresent()) {
            return new LoginResponse(
                    "Login Successful",
                    user.get().getRole(),
                    user.get().getId());
        }

        return new LoginResponse(
                "Invalid Username or Password",
                null,
                null);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> response = new ArrayList<>();

        for (User user : users) {
            UserResponse dto = new UserResponse();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setRole(user.getRole());
            dto.setPhone(user.getPhone() == null ? "-" : user.getPhone());
            dto.setStatus("ACTIVE");

            List<BorrowRequest> requests = borrowRequestRepository.findByUserId(user.getId());
            dto.setBorrowedBooks(requests.size());

            response.add(dto);
        }

        return response;
    }

    public AdminDashboardSummary getDashboardSummary() {
        AdminDashboardSummary summary = new AdminDashboardSummary();
        summary.setTotalBooks(bookRepository.count());
        summary.setTotalUsers(userRepository.count());
        summary.setBorrowedBooks(borrowRequestRepository.findByStatus("APPROVED").size());
        summary.setPendingRequests(borrowRequestRepository.findByStatus("PENDING").size());
        return summary;
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        UserProfileResponse profile = new UserProfileResponse();
        profile.setId(user.getId());
        profile.setFullName(user.getFullName());
        profile.setEmail(user.getEmail());
        profile.setPhone(user.getPhone());
        profile.setRole(user.getRole());
        profile.setStatus(user.getStatus() == null ? "ACTIVE" : user.getStatus());

        List<BorrowRequest> requests = borrowRequestRepository.findByUserId(userId);
        int totalBorrowed = requests.size();
        int currentlyBorrowed = 0;
        int overdueBooks = 0;
        int readerScore = 0;

        for (BorrowRequest request : requests) {
            if ("APPROVED".equals(request.getStatus())) {
                currentlyBorrowed++;
                if (request.getDueDate() != null && request.getDueDate().isBefore(java.time.LocalDate.now())) {
                    overdueBooks++;
                }
            }

            if (request.getReaderScore() != null) {
                readerScore = Math.max(readerScore, request.getReaderScore());
            }
        }

        profile.setTotalBorrowed(totalBorrowed);
        profile.setCurrentlyBorrowed(currentlyBorrowed);
        profile.setOverdueBooks(overdueBooks);
        profile.setReaderScore(readerScore == 0 ? 92 : readerScore);

        return profile;
    }

    public String deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return "User Deleted Successfully";
        }
        return "User Not Found";
    }
}