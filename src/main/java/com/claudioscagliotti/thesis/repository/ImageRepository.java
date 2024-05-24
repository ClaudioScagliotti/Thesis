package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    
}
