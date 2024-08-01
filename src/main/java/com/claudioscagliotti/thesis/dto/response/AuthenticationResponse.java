package com.claudioscagliotti.thesis.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthenticationResponse(@JsonProperty("access_token") String accessToken,
                                     @JsonProperty("refresh_token") String refreshToken) {
    public AuthenticationResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
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
}
