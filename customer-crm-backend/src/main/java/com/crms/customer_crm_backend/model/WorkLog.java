package com.crms.customer_crm_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity; // Add this import
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "work_log")
@Data
public class WorkLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String workName;

    @Column(nullable = false, precision = 10, scale = 2) // DECIMAL(10,2)
    private BigDecimal actualAmount; // Change from Double to BigDecimal

    @Column(nullable = false, precision = 10, scale = 2) // DECIMAL(10,2)
    private BigDecimal customerCost; // Change from Double to BigDecimal

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}