package com.example.demo.controller;

import com.example.demo.dto.AdminDashboardSummary;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.UserProfileResponse;
import com.example.demo.dto.UserResponse;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Registration API
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // Login API
    @PostMapping("/login")
    public LoginResponse loginUser(@RequestBody LoginRequest loginRequest) {
        return userService.loginUser(
                loginRequest.getUsername(),
                loginRequest.getPassword());
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/dashboard-summary")
    public AdminDashboardSummary getDashboardSummary() {
        return userService.getDashboardSummary();
    }

    @GetMapping("/{id}/profile")
    public UserProfileResponse getUserProfile(@PathVariable Long id) {
        return userService.getUserProfile(id);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}