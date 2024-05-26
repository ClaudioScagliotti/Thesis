package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.dto.tmdb.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class TmdbApiClient {
    private static final String TMDB_API_BASE_URL = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate;
    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.token}")
    private String apiToken;

    public TmdbApiClient() {
        this.restTemplate = new RestTemplate();
    }

    public AuthenticationResponse authenticate() {
        String url = TMDB_API_BASE_URL + "/authentication";
        try{
        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");
        headers.set("Authorization", "Bearer " + this.apiToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationResponse authResponse= objectMapper.readValue(exchange.getBody(), AuthenticationResponse.class);
            if (authResponse.statusCode() == 7) {
                throw new InvalidApiKeyException(authResponse.statusMessage());
            }
            return authResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while authenticating with TMDB API", e);
        }
    }

//    public Movie getMovie(int movieId) {
//        String url = String.format("%s/movie/%d?api_key=%s", TMDB_API_BASE_URL, movieId, apiKey);
//        return restTemplate.getForObject(url, Movie.class);
//    }


}

