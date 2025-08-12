package com.crms.customer_crm_backend.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crms.customer_crm_backend.model.User;
import com.crms.customer_crm_backend.service.UserService;
@RestController // Indicates that this class is a REST controller
@RequestMapping("/api/auth") // Base URL for these endpoints
public class UserController {
    @Autowired // Injects the UserService dependency
    private UserService userService;
    @PostMapping("/signup") // Maps POST requests to /api/auth/signup
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user);
            // Mask the password hash for security before sending response
            registeredUser.setPasswordHash(null);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/login") // Maps POST requests to /api/auth/login
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        try {
            // In a real application, you'd typically return a JWT token here
            // For now, we'll just confirm login and return basic user info
            User loggedInUser = userService.loginUser(user.getUserName(), user.getPasswordHash());
            // Mask the password hash for security
            loggedInUser.setPasswordHash(null);
            return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}