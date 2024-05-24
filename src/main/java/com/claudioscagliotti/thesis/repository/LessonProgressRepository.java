package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.LessonProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonProgressRepository extends JpaRepository<LessonProgress, Long> {
    
}
