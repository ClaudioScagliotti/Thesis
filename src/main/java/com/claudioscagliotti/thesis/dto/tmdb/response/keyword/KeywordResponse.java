package com.claudioscagliotti.thesis.dto.tmdb.response.keyword;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record KeywordResponse(
		@JsonProperty("page") Integer page,
		@JsonProperty("total_pages") Integer totalPages,
		List<KeywordDto> results,
		@JsonProperty("total_results") Integer totalResults
) {}