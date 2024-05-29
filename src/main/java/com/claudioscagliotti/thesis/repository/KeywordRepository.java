package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.KeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<KeywordEntity, Long> {
    
}
