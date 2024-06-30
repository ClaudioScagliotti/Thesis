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
    private final String apiUrl;

    public OpenAiApiClient(
            @Qualifier("openaiRestTemplate") RestTemplate restTemplate,
            @Value("${openai.model}") String model,
            @Value("${openai.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
    }

    public String chat(ChatRequest chatRequest) {
        setChatRequestDetails(chatRequest);
        try {
            ChatResponse response = restTemplate.postForObject(apiUrl, chatRequest, ChatResponse.class);
            if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
                return "No response";
            }

            return response.getChoices().get(0).getMessage().getContent();

        } catch (HttpClientErrorException.Unauthorized e) {
            throw new InvalidApiKeyException("Invalid API key provided", e);

        } catch (HttpClientErrorException.BadRequest e) {
            throw new ExternalAPIException("Bad request to external service", e);

        } catch (HttpServerErrorException e) {
            // Gestione specifica per errori 5xx (Server Errors)
            throw new ExternalAPIException("External service encountered a server error", e);

        } catch (RestClientException e) {
            // Gestione generale per altre eccezioni di RestClient
            throw new ExternalAPIException("Failed to communicate with external service", e);
        }

    }

    private void setChatRequestDetails(ChatRequest chatRequest) {
        chatRequest.setModel(model);
        chatRequest.setN(1);
        chatRequest.setTemperature(0.7);
    }



    public String getProfile(String role) {
        try {

            return RoleplayProfileEnum.fromRole(role).getProfileDescription();

        } catch (IllegalArgumentException e) {

            throw new UnknownRoleException("Unknown role: " + role);
        }
    }


}
