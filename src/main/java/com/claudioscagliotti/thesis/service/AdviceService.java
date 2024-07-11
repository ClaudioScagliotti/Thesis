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


/**
 * Service class for managing advice generation and user interactions.
 */
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

    /**
     * Constructs an AdviceService with the required dependencies.
     *
     * @param adviceRepository The repository for managing AdviceEntity instances.
     * @param adviceMapper     The mapper for converting AdviceEntity to AdviceDto and vice versa.
     * @param goalService      The service for managing user goals.
     * @param client           The API client for fetching movie data.
     * @param movieMapper      The mapper for converting MovieResource to MovieEntity.
     * @param movieService     The service for managing MovieEntity instances.
     * @param userRepository   The repository for managing UserEntity instances.
     * @param genreService     The service for managing movie genres.
     * @param userService      The service for managing UserEntity instances.
     */
    public AdviceService(AdviceRepository adviceRepository, AdviceMapper adviceMapper, GoalService goalService,
                         TmdbApiClient client, MovieMapper movieMapper, MovieService movieService,
                         UserRepository userRepository, GenreService genreService, UserService userService) {
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

    /**
     * Retrieves the next advice for the specified user.
     *
     * @param username The username of the user.
     * @return The next AdviceDto for the user.
     * @throws NoAdviceAvailableException If no uncompleted advices are available for the user.
     */
    public AdviceDto getNextAdvice(String username) {
        Pageable pageable = PageRequest.of(0, 1);
        List<AdviceEntity> adviceEntities = adviceRepository.findUncompletedAdviceByUsername(username, pageable);

        if (!adviceEntities.isEmpty()) {
            AdviceEntity adviceEntity = adviceEntities.get(0);
            GoalEntity goalEntity = userRepository.getGoalEntityByUsername(username);
            int daysUntilDeadline = TimeToDedicateConverter.convertTimeToDedicateToDays(goalEntity.getTimeToDedicate());

            adviceEntity.setDeadline(LocalDateTime.now().plusDays(daysUntilDeadline));
            return adviceMapper.toAdviceDto(adviceRepository.save(adviceEntity));
        } else {
            throw new NoAdviceAvailableException("There are no uncompleted advices for the user with username: " + username);
        }
    }

    /**
     * Creates a list of advice for the specified user based on their goals.
     *
     * @param username The username of the user.
     * @return The list of created AdviceDto instances.
     */
    public List<AdviceDto> createAdviceList(String username) {
        UserEntity userEntity = userService.findByUsername(username);
        GoalEntity goalEntity = userEntity.getGoalEntity();
        MovieResponse response = client.getMovies(goalService.composeParams(goalEntity));
        goalService.updatePage(goalEntity, response);

        List<AdviceEntity> adviceList = new ArrayList<>();
        List<MovieResource> movieList = response.results();

        List<MovieEntity> movieEntityList = movieList.stream()
                .map(movieDto -> movieMapper.toMovieEntity(movieDto, genreService))
                .toList();

        List<MovieEntity> savedMovies = movieService.saveAllMovie(movieEntityList);

        for (MovieEntity movie : savedMovies) {
            AdviceEntity adviceEntity = new AdviceEntity();
            adviceEntity.setUserEntity(userEntity);
            adviceEntity.setPoints(100); // TODO: Implement algorithm to calculate points
            adviceEntity.setMovie(movie);
            adviceEntity.setStatus(AdviceStatusEnum.UNCOMPLETED);
            adviceEntity.setQuizResult(QuizResultEnum.UNCOMPLETED);
            adviceList.add(adviceEntity);
        }

        return adviceMapper.toAdviceDto(adviceRepository.saveAll(adviceList));
    }

    /**
     * Marks the specified advice as completed for the user.
     *
     * @param username The username of the user.
     * @param adviceId The ID of the advice to complete.
     * @return The completed AdviceDto.
     * @throws EntityNotFoundException   If no advice with the specified ID exists.
     * @throws UnauthorizedUserException If the user is not authorized to complete the advice.
     */
    public AdviceDto completeAdvice(String username, Long adviceId) {
        Optional<AdviceEntity> advice = adviceRepository.findById(adviceId);

        if (advice.isPresent()) {
            AdviceEntity adviceEntity = advice.get();
            checkUsername(username, adviceEntity.getUserEntity().getUsername());

            double quizCorrectPercentage = PercentageCalculatorUtil.calculateSucceededPercentage(adviceEntity.getQuizzes());

            if (quizCorrectPercentage > successPercentage) {
                adviceEntity.setQuizResult(QuizResultEnum.SUCCEEDED);
            } else {
                adviceEntity.setQuizResult(QuizResultEnum.FAILED);
            }

            if (adviceEntity.getDeadline().isAfter(LocalDateTime.now()) &&
                    quizCorrectPercentage > successPercentage) {
                adviceEntity.setStatus(AdviceStatusEnum.COMPLETED);
                userService.addPoints(userService.findByUsername(username), adviceEntity.getPoints());
            } else {
                adviceEntity.setStatus(AdviceStatusEnum.FAILED);
            }

            return adviceMapper.toAdviceDto(adviceRepository.save(adviceEntity));
        } else {
            throw new EntityNotFoundException("There is no advice with id: " + adviceId);
        }
    }

    /**
     * Skips the specified advice for the user.
     *
     * @param username The username of the user.
     * @param adviceId The ID of the advice to skip.
     * @return The skipped AdviceDto.
     * @throws EntityNotFoundException   If no advice with the specified ID exists.
     * @throws UnauthorizedUserException If the user is not authorized to skip the advice.
     */
    public AdviceDto skipAdvice(String username, Long adviceId) {
        Optional<AdviceEntity> adviceEntity = adviceRepository.findById(adviceId);

        if (adviceEntity.isPresent()) {
            AdviceEntity entity = adviceEntity.get();
            checkUsername(username, entity.getUserEntity().getUsername());

            if (entity.getDeadline().isAfter(LocalDateTime.now())) {
                entity.setStatus(AdviceStatusEnum.SKIPPED);
            } else {
                entity.setStatus(AdviceStatusEnum.FAILED);
            }

            return adviceMapper.toAdviceDto(adviceRepository.save(entity));
        } else {
            throw new EntityNotFoundException("There is no advice with id: " + adviceId);
        }
    }

    /**
     * Retrieves the AdviceEntity with the specified ID.
     *
     * @param adviceId The ID of the advice to retrieve.
     * @return The retrieved AdviceEntity.
     * @throws EntityNotFoundException If no advice with the specified ID exists.
     */
    public AdviceEntity getById(Long adviceId) {
        return adviceRepository.findById(adviceId)
                .orElseThrow(() -> new EntityNotFoundException("There is no Advice with id: " + adviceId));
    }

    /**
     * Checks if the provided username matches the entity username.
     *
     * @param username       The username to check.
     * @param entityUsername The entity username to match against.
     * @throws UnauthorizedUserException If the usernames do not match.
     */
    private static void checkUsername(String username, String entityUsername) {
        if (!entityUsername.equals(username)) {
            throw new UnauthorizedUserException("The user with username: " + username + " is not authorized to perform this action");
        }
    }
}
