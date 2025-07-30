package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseResetScheduler {
    
    @Value("${app.database.reset.enabled:true}")
    private boolean databaseResetEnabled;
    
    @Autowired
    private DatabaseResetService databaseResetService;
    
    // Run every hour to check if reset is needed
    private static final Logger logger = LoggerFactory.getLogger(DatabaseResetScheduler.class);

    @Scheduled(fixedRate = 3600000) // 1 hour = 3600000 milliseconds
    public void checkDatabaseReset() {
        if (!databaseResetEnabled) {
            logger.info("Database reset is disabled. Skipping scheduled check.");
            return; // Skip scheduled reset in production
        }
        logger.info("Scheduled check: Verifying if database reset is needed...");
        databaseResetService.checkAndResetIfNeeded();
    }
}