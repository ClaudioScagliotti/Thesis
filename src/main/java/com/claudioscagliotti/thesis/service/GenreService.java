package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;

import java.util.List;

public interface GenreService {

    /**
     * Creates genre IDs formatted for API queries based on the genres associated with the goal.
     *
     * @param goalEntity The goal entity containing associated genres.
     * @return A string representing genre IDs formatted for API queries.
     */
    String createGenreIds(GoalEntity goalEntity);

    /**
     * Retrieves a genre entity by name and saves it if it doesn't exist.
     *
     * @param entity The genre entity to retrieve and possibly save.
     * @return The existing or newly saved genre entity.
     */
    GenreEntity getGenreByNameAndSaveIfNotExists(GenreEntity entity);

    /**
     * Maps genre Tmdb IDs to genre entities.
     *
     * @param genreTmdbId The list of genre Tmdb IDs.
     * @return List of genre entities mapped from the provided genre IDs.
     */
    List<GenreEntity> mapGenreTmdbIdsToEntities(List<Long> genreTmdbId);

    /**
     * Maps genre IDs to genre entities.
     *
     * @param genreId The list of genre IDs.
     * @return List of genre entities mapped from the provided genre IDs.
     */
    List<GenreEntity> mapGenreIdsToEntities(List<Long> genreId);

}
