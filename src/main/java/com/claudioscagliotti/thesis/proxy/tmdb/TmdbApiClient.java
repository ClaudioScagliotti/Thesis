package com.claudioscagliotti.thesis.proxy.tmdb;

import com.claudioscagliotti.thesis.dto.tmdb.response.authentication.AuthenticationResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.Genre;
import com.claudioscagliotti.thesis.dto.tmdb.response.genre.GenreResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.movie.MovieResponse;
import com.claudioscagliotti.thesis.enumeration.tmdb.QueryParamEnum;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

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

    private String composeParams(GoalEntity goalEntity) {//TODO setto filtri di genere, anni, paese di produzione, genere e tema
        String result="";
        LocalDate gte;
        LocalDate lte;
        //TYPE
        switch (goalEntity.getGoalType()){
            case("now-playing"): {
                result+="/movie/now_playing";
            }
            case("most-popular"): {
                result+="/movie/popular";
            }
            case("top-rated"): {
                result+="/movie/top_rated";
            }
            case("discover"): {
                result+="/discover/movie";
            }
        }
        //LANGUAGE
        result+="?"+ QueryParamEnum.LANGUAGE.getValue()+"en%7Cit";

        //DATE
        if(goalEntity.getMinYear()!=null){
            gte = createDate(goalEntity.getMinYear());
            result+="&"+ QueryParamEnum.PRIMARY_RELEASE_DATE_GTE.getValue()+gte.toString();
        }
        if(goalEntity.getMaxYear()!=null){
            lte = createDate(goalEntity.getMaxYear());
            result+="&"+ QueryParamEnum.PRIMARY_RELEASE_DATE_GTE.getValue()+lte.toString();
        }
        //GENRES
        Map<String, Integer> genreMap = goalEntity.getGenreEntityList().stream()
                .collect(Collectors.toMap(
                        GenreEntity::getName,
                        genreEntity -> getGenres().genres().stream()//TODO:usare i generi salvati nell'enum
                                .filter(genre -> genre.name().equals(genreEntity.getName()))
                                .findFirst()
                                .map(Genre::id)
                                .orElse(0)
                ));

            String genreIds = genreMap.values().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));
            result+= QueryParamEnum.WITH_GENRES+genreIds;
        //TODO: COUNTRY OF PRODUCTION
        // KEYWORDS
        return result;
    }

    private static LocalDate createDate(Integer year) {
        return LocalDate.of(year, 01, 01);
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

    public MovieResponse getMovies(String pathVariable) {
        String url = TMDB_API_BASE_URL +pathVariable;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (exchange.getStatusCode() == HttpStatusCode.valueOf(401)) {
                throw new InvalidApiKeyException(); //TODO gestione eccezioni
            }
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(exchange.getBody(), MovieResponse.class);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }
    public KeywordResponse searchKeywords(String keyword) {
        String url = TMDB_API_BASE_URL + "/search/keyword?query="+keyword;
        try{
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
            throw new RuntimeException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }

    public GenreResponse getGenres() {
        String url = TMDB_API_BASE_URL + "/genre/movie/list";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + this.apiToken);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> exchange = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            GenreResponse response= objectMapper.readValue(exchange.getBody(), GenreResponse.class);

            return response;
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while trying to retrieve data from the TMDB API", e);
        }
    }


}

