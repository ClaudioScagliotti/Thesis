package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    List<MovieEntity> saveAllMovie(List<MovieEntity> movieEntityList) {
        return movieRepository.saveAll(movieEntityList);
    }
}
