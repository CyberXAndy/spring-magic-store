package com.example.demo.bootstrap;

import com.example.demo.service.DatabaseResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1) // Ensure this runs before other startup components
public class ApplicationStartupRunner implements ApplicationRunner {
    
    @Value("${app.database.reset.enabled:true}")
    private boolean databaseResetEnabled;
    
    @Autowired
    private DatabaseResetService databaseResetService;
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!databaseResetEnabled) {
            System.out.println("Application startup - database reset disabled in production mode.");
            return;
        }
        
        System.out.println("Application startup - performing database reset...");
        databaseResetService.resetOnStartup();
        System.out.println("Database reset completed.");
    }
}