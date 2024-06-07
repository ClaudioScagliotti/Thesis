package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.mapper.MovieMapper;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import com.claudioscagliotti.thesis.repository.AdviceRepository;
import com.claudioscagliotti.thesis.utility.TimeToDedicateConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AdviceService {

    private final AdviceRepository adviceRepository;
    private final GoalService goalService;
    private final TmdbApiClient client;
    private final MovieMapper movieMapper;

    public AdviceService(AdviceRepository adviceRepository, GoalService goalService, TmdbApiClient client, MovieMapper movieMapper) {
        this.adviceRepository = adviceRepository;
        this.goalService = goalService;
        this.client = client;
        this.movieMapper = movieMapper;
    }
    public List<AdviceEntity> createAdviceList(GoalEntity goalEntity) {

        MovieResponse response=client.getMovies(goalService.composeParams(goalEntity));

        goalService.updatePage(goalEntity, response);

        List<AdviceEntity> adviceList = new ArrayList<>();
        List<MovieDto> movieList = response.results();

        for (MovieDto movie : movieList) {
            //TODO SET USER
            AdviceEntity adviceEntity = new AdviceEntity();

            int daysUntilDeadline = TimeToDedicateConverter.convertTimeToDedicateToDays(goalEntity.getTimeToDedicate());
            adviceEntity.setDeadline(LocalDateTime.now().plusDays(daysUntilDeadline));

            adviceEntity.setPoints(100);//TODO ADD ALGORITM TO CALCULATE POINTS

            adviceEntity.setMovie(movieMapper.toMovieEntity(movie));

            adviceList.add(adviceEntity);
        }

        return adviceList;
    }


}
