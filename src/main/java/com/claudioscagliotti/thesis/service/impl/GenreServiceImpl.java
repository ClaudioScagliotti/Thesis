package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.repository.GenreRepository;
import com.claudioscagliotti.thesis.service.GenreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing operations related to genres.
 */
@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    /**
     * Constructs a GenreService instance with the provided GenreRepository.
     *
     * @param genreRepository The repository for GenreEntity.
     */
    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    /**
     * Creates genre IDs formatted for API queries based on the genres associated with the goal.
     *
     * @param goalEntity The goal entity containing associated genres.
     * @return A string representing genre IDs formatted for API queries.
     */
    public String createGenreIds(GoalEntity goalEntity) {
        return "&" + QueryParamEnum.WITH_GENRES.getValue() +
                goalEntity.getGenreEntityList().stream()
                        .map(GenreEntity::getName)
                        .map(GenreEnum::getByName)
                        .map(GenreEnum::getTmdbId)
                        .map(Object::toString)
                        .collect(Collectors.joining("|")); // TODO manage the joiner
    }

    /**
     * Retrieves a genre entity by name and saves it if it doesn't exist.
     *
     * @param entity The genre entity to retrieve and possibly save.
     * @return The existing or newly saved genre entity.
     */
    @Transactional
    public GenreEntity getGenreByNameAndSaveIfNotExists(GenreEntity entity) {
        Optional<GenreEntity> genreEntityByName = genreRepository.getGenreEntityByName(entity.getName());
        return genreEntityByName.orElseGet(() -> genreRepository.save(entity));
    }

    /**
     * Maps genre Tmdb IDs to genre entities.
     *
     * @param genreTmdbId The list of genre Tmdb IDs.
     * @return List of genre entities mapped from the provided genre IDs.
     */
    public List<GenreEntity> mapGenreTmdbIdsToEntities(List<Long> genreTmdbId) {
        if (genreTmdbId == null || genreTmdbId.isEmpty()) {
            return Collections.emptyList();
        }
        return genreTmdbId.stream()
                .map(genreRepository::findByTmdbId)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Maps genre IDs to genre entities.
     *
     * @param genreId The list of genre IDs.
     * @return List of genre entities mapped from the provided genre IDs.
     */
    public List<GenreEntity> mapGenreIdsToEntities(List<Long> genreId) {
        if (genreId == null || genreId.isEmpty()) {
            return Collections.emptyList();
        }
        return genreId.stream()
                .map(genreRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    /**
     * Maps genre entities to genre IDs.
     *
     * @param genreEntities The list of genre entities.
     * @return List of genre IDs mapped from the provided genre entities.
     */

    public List<Long> mapGenreEntitiesToIds(List<GenreEntity> genreEntities) {
        if (genreEntities == null || genreEntities.isEmpty()) {
            return null;
        }
        return genreEntities.stream()
                .map(GenreEntity::getTmdbId)
                .collect(Collectors.toList());
    }

}

