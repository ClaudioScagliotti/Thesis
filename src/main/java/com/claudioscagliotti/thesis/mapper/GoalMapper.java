package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.CountryOfProductionDto;
import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.dto.response.KeywordDto;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.model.CountryOfProductionEntity;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring")
public interface GoalMapper {

    GoalMapper INSTANCE = Mappers.getMapper(GoalMapper.class);

      @Mapping(source = "keywordList", target = "keywordEntityList")
      @Mapping(source = "genreEnumList", target = "genreEntityList", qualifiedByName = "mapGenreEnumListToEntityList")
      @Mapping(source = "countryOfProductionList", target = "countryOfProductionEntityList")
      GoalEntity toGoalEntity(GoalDto dto);

      @Mapping(source = "keywordEntityList", target = "keywordList")
      @Mapping(source = "genreEntityList", target = "genreEnumList", qualifiedByName = "mapEntityListToGenreEnumList")
      @Mapping(source = "countryOfProductionEntityList", target = "countryOfProductionList")
      GoalDto toGoalDto(GoalEntity goalEntity);


    KeywordEntity toKeywordEntity(KeywordDto dto);

    KeywordDto toKeywordDto(KeywordEntity entity);

    CountryOfProductionEntity toCountryOfProductionEntity(CountryOfProductionDto dto);

    CountryOfProductionDto toCountryOfProductionDto(CountryOfProductionEntity entity);

    // Metodi aggiuntivi per mappare le liste di KeywordDto e CountryOfProductionDto
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
        // Implementa la logica di mappatura da GenreEnum a GenreEntity
        return genreEnumList.stream()
                .map(genreEnum -> new GenreEntity(genreEnum.getName()))
                .collect(Collectors.toList());
    }

    @Named("mapEntityListToGenreEnumList")
    default List<GenreEnum> mapEntityListToGenreEnumList(List<GenreEntity> genreEntityList) {
        // Implementa la logica di mappatura da GenreEntity a GenreEnum
        return genreEntityList.stream()
                .map(genreEntity -> GenreEnum.getByName(genreEntity.getName()))
                .collect(Collectors.toList());
    }
}
