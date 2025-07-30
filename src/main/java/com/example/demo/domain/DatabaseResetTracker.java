package com.example.demo.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "database_reset_tracker")
public class DatabaseResetTracker {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "last_reset_time")
    private LocalDateTime lastResetTime;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public DatabaseResetTracker() {
        this.createdAt = LocalDateTime.now();
        this.lastResetTime = LocalDateTime.now();
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public LocalDateTime getLastResetTime() {
        return lastResetTime;
    }
    
    public void setLastResetTime(LocalDateTime lastResetTime) {
        this.lastResetTime = lastResetTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}