package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Medal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedalRepository extends JpaRepository<Medal, Long> {
    
}
