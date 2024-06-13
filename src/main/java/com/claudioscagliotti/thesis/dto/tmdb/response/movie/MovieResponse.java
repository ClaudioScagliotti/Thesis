package com.claudioscagliotti.thesis.dto.tmdb.response.movie;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record MovieResponse(
		@JsonProperty("page") Integer page,
		@JsonProperty("total_pages") Integer totalPages,
		List<MovieResource> results,
		@JsonProperty("total_results") Integer totalResults
) {}