package com.crms.customer_crm_backend.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column; // Add this import
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "datetime", updatable = false)
    private LocalDateTime datetime;

    @Column(name = "operator_name", length = 500)
    private String operatorName;

    @Column(name = "customer_name", nullable = false, length = 100)
    private String customerName;

    @Column(name = "father_name", length = 100)
    private String fatherName;

    @Column(name = "spouse_name", length = 100)
    private String spouseName;

    @Column(name = "mobile_number", length = 255)
    private String mobileNumber;

    @Column(name = "work_description", columnDefinition = "TEXT")
    private String workDescription;

    @Column(name = "costing", precision = 10, scale = 2)
    private BigDecimal costing; // Change from Double to BigDecimal

    @Column(name = "call_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal callAmount; // Change from Double to BigDecimal

    @Column(name = "deposit_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal depositAmount; // Change from Double to BigDecimal

    //@Column(name = "due_amount", precision = 10, scale = 2)
    private BigDecimal dueAmount; // Change from Double to BigDecimal

    @Column(name = "discount_or_loss", precision = 10, scale = 2)
    private BigDecimal discountOrLoss; // Change from Double to BigDecimal

    @Column(name = "token_number", length = 50)
    private String tokenNumber;

    @Column(name = "emitra_id", length = 50)
    private String emitraId;

    @Column(name = "letter_remark", columnDefinition = "TEXT")
    private String letterRemark;

    @Column(name = "other_remark", columnDefinition = "TEXT")
    private String otherRemark;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_status", length = 20)
    private WorkStatus workStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "customer_work_link",
        joinColumns = @JoinColumn(name = "customer_id"),
        inverseJoinColumns = @JoinColumn(name = "work_log_id")
    )
    private Set<WorkLog> typeOfWork = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        datetime = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum WorkStatus {
        Accepted, In_progress, Due, Returned, Completed
    }
}