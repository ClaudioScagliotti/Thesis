package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.dto.tmdb.response.authentication.AuthenticationResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.GenreResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.exception.ExternalAPIException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.NoMovieException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
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


    public AuthenticationResource authenticate() {
        String url = TMDB_API_BASE_URL + "/authentication";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            AuthenticationResource authResponse = objectMapper.readValue(exchange.getBody(), AuthenticationResource.class);
            if (authResponse.statusCode() == 7) {
                throw new InvalidApiKeyException(authResponse.statusMessage());
            }
            return authResponse;
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while authenticating with TMDB API", e);
        }
    }

    public MovieResponse getMovies(String pathVariable) {//TODO TEST
        MovieResponse response;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> entity = new HttpEntity<>(headers);


                String url = TMDB_API_BASE_URL + pathVariable;

                ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                response = objectMapper.readValue(exchange.getBody(), MovieResponse.class);
            if (exchange.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidApiKeyException();
            }

            // Gestione di risposta vuota
            if (response.results().isEmpty()) {
                throw new NoMovieException("No movies found with the given filters");
            }

        } catch (NoMovieException e) {
            throw new RuntimeException("No movies found: " + e.getMessage(), e);
        } catch (InvalidApiKeyException e) {

            throw new RuntimeException("Invalid API key provided", e);
        } catch (Exception e) {
            // Gestione generica per tutte le altre eccezioni
            throw new RuntimeException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
        return response;
    }

    public KeywordResponse searchKeywords(String keyword) {
        String url = TMDB_API_BASE_URL + "/search/keyword?query=" + keyword;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (exchange.getStatusCode() == HttpStatusCode.valueOf(401)) {
                throw new InvalidApiKeyException(); //TODO gestione eccezioni
            }
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(exchange.getBody(), KeywordResponse.class);
        } catch (Exception e) {
            throw new ExternalAPIException("An error occurred while trying to retrieve data from the TMDB API");
        }
    }

    public GenreResponse getGenres() {
        String url = TMDB_API_BASE_URL + "/genre/movie/list";
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(exchange.getBody(), GenreResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }


}

