package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    
}
