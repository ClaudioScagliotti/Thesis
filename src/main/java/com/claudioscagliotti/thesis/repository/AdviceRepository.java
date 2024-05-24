package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Advice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceRepository extends JpaRepository<Advice, Long> {
    
}
