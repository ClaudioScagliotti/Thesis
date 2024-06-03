package com.claudioscagliotti.thesis.repository;


import com.claudioscagliotti.thesis.model.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    GenreEntity getGenreEntityByName(String name);
    
}
