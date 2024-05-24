package com.claudioscagliotti.thesis.repository;


import com.claudioscagliotti.thesis.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    
}
