package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for handling movie-related operations.
 */
@Service
public class MovieService {

    private final MovieRepository movieRepository;

    /**
     * Constructs a MovieService with the specified MovieRepository.
     *
     * @param movieRepository The MovieRepository to be used.
     */
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }


    /**
     * Find a movie with the given tmdbId
     *
     * @param externalId The id of the movie on TMDB.
     * @return An optional of the MovieEntity.
     */
    public Optional<MovieEntity> findByTmdvlId(Integer externalId) {
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
            Optional<MovieEntity> existingMovie = findByTmdvlId(movie.getTmdbId());
            if (existingMovie.isEmpty()) {
                savedMovies.add(movieRepository.save(movie));
            } else {
                savedMovies.add(existingMovie.get());
            }
        }
        return savedMovies;
    }
}
