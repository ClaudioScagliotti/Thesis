package com.claudioscagliotti.thesis.dto.tmdb.response.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(
        @JsonProperty("success") Boolean success,
        @JsonProperty("status_code") Integer statusCode,
        @JsonProperty("status_message") String statusMessage
) {
}
