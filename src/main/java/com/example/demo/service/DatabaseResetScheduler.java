package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseResetScheduler {
    
    @Autowired
    private DatabaseResetService databaseResetService;
    
    // Run every hour to check if reset is needed
    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 milliseconds
    public void checkDatabaseReset() {
        System.out.println("Scheduled check: Verifying if database reset is needed...");
        databaseResetService.checkAndResetIfNeeded();
    }
}