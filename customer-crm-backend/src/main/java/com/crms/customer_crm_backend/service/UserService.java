package com.crms.customer_crm_backend.service;

import com.crms.customer_crm_backend.model.User;
import com.crms.customer_crm_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // We'll add this dependency soon
import org.springframework.stereotype.Service;

@Service // Indicates that this class is a Spring service component
public class UserService {

    @Autowired // Injects the UserRepository dependency
    private UserRepository userRepository;

    // Password encoder for hashing passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User registerUser(User user) {
        // Check if user with given username or email already exists
        if (userRepository.findByUserName(user.getUserName()).isPresent() ||
            userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Username or email already exists!");
        }

        // Hash the password before saving
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    public User loginUser(String userName, String password) {
        User user = userRepository.findByUserName(userName)
                                  .orElseThrow(() -> new RuntimeException("User not found!"));

        // Compare provided password with hashed password
        if (passwordEncoder.matches(password, user.getPasswordHash())) {
            return user; // Login successful
        } else {
            throw new RuntimeException("Invalid credentials!");
        }
    }

    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName)
                             .orElse(null); // Return null if not found, or throw an exception
    }
}
