package com.crms.customer_crm_backend.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Add this import

import com.crms.customer_crm_backend.model.Customer;
import com.crms.customer_crm_backend.model.WorkLog;
import com.crms.customer_crm_backend.repository.CustomerRepository;
import com.crms.customer_crm_backend.repository.WorkLogRepository;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WorkLogRepository workLogRepository;

    // Create a new customer record
    @Transactional
    public Customer createCustomer(Customer customer) {
        // Calculate due_amount using BigDecimal arithmetic
        if (customer.getCallAmount() != null && customer.getDepositAmount() != null) {
            customer.setDueAmount(customer.getCallAmount().subtract(customer.getDepositAmount()));
        } else {
            customer.setDueAmount(BigDecimal.ZERO); // Default to zero if amounts are null
        }


        Set<WorkLog> managedWorkLogs = customer.getTypeOfWork().stream()
            .map(workLog -> workLog.getId() != null ? workLogRepository.findById(workLog.getId()).orElse(null) : null)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());

        if (managedWorkLogs.isEmpty() && !customer.getTypeOfWork().isEmpty()) {
            throw new RuntimeException("One or more selected WorkLog entries are invalid or do not exist.");
        }

        customer.setTypeOfWork(managedWorkLogs);

        return customerRepository.save(customer);
    }

    // Get all customer records
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Get customer by ID
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    // Update customer record
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + id));
        existingCustomer.setOperatorName(customerDetails.getOperatorName());
        existingCustomer.setCustomerName(customerDetails.getCustomerName());
        existingCustomer.setFatherName(customerDetails.getFatherName());
        existingCustomer.setSpouseName(customerDetails.getSpouseName());
        existingCustomer.setMobileNumber(customerDetails.getMobileNumber());
        existingCustomer.setWorkDescription(customerDetails.getWorkDescription());
        existingCustomer.setCosting(customerDetails.getCosting());
        existingCustomer.setCallAmount(customerDetails.getCallAmount());
        existingCustomer.setDepositAmount(customerDetails.getDepositAmount());

        // Recalculate due_amount if callAmount or depositAmount changed
        if (customerDetails.getCallAmount() != null && customerDetails.getDepositAmount() != null) {
            existingCustomer.setDueAmount(customerDetails.getCallAmount().subtract(customerDetails.getDepositAmount()));
        } else {
            existingCustomer.setDueAmount(BigDecimal.ZERO);
        }

        existingCustomer.setDiscountOrLoss(customerDetails.getDiscountOrLoss());
        existingCustomer.setTokenNumber(customerDetails.getTokenNumber());
        existingCustomer.setEmitraId(customerDetails.getEmitraId());
        existingCustomer.setLetterRemark(customerDetails.getLetterRemark());
        existingCustomer.setOtherRemark(customerDetails.getOtherRemark());
        existingCustomer.setWorkStatus(customerDetails.getWorkStatus());
        existingCustomer.setOperatorName(customerDetails.getOperatorName());

        Set<WorkLog> updatedWorkLogs = customerDetails.getTypeOfWork().stream()
            .map(workLog -> workLog.getId() != null ? workLogRepository.findById(workLog.getId()).orElse(null) : null)
            .filter(java.util.Objects::nonNull)
            .collect(Collectors.toSet());

        if (updatedWorkLogs.isEmpty() && !customerDetails.getTypeOfWork().isEmpty()) {
            throw new RuntimeException("One or more selected WorkLog entries for update are invalid or do not exist.");
        }
        existingCustomer.setTypeOfWork(updatedWorkLogs);

        return customerRepository.save(existingCustomer);
    }

    // Delete customer record
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found with id: " + id);
        }
        customerRepository.deleteById(id);
    }

    // Search customers for reporting
    public List<Customer> searchCustomers(String customerName) {
        if (customerName != null && !customerName.trim().isEmpty()) {
            return customerRepository.findByCustomerNameContainingIgnoreCase(customerName);
        }
        return customerRepository.findAll();
    }
}