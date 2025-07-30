package com.example.demo.bootstrap;

import com.example.demo.service.DatabaseResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Ensure this runs before other startup components
public class ApplicationStartupRunner implements ApplicationRunner {
    
    @Autowired
    private DatabaseResetService databaseResetService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application startup - checking database reset requirements...");
        databaseResetService.checkAndResetIfNeeded();
        System.out.println("Database reset check completed.");
    }
}