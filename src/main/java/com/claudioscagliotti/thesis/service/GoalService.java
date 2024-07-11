package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.enumeration.tmdb.MetohdEnum;
import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.mapper.GoalMapper;
import com.claudioscagliotti.thesis.model.*;
import com.claudioscagliotti.thesis.repository.CountryOfProductionRepository;
import com.claudioscagliotti.thesis.repository.GoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for managing operations related to goals.
 */
@Service
public class GoalService {

    private final UserService userService;
    private final GoalRepository goalRepository;
    private final CountryOfProductionService countryOfProductionService;
    private final GenreService genreService;
    private final GoalMapper goalMapper;
    private final CountryOfProductionRepository countryOfProductionRepository;
    private final KeywordService keywordService;

    /**
     * Constructs a GoalService instance with the provided dependencies.
     *
     * @param userService                   The UserService dependency.
     * @param goalRepository                The GoalRepository dependency.
     * @param countryOfProductionService    The CountryOfProductionService dependency.
     * @param genreService                  The GenreService dependency.
     * @param goalMapper                    The GoalMapper dependency.
     * @param countryOfProductionRepository The CountryOfProductionRepository dependency.
     * @param keywordService                The KeywordService dependency.
     */
    public GoalService(UserService userService, GoalRepository goalRepository,
                       CountryOfProductionService countryOfProductionService, GenreService genreService,
                       GoalMapper goalMapper, CountryOfProductionRepository countryOfProductionRepository,
                       KeywordService keywordService) {
        this.userService = userService;
        this.goalRepository = goalRepository;
        this.countryOfProductionService = countryOfProductionService;
        this.genreService = genreService;
        this.goalMapper = goalMapper;
        this.countryOfProductionRepository = countryOfProductionRepository;
        this.keywordService = keywordService;
    }

    /**
     * Creates a new goal based on the provided GoalDto and associates it with the user.
     *
     * @param request  The GoalDto containing goal details.
     * @param username The username of the user to associate the goal with.
     * @return The created GoalDto.
     */
    public GoalDto createGoal(GoalDto request, String username) {
        GoalEntity goalEntity = saveGoal(request);
        userService.updateUserGoal(username, goalEntity);
        return goalMapper.toGoalDto(goalEntity);
    }

    /**
     * Retrieves the goal associated with the specified user.
     *
     * @param username The username of the user.
     * @return The GoalDto associated with the user.
     */
    public GoalDto getGoalByUser(String username) {
        UserEntity userEntity = userService.findByUsername(username);
        return goalMapper.toGoalDto(userEntity.getGoalEntity());
    }

    /**
     * Composes query parameters for API requests based on the provided goal entity.
     *
     * @param goalEntity The goal entity containing criteria for API queries.
     * @return A string of concatenated query parameters.
     */
    public String composeParams(GoalEntity goalEntity) {
        String result;
        // TYPE
        result = setGoalType(goalEntity);
        // LANGUAGE
        result += "?" + QueryParamEnum.LANGUAGE.getValue() + "en%7Cit";
        // DATE
        result += setDates(goalEntity);
        // GENRES
        result += genreService.createGenreIds(goalEntity);
        // COUNTRY OF PRODUCTION
        result += countryOfProductionService.getCountryCodesAsString(goalEntity.getCountryOfProductionEntityList());
        // KEYWORDS
        result += "&" + QueryParamEnum.WITH_KEYWORDS.getValue() + goalEntity.getKeywordEntityList().stream()
                .map(KeywordEntity::getTmdbId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        result += "&" + QueryParamEnum.PAGE.getValue() + goalEntity.getPage();

        return result;
    }

    /**
     * Sets the goal type parameter based on the goal entity's type.
     *
     * @param goalEntity The goal entity containing the type.
     * @return The formatted goal type parameter.
     */
    private static String setGoalType(GoalEntity goalEntity) {
        String result;

        switch (goalEntity.getGoalType().getType()) {
            case NOW_PLAYING -> {
                result = MetohdEnum.NOW_PLAYING.getValue();
            }
            case MOST_POPULAR -> {
                result = MetohdEnum.MOST_POPULAR.getValue();
            }
            case TOP_RATED -> {
                result = MetohdEnum.TOP_RATED.getValue();
            }
            default -> {
                result = MetohdEnum.DISCOVER.getValue();
            }
        }
        return result;
    }

    /**
     * Sets date range parameters based on the provided goal entity's date criteria.
     *
     * @param goalEntity The goal entity containing date criteria.
     * @return The formatted date range parameters.
     */
    private static String setDates(GoalEntity goalEntity) {
        String result = "";
        LocalDate lte;
        LocalDate gte;
        if (goalEntity.getMinYear() != null) {
            gte = createDate(goalEntity.getMinYear());
            result = "&" + QueryParamEnum.PRIMARY_RELEASE_DATE_GTE.getValue() + gte;
        }
        if (goalEntity.getMaxYear() != null) {
            lte = createDate(goalEntity.getMaxYear());
            result += "&" + QueryParamEnum.PRIMARY_RELEASE_DATE_LTE.getValue() + lte;
        }
        return result;
    }

    /**
     * Creates a LocalDate object with the provided year.
     *
     * @param year The year to create the date object.
     * @return The LocalDate object representing the start of the year.
     */
    private static LocalDate createDate(Integer year) {
        return LocalDate.of(year, 1, 1);
    }

    /**
     * Saves a goal entity including associated entities such as countries of production, genres, and keywords.
     *
     * @param dto The GoalDto containing data to save.
     * @return The saved GoalEntity.
     */
    @Transactional
    public GoalEntity saveGoal(GoalDto dto) {
        GoalEntity goalEntity = goalMapper.toGoalEntity(dto);

        goalEntity.getCountryOfProductionEntityList().forEach(c -> countryOfProductionRepository.getCountryOfProductionByCountryCode(c.getCountryCode()));
        List<CountryOfProductionEntity> countryEntities = goalEntity.getCountryOfProductionEntityList().stream()
                .map(c -> countryOfProductionRepository.getCountryOfProductionByCountryCode(c.getCountryCode()))
                .toList();
        goalEntity.setCountryOfProductionEntityList(countryEntities);

        List<GenreEntity> savedGenreEntities = goalEntity.getGenreEntityList().stream()
                .map(genreService::getGenreByNameAndSaveIfNotExists)
                .collect(Collectors.toList());
        goalEntity.setGenreEntityList(savedGenreEntities);

        goalEntity.setKeywordEntityList(keywordService.saveAll(goalEntity.getKeywordEntityList()));
        // I already have the tmdb keyword ids because I do this process first
        return goalRepository.save(goalEntity);
    }

    /**
     * Updates an existing goal entity in the database.
     *
     * @param entity The updated GoalEntity to save.
     * @return The updated GoalEntity.
     */
    public GoalEntity updateGoal(GoalEntity entity) {
        return goalRepository.save(entity);
    }

    /**
     * Updates the page number of a goal entity based on the response from an external API.
     *
     * @param goalEntity The goal entity to update.
     * @param response   The MovieResponse containing API response data.
     */
    public void updatePage(GoalEntity goalEntity, MovieResponse response) {
        if (response.totalPages() - response.page() > 1) {
            goalEntity.setPage(goalEntity.getPage() + 1);
            updateGoal(goalEntity);
        }
    }
}
