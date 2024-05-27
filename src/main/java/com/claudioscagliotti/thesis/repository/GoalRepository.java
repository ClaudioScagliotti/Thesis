package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.GoalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<GoalEntity, Long> {
    
}
