package com.crms.customer_crm_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crms.customer_crm_backend.model.WorkLog;

@Repository
public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
    Optional<WorkLog> findByWorkName(String workName);
}