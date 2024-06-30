package com.claudioscagliotti.thesis.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@Configuration
public class TmdbConfig {

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.token}")
    private String apiToken;

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

    public String getApiKey() {
        return apiKey;
    }

    public String getApiToken() {
        return apiToken;
    }
}
