package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.model.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Long> {
    
}
