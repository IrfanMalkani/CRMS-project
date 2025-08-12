package com.crms.customer_crm_backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crms.customer_crm_backend.model.WorkLog;
import com.crms.customer_crm_backend.service.WorkLogService;

@RestController
@RequestMapping("/api/worklogs") // Base URL for WorkLog endpoints
public class WorkLogController {

    @Autowired
    private WorkLogService workLogService;

    // POST /api/worklogs - Add new WorkLog
    @PostMapping
    public ResponseEntity<?> createWorkLog(@RequestBody WorkLog workLog) {
        try {
            WorkLog createdWorkLog = workLogService.createWorkLog(workLog);
            return new ResponseEntity<>(createdWorkLog, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // GET /api/worklogs - Get all WorkLogs
    @GetMapping
    public ResponseEntity<List<WorkLog>> getAllWorkLogs() {
        List<WorkLog> workLogs = workLogService.getAllWorkLogs();
        return new ResponseEntity<>(workLogs, HttpStatus.OK);
    }

    // GET /api/worklogs/{id} - Get WorkLog by ID
    @GetMapping("/{id}")
    public ResponseEntity<WorkLog> getWorkLogById(@PathVariable Long id) {
        return workLogService.getWorkLogById(id)
                .map(workLog -> new ResponseEntity<>(workLog, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // PUT /api/worklogs/{id} - Update WorkLog
    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkLog(@PathVariable Long id, @RequestBody WorkLog workLogDetails) {
        try {
            WorkLog updatedWorkLog = workLogService.updateWorkLog(id, workLogDetails);
            return new ResponseEntity<>(updatedWorkLog, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // DELETE /api/worklogs/{id} - Delete WorkLog
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkLog(@PathVariable Long id) {
        try {
            workLogService.deleteWorkLog(id);
            return new ResponseEntity<>("Work log deleted successfully", HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}