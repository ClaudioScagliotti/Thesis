package com.claudioscagliotti.thesis.dto.tmdb.response.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
public record MovieResource(
		String overview,
		@JsonProperty("original_language") String originalLanguage,
		@JsonProperty("original_title") String originalTitle,
		Boolean video,
		String title,
		@JsonProperty("genre_ids") List<Integer> genreIds,
		@JsonProperty("poster_path") String posterPath,
		@JsonProperty("backdrop_path") String backdropPath,
		@JsonProperty("release_date") String releaseDate,
		Float popularity,
		@JsonProperty("vote_average") Float voteAverage,
		Integer id,
		Boolean adult,
		@JsonProperty("vote_count") Integer voteCount
) {}