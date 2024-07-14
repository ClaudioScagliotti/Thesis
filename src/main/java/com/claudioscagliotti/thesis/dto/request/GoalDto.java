package com.claudioscagliotti.thesis.dto.request;

import com.claudioscagliotti.thesis.dto.response.CountryOfProductionDto;
import com.claudioscagliotti.thesis.dto.response.KeywordDto;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class GoalDto {


    private Long id;
    @NotNull
    private Integer timeToDedicate;
    @NotNull
    private String goalType;
    @NotNull
    private Integer minYear;
    @NotNull
    private Integer maxYear;
    private List<KeywordDto> keywordList;
    private List<GenreEnum> genreEnumList;
    private List<CountryOfProductionDto> countryOfProductionList;

}
