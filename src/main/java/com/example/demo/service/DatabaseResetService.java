package com.example.demo.service;

import com.example.demo.domain.DatabaseResetTracker;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Product;
import com.example.demo.repositories.DatabaseResetTrackerRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DatabaseResetService {
    
    @Value("${app.database.reset.enabled:true}")
    private boolean databaseResetEnabled;
    
    @Autowired
    private DatabaseResetTrackerRepository resetTrackerRepository;
    
    @Autowired
    private PartRepository partRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(DatabaseResetService.class);

    @Transactional
    public void checkAndResetIfNeeded() {
        if (!databaseResetEnabled) {
            logger.info("Database reset is disabled. Skipping check.");
            return;
        }
        logger.info("Checking if database reset is needed...");
        DatabaseResetTracker tracker = resetTrackerRepository.findFirstByOrderByIdDesc();
        
        if (tracker == null) {
            logger.info("No previous reset tracker found. Performing initial database reset.");
            performDatabaseReset();
        } else {
            LocalDateTime lastReset = tracker.getLastResetTime();
            LocalDateTime now = LocalDateTime.now();
            long hoursSinceLastReset = ChronoUnit.HOURS.between(lastReset, now);
            
            if (hoursSinceLastReset >= 24) {
                logger.info("More than 24 hours since last reset ({} hours). Performing database reset.", hoursSinceLastReset);
                performDatabaseReset();
            } else {
                logger.info("Database reset not needed yet. Last reset was {} hours ago.", hoursSinceLastReset);
            }
        }
    }

    @Transactional
    public void resetOnStartup() {
        if (!databaseResetEnabled) {
            logger.info("Database reset is disabled. Skipping startup reset.");
            return;
        }
        logger.info("Performing database reset on application startup...");
        performDatabaseReset();
    }

    @Transactional
    public void performDatabaseReset() {
        logger.info("Starting database reset process...");
        try {
            // Clear existing data - handle many-to-many relationships first
            // Clear all part-product associations to avoid cascade constraint issues
            logger.info("Clearing part-product associations...");
            Iterable<Product> products = productRepository.findAll();
            for (Product product : products) {
                product.getParts().clear();
                productRepository.save(product);
            }
            logger.info("Part-product associations cleared.");
            
            // Now safely delete all data
            logger.info("Deleting all existing parts and products...");
            partRepository.deleteAll();
            productRepository.deleteAll();
            logger.info("Existing parts and products deleted.");
            
            // Add default data from BootStrapData
            logger.info("Adding default data...");
            addDefaultData();
            logger.info("Default data added.");
            
            // Update or create reset tracker
            logger.info("Updating database reset tracker...");
            DatabaseResetTracker tracker = resetTrackerRepository.findFirstByOrderByIdDesc();
            if (tracker == null) {
                tracker = new DatabaseResetTracker();
            } else {
                tracker.setLastResetTime(LocalDateTime.now());
            }
            resetTrackerRepository.save(tracker);
            logger.info("Database reset tracker updated.");
            
            logger.info("Database reset completed successfully at: {}", LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Database reset failed: {}", e.getMessage(), e);
            throw new RuntimeException("Database reset failed", e);
        }
    }
    
    private void addDefaultData() {
        // Create the same default data as in BootStrapData
        OutsourcedPart part1 = new OutsourcedPart();
        part1.setCompanyName("Wand Co.");
        part1.setName("Magic Wand");
        part1.setInv(10);
        part1.setPrice(15.0);
        part1.setMinInv(5);
        part1.setMaxInv(20);

        OutsourcedPart part2 = new OutsourcedPart();
        part2.setCompanyName("Potion Co.");
        part2.setName("Potion Bottle");
        part2.setInv(20);
        part2.setPrice(5.0);
        part2.setMinInv(10);
        part2.setMaxInv(50);

        OutsourcedPart part3 = new OutsourcedPart();
        part3.setCompanyName("Spell Book Co.");
        part3.setName("Spell Book");
        part3.setInv(5);
        part3.setPrice(25.0);
        part3.setMinInv(2);
        part3.setMaxInv(15);

        OutsourcedPart part4 = new OutsourcedPart();
        part4.setCompanyName("Crystal Ball Co.");
        part4.setName("Crystal Ball");
        part4.setInv(8);
        part4.setPrice(40.0);
        part4.setMinInv(3);
        part4.setMaxInv(12);

        OutsourcedPart part5 = new OutsourcedPart();
        part5.setCompanyName("Broomstick Co.");
        part5.setName("Broomstick");
        part5.setInv(12);
        part5.setPrice(30.0);
        part5.setMinInv(5);
        part5.setMaxInv(25);

        partRepository.save(part1);
        partRepository.save(part2);
        partRepository.save(part3);
        partRepository.save(part4);
        partRepository.save(part5);

        Product product1 = new Product("Beginner Wizard Kit", 100.0, 10);
        Product product2 = new Product("Advanced Wizard Kit", 250.0, 5);
        Product product3 = new Product("Potion Making Set", 50.0, 20);
        Product product4 = new Product("Fortune Telling Set", 70.0, 8);
        Product product5 = new Product("Flying Starter Pack", 200.0, 12);

        productRepository.save(product1);
        productRepository.save(product2);
        productRepository.save(product3);
        productRepository.save(product4);
        productRepository.save(product5);
    }
}