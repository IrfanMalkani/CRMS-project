package com.crms.customer_crm_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crms.customer_crm_backend.model.User;

@Repository // Indicates that this interface is a Spring Data repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Custom method to find a User by their userName
    Optional<User> findByUserName(String userName);

    // Custom method to find a User by their email
    Optional<User> findByEmail(String email);
}