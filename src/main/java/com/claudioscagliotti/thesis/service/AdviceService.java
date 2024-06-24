package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.enumeration.AdviceStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.exception.NoAdviceAvailableException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.mapper.AdviceMapper;
import com.claudioscagliotti.thesis.mapper.MovieMapper;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import com.claudioscagliotti.thesis.repository.AdviceRepository;
import com.claudioscagliotti.thesis.repository.UserRepository;
import com.claudioscagliotti.thesis.utility.PercentageCalculatorUtil;
import com.claudioscagliotti.thesis.utility.TimeToDedicateConverter;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdviceService {

    @Value("${success.percentage}")
    private double successPercentage;
    private final AdviceRepository adviceRepository;
    private final AdviceMapper adviceMapper;
    private final GoalService goalService;
    private final TmdbApiClient client;
    private final MovieMapper movieMapper;
    private final MovieService movieService;
    private final UserRepository userRepository;
    private final GenreService genreService;
    private final UserService userService;

    public AdviceService(AdviceRepository adviceRepository, AdviceMapper adviceMapper, GoalService goalService, TmdbApiClient client, MovieMapper movieMapper, UserDetailsServiceImpl userDetailsService, MovieService movieService, UserRepository userRepository,  GenreService genreService, UserService userService) {
        this.adviceRepository = adviceRepository;
        this.adviceMapper = adviceMapper;
        this.goalService = goalService;
        this.client = client;
        this.movieMapper = movieMapper;
        this.movieService = movieService;
        this.userRepository = userRepository;
        this.genreService = genreService;
        this.userService = userService;
    }

    public AdviceDto getNextAdvice(String username){
        Pageable pageable = PageRequest.of(0, 1);
        List<AdviceEntity> adviceEntities = adviceRepository.findUncompletedAdviceByUsername(username, pageable);
        if(!adviceEntities.isEmpty()){

            AdviceEntity adviceEntity = adviceEntities.get(0);
            GoalEntity goalEntity = userRepository.getGoalEntityByUsername(username);
            int daysUntilDeadline = TimeToDedicateConverter.convertTimeToDedicateToDays(goalEntity.getTimeToDedicate());

            adviceEntity.setDeadline(LocalDateTime.now().plusDays(daysUntilDeadline));
            return adviceMapper.toAdviceDto(adviceRepository.save(adviceEntity));

        }
        else throw new NoAdviceAvailableException("There are not uncompleted advices for the user with username: "+username);
    }

    public List<AdviceDto> createAdviceList(String username) {
        UserEntity userEntity= userService.findByUsername(username);

        GoalEntity goalEntity = userEntity.getGoalEntity();
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
            adviceEntity.setUserEntity(userEntity);

            adviceEntity.setPoints(100);// TODO ADD ALGORITM TO CALCULATE POINTS

            adviceEntity.setMovie(movie);
            adviceEntity.setStatus(AdviceStatusEnum.UNCOMPLETED);
            adviceEntity.setQuizResult(QuizResultEnum.UNCOMPLETED);
            adviceList.add(adviceEntity);
        }
        return adviceMapper.toAdviceDto(adviceRepository.saveAll(adviceList));
    }

    public AdviceDto completeAdvice(String username, Long adviceId){
        Optional<AdviceEntity> advice = adviceRepository.findById(adviceId);

        if(advice.isPresent()){
            AdviceEntity adviceEntity = advice.get();

            checkUsername(username, adviceEntity.getUserEntity().getUsername());

            double quizCorrectPercentage = PercentageCalculatorUtil.calculateSucceededPercentage(adviceEntity.getQuizzes());

            if(quizCorrectPercentage>successPercentage) {
                adviceEntity.setQuizResult(QuizResultEnum.SUCCEEDED);
            }
            else {
                adviceEntity.setQuizResult(QuizResultEnum.FAILED);
            }

            if(adviceEntity.getDeadline().isAfter(LocalDateTime.now()) &&
                    quizCorrectPercentage>successPercentage){
                adviceEntity.setStatus(AdviceStatusEnum.COMPLETED);

                userService.addPoints(userService.findByUsername(username),adviceEntity.getPoints());
            }
            else{
                adviceEntity.setStatus(AdviceStatusEnum.FAILED);
            }

            return adviceMapper.toAdviceDto(adviceRepository.save(adviceEntity));

        } else throw new EntityNotFoundException("There is not an advice with id: "+adviceId);

    }


    private static void checkUsername(String username, String entityUsername) {
        if(!entityUsername.equals(username)){
            throw new UnauthorizedUserException("The user with username: "+username+" is not authorized to make this action");
        }
    }

    public AdviceDto skipAdvice(String username, Long adviceId){
        Optional<AdviceEntity> adviceEntity = adviceRepository.findById(adviceId);
        if(adviceEntity.isPresent()){
            AdviceEntity entity = adviceEntity.get();
            checkUsername(username, entity.getUserEntity().getUsername());
            if(entity.getDeadline().isAfter(LocalDateTime.now())){
                entity.setStatus(AdviceStatusEnum.SKIPPED);
            }
            else {
                entity.setStatus(AdviceStatusEnum.FAILED);
            }
            return adviceMapper.toAdviceDto(adviceRepository.save(entity));

        } else throw new EntityNotFoundException("There is no advice with id: "+adviceId);
    }

    public AdviceEntity getById(Long adviceId) {
        return adviceRepository.findById(adviceId)
                .orElseThrow(() -> new EntityNotFoundException("There is not an Advice with id: " + adviceId));
    }


}
