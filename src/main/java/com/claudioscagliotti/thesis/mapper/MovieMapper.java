package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieDto;
import com.claudioscagliotti.thesis.model.MovieEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);
    MovieEntity toMovieEntity(MovieDto movieDto);
    MovieDto toMovieDto(MovieEntity movieEntity);
}
