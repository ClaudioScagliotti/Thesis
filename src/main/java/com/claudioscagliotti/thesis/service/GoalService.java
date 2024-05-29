package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
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

    public GoalService(GoalRepository goalRepository, CountryOfProductionService countryOfProductionService, GenreService genreService) {
        this.goalRepository = goalRepository;
        this.countryOfProductionService = countryOfProductionService;
        this.genreService = genreService;
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
            result = "&" + QueryParamEnum.PRIMARY_RELEASE_DATE_GTE.getValue() + gte.toString();
        }
        if (goalEntity.getMaxYear() != null) {
            lte = createDate(goalEntity.getMaxYear());
            result += "&" + QueryParamEnum.PRIMARY_RELEASE_DATE_LTE.getValue() + lte.toString();
        }
        return result;
    }

    private static LocalDate createDate(Integer year) {
        return LocalDate.of(year, 01, 01);
    }
}
