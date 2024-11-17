package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.MovieEntity;

import java.util.List;
import java.util.Optional;

public interface MovieService {

    /**
     * Finds a movie with the given TMDB ID.
     *
     * @param externalId The ID of the movie on TMDB.
     * @return An Optional of the MovieEntity.
     */
    Optional<MovieEntity> findByTmdbId(Long externalId);

    /**
     * Saves a list of movie entities to the repository, checking for each film
     * if it already exists in the database.
     *
     * @param movieEntityList The list of MovieEntity objects to be saved.
     * @return The list of saved MovieEntity objects.
     */
    List<MovieEntity> saveAllMovie(List<MovieEntity> movieEntityList);
}
