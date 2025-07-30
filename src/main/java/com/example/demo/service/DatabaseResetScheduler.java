package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseResetScheduler {
    
    @Value("${app.database.reset.enabled:true}")
    private boolean databaseResetEnabled;
    
    @Autowired
    private DatabaseResetService databaseResetService;
    
    // Run every hour to check if reset is needed
    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 milliseconds
    public void checkDatabaseReset() {
        if (!databaseResetEnabled) {
            return; // Skip scheduled reset in production
        }
        System.out.println("Scheduled check: Verifying if database reset is needed...");
        databaseResetService.checkAndResetIfNeeded();
    }
}