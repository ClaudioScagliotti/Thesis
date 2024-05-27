package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    
}
