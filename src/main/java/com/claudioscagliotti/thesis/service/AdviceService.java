package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.StatusEnum;
import com.claudioscagliotti.thesis.mapper.AdviceMapper;
import com.claudioscagliotti.thesis.mapper.GoalMapper;
import com.claudioscagliotti.thesis.mapper.MovieMapper;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import com.claudioscagliotti.thesis.repository.AdviceRepository;
import com.claudioscagliotti.thesis.utility.TimeToDedicateConverter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdviceService {

    private final AdviceRepository adviceRepository;
    private final AdviceMapper adviceMapper;
    private final GoalService goalService;
    private final TmdbApiClient client;
    private final MovieMapper movieMapper;
    private final MovieService movieService;
    private final GoalMapper goalMapper;

    private final GenreService genreService;
    private final UserService userService;

    public AdviceService(AdviceRepository adviceRepository, AdviceMapper adviceMapper, GoalService goalService, TmdbApiClient client, MovieMapper movieMapper, GoalMapper goalMapper, UserDetailsServiceImpl userDetailsService, MovieService movieService, GenreService genreService, UserService userService) {
        this.adviceRepository = adviceRepository;
        this.adviceMapper = adviceMapper;
        this.goalService = goalService;
        this.client = client;
        this.movieMapper = movieMapper;
        this.goalMapper = goalMapper;
        this.movieService = movieService;
        this.genreService = genreService;
        this.userService = userService;
    }

    public List<AdviceDto> getAdviceListResponse(String username){
        List<AdviceEntity> adviceList = createAdviceList(username);
        return adviceMapper.toAdviceDtoList(adviceList);
    }
    public AdviceDto getNextAdvice(String username){
        Pageable pageable = PageRequest.of(0, 1);
        List<AdviceEntity> adviceEntities = adviceRepository.findUncompletedAdviceByUsername(username, pageable);
        return adviceEntities.isEmpty() ? null : adviceMapper.toAdviceDto(adviceEntities.get(0));
    }

    public List<AdviceEntity> createAdviceList(String username) {
        Optional<UserEntity> userEntity= userService.findByUsername(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        GoalEntity goalEntity = userEntity.get().getGoalEntity();
        MovieResponse response=client.getMovies(goalService.composeParams(goalEntity));

        goalService.updatePage(goalEntity, response);

        List<AdviceEntity> adviceList = new ArrayList<>();
        List<MovieResource> movieList = response.results();

        List<MovieEntity> movieEntityList = movieList.stream()
                .map(movieDto -> movieMapper.toMovieEntity(movieDto, genreService))
                .toList();
        List<MovieEntity> savedMovie= movieService.saveAllMovie(movieEntityList);
        for (MovieEntity movie : savedMovie) {
            AdviceEntity adviceEntity = new AdviceEntity();
            adviceEntity.setUserEntity(userEntity.get());

            int daysUntilDeadline = TimeToDedicateConverter.convertTimeToDedicateToDays(goalEntity.getTimeToDedicate());
            adviceEntity.setDeadline(LocalDateTime.now().plusDays(daysUntilDeadline));

            adviceEntity.setPoints(100);//TODO ADD ALGORITM TO CALCULATE POINTS

            adviceEntity.setMovie(movie);
            adviceEntity.setStatus(StatusEnum.UNCOMPLETED.name());
            adviceEntity.setQuizResult(QuizResultEnum.UNCOMPLETED.name());
            adviceList.add(adviceEntity);
        }
        return adviceRepository.saveAll(adviceList);
    }


}
