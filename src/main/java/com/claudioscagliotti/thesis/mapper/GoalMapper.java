package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.CountryOfProductionDto;
import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.dto.response.KeywordDto;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface GoalMapper {
    @Mapping(source = "goalType", target = "goalType", qualifiedByName = "mapStringToGoalType")
    @Mapping(source = "keywordList", target = "keywordEntityList")
    @Mapping(source = "genreEnumList", target = "genreEntityList", qualifiedByName = "mapGenreEnumListToEntityList")
    @Mapping(source = "countryOfProductionList", target = "countryOfProductionEntityList")
    GoalEntity toGoalEntity(GoalDto dto);

    @Mapping(source = "goalType", target = "goalType", qualifiedByName = "mapGoalTypeToString")
    @Mapping(source = "keywordEntityList", target = "keywordList")
    @Mapping(source = "genreEntityList", target = "genreEnumList", qualifiedByName = "mapEntityListToGenreEnumList")
    @Mapping(source = "countryOfProductionEntityList", target = "countryOfProductionList")
    GoalDto toGoalDto(GoalEntity goalEntity);

    KeywordEntity toKeywordEntity(KeywordDto dto);

    KeywordDto toKeywordDto(KeywordEntity entity);

    CountryOfProductionEntity toCountryOfProductionEntity(CountryOfProductionDto dto);

    CountryOfProductionDto toCountryOfProductionDto(CountryOfProductionEntity entity);

    default List<KeywordEntity> mapKeywordListToEntity(List<KeywordDto> keywordDtos) {
        if (keywordDtos == null) {
            return null;
        }
        return keywordDtos.stream()
                .map(this::toKeywordEntity)
                .collect(Collectors.toList());
    }

    default List<KeywordDto> mapKeywordEntityToList(List<KeywordEntity> keywordEntities) {
        if (keywordEntities == null) {
            return null;
        }
        return keywordEntities.stream()
                .map(this::toKeywordDto)
                .collect(Collectors.toList());
    }

    default List<CountryOfProductionEntity> mapCountryListToEntity(List<CountryOfProductionDto> countryDtos) {
        if (countryDtos == null) {
            return null;
        }
        return countryDtos.stream()
                .map(this::toCountryOfProductionEntity)
                .collect(Collectors.toList());
    }

    default List<CountryOfProductionDto> mapCountryEntityToList(List<CountryOfProductionEntity> countryEntities) {
        if (countryEntities == null) {
            return null;
        }
        return countryEntities.stream()
                .map(this::toCountryOfProductionDto)
                .collect(Collectors.toList());
    }

    @Named("mapGenreEnumListToEntityList")
    default List<GenreEntity> mapGenreEnumListToEntityList(List<GenreEnum> genreEnumList) {
        return genreEnumList.stream()
                .map(genreEnum -> new GenreEntity(genreEnum.getName(), genreEnum.getTmdbId()))
                .collect(Collectors.toList());
    }

    @Named("mapEntityListToGenreEnumList")
    default List<GenreEnum> mapEntityListToGenreEnumList(List<GenreEntity> genreEntityList) {
        return genreEntityList.stream()
                .map(genreEntity -> GenreEnum.getByName(genreEntity.getName()))
                .collect(Collectors.toList());
    }

    @Named("mapStringToGoalType")
    default GoalTypeEntity mapStringToGoalType(String value) {
        if (value == null) {
            return null;
        }
        GoalTypeEnum goalTypeEnum = GoalTypeEnum.valueOf(value.toUpperCase());
        GoalTypeEntity goalType = new GoalTypeEntity();
        goalType.setType(goalTypeEnum);
        goalType.setId(goalTypeEnum.getValue());
        return goalType;
    }

    @Named("mapGoalTypeToString")
    default String mapGoalTypeToString(GoalTypeEntity goalType) {
        if (goalType == null || goalType.getType() == null) {
            return null;
        }
        return goalType.getType().name();
    }
}