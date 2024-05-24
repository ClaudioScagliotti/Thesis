package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {
    
}
