package com.claudioscagliotti.thesis.repository;


import com.claudioscagliotti.thesis.model.GenreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    Optional<GenreEntity> getGenreEntityByName(String name);
    GenreEntity getGenreEntityByTmdbId(Integer tmdbId);
    
}
