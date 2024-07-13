package com.claudioscagliotti.thesis.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * OpenAIConfig configures a RestTemplate bean with an interceptor for OpenAI API requests.
 *
 * This configuration class reads the OpenAI API key from application properties and sets it
 * as a bearer token in the Authorization header of HTTP requests made through the RestTemplate.
 * It ensures that every request made using the 'openaiRestTemplate' bean includes the API key
 * for authentication with the OpenAI API.
 */
@Configuration
public class OpenAIConfig {

    @Value("${openai.api.key}")
    String openaiApiKey;

    /**
     * Configures and provides a RestTemplate bean for OpenAI API requests.
     *
     * @return RestTemplate configured with an interceptor to add the OpenAI API key as a bearer token.
     */
    @Bean
    @Qualifier("openaiRestTemplate")
    public RestTemplate openaiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // Add an interceptor to modify outgoing requests
        restTemplate.getInterceptors().add((request, body, execution) -> {
            // Add Authorization header with Bearer token containing OpenAI API key
            request.getHeaders().add("Authorization", "Bearer " + openaiApiKey);
            return execution.execute(request, body); // Execute the request
        });

        return restTemplate;
    }
}

