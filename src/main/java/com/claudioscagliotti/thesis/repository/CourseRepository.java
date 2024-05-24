package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
