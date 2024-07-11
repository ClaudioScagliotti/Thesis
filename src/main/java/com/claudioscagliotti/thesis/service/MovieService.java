package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * Saves a list of movie entities to the repository.
     *
     * @param movieEntityList The list of MovieEntity objects to be saved.
     * @return The list of saved MovieEntity objects.
     */
    List<MovieEntity> saveAllMovie(List<MovieEntity> movieEntityList) {
        return movieRepository.saveAll(movieEntityList);
    }
}
