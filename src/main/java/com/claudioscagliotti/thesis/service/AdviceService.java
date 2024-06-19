package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.enumeration.AdviceStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdviceService {

    private static final double SUCCESS_PERCENTAGE = 51;
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

    public List<AdviceDto> getAdviceListResponse(String username){
        List<AdviceEntity> adviceList = createAdviceList(username);
        return adviceMapper.toAdviceDtoList(adviceList);
    }
    public AdviceDto getNextAdvice(String username){
        Pageable pageable = PageRequest.of(0, 1);
        List<AdviceEntity> adviceEntities = adviceRepository.findUncompletedAdviceByUsername(username, pageable);
        if(!adviceEntities.isEmpty()){
            AdviceEntity adviceEntity = adviceEntities.get(0);
            GoalEntity goalEntity = userRepository.getGoalEntityByUsername(username);
            int daysUntilDeadline = TimeToDedicateConverter.convertTimeToDedicateToDays(goalEntity.getTimeToDedicate());
            adviceRepository.updateAdviceDeadline(adviceEntity.getId(),LocalDateTime.now().plusDays(daysUntilDeadline));
            Optional<AdviceEntity> adviceEntityUpdated = adviceRepository.findById(adviceEntity.getId());
            if(adviceEntityUpdated.isPresent()){
                return adviceMapper.toAdviceDto(adviceEntityUpdated.get());
            }
        }
        // TODO GESTIRE CASO IN CUI SIANO FINITI GLI ADVICE
        return null;

    }

    public List<AdviceEntity> createAdviceList(String username) {
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
        return adviceRepository.saveAll(adviceList);
    }

    public AdviceDto completeAdvice(String username, Long adviceId){
        Optional<AdviceEntity> adviceEntity = adviceRepository.findById(adviceId);

        if(adviceEntity.isPresent()){
            AdviceEntity entity = adviceEntity.get();

            checkUsername(username, entity.getUserEntity().getUsername());
            double quizCorrectPercentage = PercentageCalculatorUtil.calculateSucceededPercentage(entity.getQuizzes());
            if(quizCorrectPercentage>SUCCESS_PERCENTAGE)
            {
                updateQuizResult(entity.getId(), QuizResultEnum.SUCCEEDED);
            }
            else {
                updateQuizResult(entity.getId(), QuizResultEnum.FAILED);
            }

            if(entity.getDeadline().isAfter(LocalDateTime.now()) &&
                    quizCorrectPercentage>SUCCESS_PERCENTAGE){
                entity.setStatus(AdviceStatusEnum.COMPLETED);
            }
            else{
                entity.setStatus(AdviceStatusEnum.FAILED);
            }
            return updateAdviceStatus(entity);
        }else throw new EntityNotFoundException();

    }

    private AdviceDto updateAdviceStatus(AdviceEntity entity) {
        adviceRepository.updateStatus(entity.getId(), entity.getStatus());
        Optional<AdviceEntity> updatedEntity = adviceRepository.findById(entity.getId());
        if(updatedEntity.isPresent()){
            return adviceMapper.toAdviceDto(updatedEntity.get());
        }
        throw new EntityNotFoundException();
    }

    private static void checkUsername(String username, String entityUsername) {
        if(!entityUsername.equals(username)){
            throw new UnauthorizedUserException();
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
            return updateAdviceStatus(entity);

        }else throw new EntityNotFoundException();
    }

    public void updateQuizResult(Long adviceId, QuizResultEnum resultEnum){
        adviceRepository.updateQuizResult(adviceId, resultEnum);
    }


}
