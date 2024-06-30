package com.claudioscagliotti.thesis.proxy.openai;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.ChatResponse;
import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.exception.ExternalAPIException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
@Service
public class OpenAiApiClient {
    private final RestTemplate restTemplate;
    private final String model;
    private final int maxTokens;
    private final String apiUrl;

    public OpenAiApiClient(
            @Qualifier("openaiRestTemplate") RestTemplate restTemplate,
            @Value("${openai.model}") String model,
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.maxTokens}") int maxTokens) {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
        this.maxTokens = maxTokens;
    }

    public ChatResponse chat(ChatRequest chatRequest) {
        setChatRequestDetails(chatRequest);
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                return null;
            }

            return response;

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new InvalidApiKeyException("Invalid API key provided", e);

        } catch (HttpClientErrorException.BadRequest e) {
            throw new ExternalAPIException("Bad request to external service", e);

        } catch (HttpServerErrorException e) {
            throw new ExternalAPIException("External service encountered a server error", e);

        } catch (RestClientException e) {
            throw new ExternalAPIException("Failed to communicate with external service", e);
        }

    }

    private void setChatRequestDetails(ChatRequest chatRequest) {
        chatRequest.setModel(model);
        chatRequest.setN(1);
        chatRequest.setTemperature(0.7);
        chatRequest.setMaxTokens(maxTokens);
    }



    public String getProfile(String role) {
        try {

            return RoleplayProfileEnum.fromRole(role).getProfileDescription();

        } catch (IllegalArgumentException e) {

            throw new UnknownRoleException("Unknown role: " + role);
        }
    }


}
