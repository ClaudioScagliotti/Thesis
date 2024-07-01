package com.claudioscagliotti.thesis.proxy.openai;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.Choice;
import com.claudioscagliotti.thesis.dto.request.openai.Message;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.exception.ExternalAPIException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OpenAiApiClient {
    private static final int N_RECENT_MESSAGES = 5; // Numero di messaggi da inviare a gpt
    private final RestTemplate restTemplate;
    private final String model;
    private final int maxTokens;
    private final String apiUrl;
    private final ChatSessionService chatSessionService;

    public OpenAiApiClient(
            @Qualifier("openaiRestTemplate") RestTemplate restTemplate,
            @Value("${openai.model}") String model,
            @Value("${openai.api.url}") String apiUrl,
            @Value("${openai.maxTokens}") int maxTokens,
            ChatSessionService chatSessionService) {
        this.restTemplate = restTemplate;
        this.model = model;
        this.apiUrl = apiUrl;
        this.maxTokens = maxTokens;
        this.chatSessionService = chatSessionService;
    }

    public Message getLastMessage(ChatResponse response) {
        if (response == null || response.getChoices() == null || response.getChoices().isEmpty()) {
            return null;
        }

        Choice lastChoice = response.getChoices().stream()
                .max(Comparator.comparingInt(Choice::getIndex))
                .orElse(null);

        return lastChoice.getMessage();
    }

    private List<Message> getlastMessages(List<Message> messages) {
        List<Message> relevantMessages = new ArrayList<>();

        if (!messages.isEmpty() && "system".equals(messages.get(0).getRole())) {
            relevantMessages.add(messages.get(0));
        }

        int startIndex = messages.size() - N_RECENT_MESSAGES;
        if (startIndex < 0) {
            startIndex = 0;
        }

        for (int i = startIndex; i < messages.size(); i++) {
            relevantMessages.add(messages.get(i));
        }

        return relevantMessages;
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

        List<Message> truncatedMessages = getlastMessages(chatRequest.getMessages());
        chatRequest.setMessages(truncatedMessages);
    }



    public String getProfile(String role) {
        try {

            return RoleplayProfileEnum.fromRole(role).getProfileDescription();

        } catch (IllegalArgumentException e) {

            throw new UnknownRoleException("Unknown role: " + role);
        }
    }

    public ChatRequest manageConversationHistory(String role, PromptRequest request, String username) {
        List<Message> conversationHistory = getConversation(username);

        ChatRequest chatRequest = new ChatRequest();

        if (conversationHistory.isEmpty()) {
            // Se la cronologia è vuota, setto il contesto di sistema e il prompt dell'utente
            List<Message> initialMessages = new ArrayList<>();
            initialMessages.add(new Message("system", getProfile(role)));
            initialMessages.add(new Message("user", request.getPrompt()));
            chatRequest.setMessages(initialMessages);
        } else {
            // Se ci sono messaggi salvati, aggiungo il nuovo messaggio dell'utente in coda alla cronologia esistente
            conversationHistory.add(new Message("user", request.getPrompt()));
            chatRequest.setMessages(conversationHistory);
        }
        return chatRequest;
    }
    public void updateConversationHistory(String username, ChatResponse response){
        chatSessionService.updateConversationHistory(username, response);
    }
    public List<Message> getConversation(String username) {
        return chatSessionService.getConversationHistory(username);
    }

}
