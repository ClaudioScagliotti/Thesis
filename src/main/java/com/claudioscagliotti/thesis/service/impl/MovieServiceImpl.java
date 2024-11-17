package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.repository.MovieRepository;
import com.claudioscagliotti.thesis.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling movie-related operations.
 */
@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    /**
     * Constructs a MovieService with the specified MovieRepository.
     *
     * @param movieRepository The MovieRepository to be used.
     */
    public MovieServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    /**
     * Find a movie with the given tmdbId
     *
     * @param externalId The id of the movie on TMDB.
     * @return An optional of the MovieEntity.
     */
    public Optional<MovieEntity> findByTmdbId(Long externalId) {
        return movieRepository.findByTmdbId(externalId);
    }

    /**
     * Saves a list of movie entities to the repository, checking  for every film
     * if it not exists already on database.
     *
     * @param movieEntityList The list of MovieEntity objects to be saved.
     * @return The list of saved MovieEntity objects.
     */
    public List<MovieEntity> saveAllMovie(List<MovieEntity> movieEntityList) {
        List<MovieEntity> savedMovies = new ArrayList<>();
        for (MovieEntity movie : movieEntityList) {
            Optional<MovieEntity> existingMovie = findByTmdbId(movie.getTmdbId());
            if (existingMovie.isEmpty()) {
                savedMovies.add(movieRepository.save(movie));
            } else {
                savedMovies.add(existingMovie.get());
            }
        }
        return savedMovies;
    }
}
