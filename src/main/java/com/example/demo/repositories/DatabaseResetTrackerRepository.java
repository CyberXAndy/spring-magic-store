package com.example.demo.repositories;

import com.example.demo.domain.DatabaseResetTracker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseResetTrackerRepository extends CrudRepository<DatabaseResetTracker, Long> {
    DatabaseResetTracker findFirstByOrderByIdDesc();
}