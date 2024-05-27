package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.AdviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdviceRepository extends JpaRepository<AdviceEntity, Long> {
    
}
