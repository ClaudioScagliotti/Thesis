package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
}
