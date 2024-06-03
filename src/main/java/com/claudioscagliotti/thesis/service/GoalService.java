package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.enumeration.tmdb.*;
import com.claudioscagliotti.thesis.mapper.GoalMapper;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import com.claudioscagliotti.thesis.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Service
public class GoalService {
    private final GoalRepository goalRepository;
    private final CountryOfProductionService countryOfProductionService;
    private final GenreService genreService;
    private final GoalMapper goalMapper;

    public GoalService(GoalRepository goalRepository, CountryOfProductionService countryOfProductionService, GenreService genreService, GoalMapper goalMapper) {
        this.goalRepository = goalRepository;
        this.countryOfProductionService = countryOfProductionService;
        this.genreService = genreService;
        this.goalMapper = goalMapper;
    }

    public GoalDto createGoal(GoalDto request) {
        GoalEntity goalEntity= goalMapper.INSTANCE.toGoalEntity(request);
        return goalMapper.INSTANCE.toGoalDto(goalRepository.save(goalEntity));
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
                .collect(Collectors.joining(","));
        return result;
    }

    private static String setGoalType(GoalEntity goalEntity) {
        String result;
        switch (goalEntity.getGoalType()) {
            case ("now-playing"): {
                result = "/movie/now_playing";
            }
            case ("most-popular"): {
                result = "/movie/popular";
            }
            case ("top-rated"): {
                result = "/movie/top_rated";
            }
            default: {
                result = "/discover/movie";
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
}
