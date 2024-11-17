package com.claudioscagliotti.thesis.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for TMDB API settings and RestTemplate configuration.
 * This class provides configuration for accessing TMDB API using a RestTemplate with custom headers
 * including authorization token.
 */
@Getter
@Configuration
public class TmdbConfig {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.token}")
    private String apiToken;

    /**
     * Configures a RestTemplate bean with TMDB API settings.
     *
     * @return RestTemplate configured with interceptors to set 'accept' and 'Authorization' headers.
     */
    @Bean
    public RestTemplate tmdbRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();
            headers.set("accept", "application/json");
            headers.set("Authorization", "Bearer " + apiToken);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}