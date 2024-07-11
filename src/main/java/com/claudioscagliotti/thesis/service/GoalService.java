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

@Service
public class GoalService {
    private final UserService userService;
    private final GoalRepository goalRepository;
    private final CountryOfProductionService countryOfProductionService;
    private final GenreService genreService;
    private final GoalMapper goalMapper;
    private final CountryOfProductionRepository countryOfProductionRepository;
    private final KeywordService keywordService;

    public GoalService(UserService userService, GoalRepository goalRepository, CountryOfProductionService countryOfProductionService, GenreService genreService, GoalMapper goalMapper, CountryOfProductionRepository countryOfProductionRepository, KeywordService keywordService) {
        this.userService = userService;
        this.goalRepository = goalRepository;
        this.countryOfProductionService = countryOfProductionService;
        this.genreService = genreService;
        this.goalMapper = goalMapper;
        this.countryOfProductionRepository = countryOfProductionRepository;
        this.keywordService = keywordService;
    }

    public GoalDto createGoal(GoalDto request, String username) {
        GoalEntity goalEntity = saveGoal(request);
        userService.updateUserGoal(username, goalEntity);
        return goalMapper.toGoalDto(goalEntity);
    }
    public GoalDto getGoalByUser(String username){
        UserEntity userEntity = userService.findByUsername(username);
        return goalMapper.toGoalDto(userEntity.getGoalEntity());
    }

    public String composeParams(GoalEntity goalEntity) {
        String result;
        //TYPE
        result = setGoalType(goalEntity);
        //LANGUAGE
        result += "?" + QueryParamEnum.LANGUAGE.getValue() + "en%7Cit";
        //DATE
        result += setDates(goalEntity);
        //GENRES
        result += genreService.createGenreIds(goalEntity);
        //COUNTRY OF PRODUCTION
        result += countryOfProductionService.getCountryCodesAsString(goalEntity.getCountryOfProductionEntityList());
        //KEYWORDS
        result += "&" + QueryParamEnum.WITH_KEYWORDS.getValue() + goalEntity.getKeywordEntityList().stream()
                .map(KeywordEntity::getTmdbId)
                .map(String::valueOf)
                .collect(Collectors.joining("|"));

        result += "&"+ QueryParamEnum.PAGE.getValue()+goalEntity.getPage();

        return result;
    }

    private static String setGoalType(GoalEntity goalEntity) {
        String result;

        switch (goalEntity.getGoalType().getType()) {
            case NOW_PLAYING: {
                result = MetohdEnum.NOW_PLAYING.getValue();
            }
            case MOST_POPULAR: {
                result = MetohdEnum.MOST_POPULAR.getValue();
            }
            case TOP_RATED: {
                result = MetohdEnum.TOP_RATED.getValue();
            }
            default: {
                result = MetohdEnum.DISCOVER.getValue();
            }
        }
        return result;
    }

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

    private static LocalDate createDate(Integer year) {
        return LocalDate.of(year, 1, 1);
    }
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
        // mi arrivano già gli id di tmdb perchè questo processo lo faccio prima

        return goalRepository.save(goalEntity);
    }
    public GoalEntity updateGoal(GoalEntity entity){
        return goalRepository.save(entity);
    }
    public void updatePage(GoalEntity goalEntity, MovieResponse response) {
        if(response.totalPages()- response.page()>1){
            goalEntity.setPage(goalEntity.getPage()+1);
            updateGoal(goalEntity);
        }
    }
}
