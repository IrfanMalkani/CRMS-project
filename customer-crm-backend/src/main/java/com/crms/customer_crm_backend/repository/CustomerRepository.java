package com.crms.customer_crm_backend.repository; // Corrected package path

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crms.customer_crm_backend.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Custom query to search customers by name (case-insensitive)
    List<Customer> findByCustomerNameContainingIgnoreCase(String customerName);

    // You might add more search methods here as needed for your report page
    // Example: List<Customer> findByMobileNumber(String mobileNumber);
    // Example: List<Customer> findByWorkStatus(Customer.WorkStatus status);
}