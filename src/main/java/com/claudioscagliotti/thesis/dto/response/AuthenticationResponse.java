package com.claudioscagliotti.thesis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(@JsonProperty("access_token") String accessToken,
                                     @JsonProperty("refresh_token") String refreshToken,
                                     @JsonProperty("message") String message) {
    public AuthenticationResponse(String accessToken, String refreshToken, String message) {
        this.accessToken = accessToken;
        this.message = message;
        this.refreshToken = refreshToken;
    }

    @Override
    public String accessToken() {
        return accessToken;
    }

    @Override
    public String refreshToken() {
        return refreshToken;
    }

    @Override
    public String message() {
        return message;
    }
}
