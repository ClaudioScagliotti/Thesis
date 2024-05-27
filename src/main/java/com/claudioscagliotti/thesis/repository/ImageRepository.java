package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    
}
