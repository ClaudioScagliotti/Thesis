package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.MovieDto;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResource;
import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.MovieEntity;
import com.claudioscagliotti.thesis.service.GenreService;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring", uses = GenreService.class)
public interface MovieMapper {

    @Mapping(source = "id", target = "tmdbId")
    @Mapping(source = "genreIds", target = "genreEntities", qualifiedByName = "mapGenreIdsToEntities")
    MovieEntity toMovieEntity(MovieResource movieResource, @Context GenreService genreService);

    @Mapping(source = "tmdbId", target = "id")
    @Mapping(source = "genreEntities", target = "genreIds", qualifiedByName = "mapGenreEntitiesToIds")
    MovieResource toMovieResource(MovieEntity movieEntity, @Context GenreService genreService);

    @Mapping(source = "genreEntities", target = "genreIds", qualifiedByName = "mapGenreEntitiesToEnums")
    MovieDto toMovieDto(MovieEntity movieEntity);

    @Named("mapGenreIdsToEntities")
    default List<GenreEntity> mapGenreIdsToEntities(List<Integer> genreIds, @Context GenreService genreService) {
        return genreService.mapGenreIdsToEntities(genreIds);
    }

    @Named("mapGenreEntitiesToIds")
    default List<Integer> mapGenreEntitiesToIds(List<GenreEntity> genreEntities, @Context GenreService genreService) {
        return genreService.mapGenreEntitiesToIds(genreEntities);
    }

    @Named("mapGenreEntitiesToEnums")
    default List<GenreEnum> mapGenreEntitiesToEnums(List<GenreEntity> genreEntities) {
        if (genreEntities == null || genreEntities.isEmpty()) {
            return null;
        }
        return genreEntities.stream()
                .map(genreEntity -> GenreEnum.fromTmdbId(genreEntity.getTmdbId()))
                .collect(Collectors.toList());
    }
}
