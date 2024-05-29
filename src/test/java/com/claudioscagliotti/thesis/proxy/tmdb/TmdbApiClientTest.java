package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.dto.tmdb.response.authentication.AuthenticationResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.GenreResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisApplication.class)
class TmdbApiClientTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Autowired
    private TmdbApiClient client;


    @Test
    public void testAuthenticate() throws Exception {
        TmdbApiClient apiClient = client;
        AuthenticationResponse response = apiClient.authenticate();


        assertThat(response.success()).isNotNull();
        assertThat(response.statusCode()).isNotNull();
        assertThat(response.statusMessage()).isNotNull();
    }

    @Test
    public void testGetMovies() throws Exception {
        TmdbApiClient apiClient = client;
        MovieResponse response = apiClient.getMovies("/discover/movie?language=en%7Cit&primary_release_date.gte=2000-01-01&primary_release_date.lte=2020-01-01&with_genres=80,28&with_origin_country=IT");


        assertThat(response.page()).isNotNull();
        assertThat(response.totalResults()).isNotNull();
        assertThat(response.results()).isNotNull();
    }

    @Test
    public void testGenres() throws Exception {
        TmdbApiClient apiClient = client;
        GenreResponse response = apiClient.getGenres();


        assertThat(response.genres()).isNotNull();
        assertThat(response.genres().size()).isNotNull();
    }
}