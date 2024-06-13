package com.claudioscagliotti.thesis.dto.response;

import com.claudioscagliotti.thesis.enumeration.tmdb.GenreEnum;

import java.util.List;

public record MovieDto(String overview,
                       String originalLanguage,
                       String originalTitle,
                       String title,
                       List<GenreEnum> genreIds,
                       String posterPath,
                       String backdropPath,
                       String releaseDate,
                       Float popularity,
                       Float voteAverage,
                       Integer id
) {
}
