package com.claudioscagliotti.thesis.dto.tmdb.response.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


public record MovieResponse(
		@JsonProperty("page") Integer page,
		@JsonProperty("total_pages") Integer totalPages,
		List<Movie> results,
		@JsonProperty("total_results") Integer totalResults
) {}