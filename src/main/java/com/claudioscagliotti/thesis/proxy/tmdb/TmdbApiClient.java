package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.dto.tmdb.response.authentication.AuthenticationResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.GenreResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.exception.ExternalApiException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.NoMovieException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static com.claudioscagliotti.thesis.utility.ConstantsUtil.TMDB_API_BASE_URL;

/**
 * TmdbApiClient is a service class responsible for interacting with the TMDB (The Movie Database) API
 * to perform authentication and retrieve movie-related information such as movies, keywords, and genres.
 *
 * This class provides methods to authenticate with the TMDB API, retrieve movies based on various criteria,
 * search for keywords, and fetch genre information.
 *
 * Example usage:
 * - Authenticate with TMDB API to obtain necessary credentials.
 * - Retrieve a list of movies based on specified criteria.
 * - Search for keywords related to movies in TMDB.
 * - Fetch a list of movie genres available in TMDB.
 *
 * Dependencies:
 * - Requires a configured RestTemplate with appropriate interceptors for API communication.
 * - Utilizes TmdbConfig for obtaining API key and token configurations.
 */
@Service
public class TmdbApiClient {
    private final RestTemplate restTemplate;

    /**
     * Constructs TmdbApiClient with RestTemplate, API key, and API token dependencies.
     *
     * @param restTemplate Configured RestTemplate for making API calls to TMDB.
     */
    public TmdbApiClient(@Qualifier("tmdbRestTemplate") RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    /**
     * Authenticates with the TMDB API using the provided API token.
     *
     * @return AuthenticationResource containing authentication details.
     * @throws InvalidApiKeyException If the API token used for authentication is invalid.
     * @throws ExternalApiException If there is an error communicating with the TMDB API.
     */
    public AuthenticationResource authenticate() {
        String url = TMDB_API_BASE_URL + "/authentication";
        try {
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            AuthenticationResource authResponse = objectMapper.readValue(response.getBody(), AuthenticationResource.class);

            if (authResponse.statusCode() == 7) {
                throw new InvalidApiKeyException(authResponse.statusMessage());
            }
            return authResponse;
        } catch (HttpClientErrorException.Unauthorized e) {
            throw new InvalidApiKeyException("Invalid API token", e);
        } catch (Exception e) {
            throw new ExternalApiException("Error occurred while authenticating with TMDB API", e);
        }
    }


    /**
     * Retrieves a list of movies based on the specified path variable from the TMDB API.
     *
     * @param pathVariable Path variable specifying the endpoint to retrieve movies.
     * @return MovieResponse containing movie information based on the query.
     * @throws InvalidApiKeyException If the API key used for authentication is invalid.
     * @throws NoMovieException       If no movies are found based on the provided filters.
     * @throws ExternalApiException   If there is an error communicating with the TMDB API.
     */
    public MovieResponse getMovies(String pathVariable) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

            String url = TMDB_API_BASE_URL + pathVariable;

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidApiKeyException("Invalid API key provided");
            }

            MovieResponse movieResponse = objectMapper.readValue(response.getBody(), MovieResponse.class);

            if (movieResponse.results().isEmpty()) {
                throw new NoMovieException("No movies found with the given filters");
            }

            return movieResponse;
        } catch (InvalidApiKeyException | NoMovieException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }

    /**
     * Searches for keywords related to movies in the TMDB API.
     *
     * @param keyword Keyword to search for in the TMDB API.
     * @return KeywordResponse containing keyword search results.
     * @throws InvalidApiKeyException If the API key used for authentication is invalid.
     * @throws ExternalApiException   If there is an error communicating with the TMDB API.
     */
    public KeywordResponse searchKeywords(String keyword) {
        String url = TMDB_API_BASE_URL + "/search/keyword?query=" + keyword;
        try {
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidApiKeyException("Invalid API key provided");
            }

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(response.getBody(), KeywordResponse.class);
        } catch (InvalidApiKeyException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }
    /**
     * Retrieves a list of movie genres from the TMDB API.
     *
     * @return GenreResponse containing a list of movie genres.
     * @throws InvalidApiKeyException If the API key used for authentication is invalid.
     * @throws ExternalApiException           If there is an error communicating with the TMDB API.
     */
    public GenreResponse getGenres() {
        String url = TMDB_API_BASE_URL + "/genre/movie/list";
        try {
            HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new InvalidApiKeyException("Invalid API key provided");
            }

            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue(response.getBody(), GenreResponse.class);
        } catch (InvalidApiKeyException e) {
            throw e;
        } catch (Exception e) {
            throw new ExternalApiException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }
}

