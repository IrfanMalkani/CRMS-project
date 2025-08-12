package com.crms.customer_crm_backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.crms.customer_crm_backend.model.WorkLog;
import com.crms.customer_crm_backend.repository.WorkLogRepository;

@Service
public class WorkLogService {

    @Autowired
    private WorkLogRepository workLogRepository;

    // Add new WorkLog
    public WorkLog createWorkLog(WorkLog workLog) {
        if (workLogRepository.findByWorkName(workLog.getWorkName()).isPresent()) {
            throw new RuntimeException("Work log with this name already exists!");
        }
        return workLogRepository.save(workLog);
    }

    // Get all WorkLogs
    public List<WorkLog> getAllWorkLogs() {
        return workLogRepository.findAll();
    }

    // Get WorkLog by ID
    public Optional<WorkLog> getWorkLogById(Long id) {
        return workLogRepository.findById(id);
    }

    // Update WorkLog
    public WorkLog updateWorkLog(Long id, WorkLog workLogDetails) {
        WorkLog existingWorkLog = workLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Work log not found with id: " + id));

        // Check if the new workName already exists for another ID
        if (workLogRepository.findByWorkName(workLogDetails.getWorkName()).isPresent() &&
            !workLogRepository.findByWorkName(workLogDetails.getWorkName()).get().getId().equals(id)) {
            throw new RuntimeException("Work log with this name already exists for another record!");
        }

        existingWorkLog.setWorkName(workLogDetails.getWorkName());
        existingWorkLog.setActualAmount(workLogDetails.getActualAmount());
        existingWorkLog.setCustomerCost(workLogDetails.getCustomerCost());
        // createdAt is not updated
        return workLogRepository.save(existingWorkLog);
    }

    // Delete WorkLog
    public void deleteWorkLog(Long id) {
        if (!workLogRepository.existsById(id)) {
            throw new RuntimeException("Work log not found with id: " + id);
        }
        workLogRepository.deleteById(id);
    }
}