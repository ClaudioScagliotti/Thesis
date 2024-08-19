package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.dto.tmdb.response.authentication.AuthenticationResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.GenreResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResource;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class TmdbApiClientTest {

    @MockBean
    @Qualifier("tmdbRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private TmdbApiClient client;

    @Test
    public void testAuthenticateWithMockResponse() throws Exception {
        AuthenticationResource mockResponse = new AuthenticationResource(true,200, "Success" );

        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(new ObjectMapper().writeValueAsString(mockResponse), HttpStatus.OK));

        AuthenticationResource response = client.authenticate();

        then(response).isNotNull();
        then(response.success()).isTrue();
        then( response.statusCode()).isEqualTo(200);
    }

    @Test
    public void testGetMoviesWithMockResponse() throws Exception {
        List<Long> genreIds = List.of(28L, 12L, 16L);

        MovieResource movieResource = new MovieResource(
                "An epic journey of a hero.",
                "en",
                "The Hero's Journey",
                false,
                "Heroic Adventures",
                genreIds,
                "/path/to/poster.jpg",
                "/path/to/backdrop.jpg",
                "2023-08-18",
                85.5f,
                8.7f,
                12345,
                false,
                9876
        );

        MovieResponse mockMovieResponse = new MovieResponse(1, 10, List.of(movieResource), 1000);

        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(new ObjectMapper().writeValueAsString(mockMovieResponse), HttpStatus.OK));

        MovieResponse response = client.getMovies("/discover/movie?language=en|it");

        then(response).isNotNull();
        then(response.page()).isEqualTo(1);
        then(response.totalPages()).isEqualTo(10);
    }

    @Test
    public void testSearchKeywordsWithMockResponse() throws Exception {
        KeywordResponse mockKeywordResponse = new KeywordResponse(1, 1, List.of(), 0);
        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(new ObjectMapper().writeValueAsString(mockKeywordResponse), HttpStatus.OK));
        KeywordResponse response = client.searchKeywords("inception");


        then(response).isNotNull();
        then(response.results()).isEmpty();
        then(response.page()).isEqualTo(1);
        then(response.totalPages()).isEqualTo(1);
        then(response.totalResults()).isEqualTo(0);
    }

    @Test
    public void testGetGenresWithMockResponse() throws Exception {
        GenreResponse mockGenreResponse = new GenreResponse(List.of());

        Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(), Mockito.eq(String.class)))
                .thenReturn(new ResponseEntity<>(new ObjectMapper().writeValueAsString(mockGenreResponse), HttpStatus.OK));

        GenreResponse response = client.getGenres();

        then(response).isNotNull();
        then(response.genres().isEmpty()).isTrue();
    }
}
