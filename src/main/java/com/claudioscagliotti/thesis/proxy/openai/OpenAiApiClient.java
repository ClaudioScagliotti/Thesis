package com.claudioscagliotti.thesis.proxy.openai;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.openai.Choice;
import com.claudioscagliotti.thesis.dto.openai.Message;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.exception.ExternalAPIException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.service.ChatSessionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class OpenAiApiClient {

    @Value("${openai.request.context.messages}")
    private int N_RECENT_MESSAGES;
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final ChatSessionService chatSessionService;
    @Value("${openai.model}")
    private String model;

    @Value("${openai.maxTokens}")
    private int maxTokens;

    @Value("${openai.response.number}")
    private int n;

    @Value("${openai.response.temperature}")
    private double temperature;

    public ChatRequest createChatRequestWithMessages(List<Message> messages) {
        return ChatRequest.createChatRequest(model, maxTokens, n, temperature, messages);
    }

    public OpenAiApiClient(
            @Qualifier("openaiRestTemplate") RestTemplate restTemplate,
            @Value("${openai.api.url}") String apiUrl,
            ChatSessionService chatSessionService) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.chatSessionService = chatSessionService;
    }


    private List<Message> getlastMessages(List<Message> messages) {
        List<Message> relevantMessages = new ArrayList<>();

        if (!messages.isEmpty() && "system".equals(messages.get(0).getRole())) {
            relevantMessages.add(messages.get(0));
        }

        int startIndex = messages.size() - N_RECENT_MESSAGES;
        if (startIndex < 0) {
            startIndex = 1;
        }

        for (int i = startIndex; i < messages.size(); i++) {
            relevantMessages.add(messages.get(i));
        }

        return relevantMessages;
    }



    public ChatResponse chat(ChatRequest chatRequest) {
        truncateChatRequestMessages(chatRequest);

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

    private void truncateChatRequestMessages(ChatRequest chatRequest) {
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

public ChatRequest retrieveConversationHistory(String role, PromptRequest request, String username) {
    List<Choice> conversationHistory = getConversation(username);
    List<Choice> newChoices = new ArrayList<>();
    int index= Choice.calculateLastIndex(conversationHistory);

    if (conversationHistory.isEmpty()) {
        Message profileMessage = new Message("system", getProfile(role));
        Choice profileChoice= new Choice(++index, profileMessage);
        newChoices.add(profileChoice);
    }

    Message userMessage = new Message("user", request.getPrompt());
    Choice userChoice= new Choice(++index, userMessage);

    newChoices.add(userChoice);
    chatSessionService.updateConversationHistory(username, newChoices);

    List<Message> messages = new ArrayList<>(getConversation(username).stream().map(Choice::getMessage).toList());

    return createChatRequestWithMessages(messages);
}
    public void addResponseToConversationHistory(String username, List<Message> messages) {
        List<Choice> conversationHistory = getConversation(username);
        List<Choice> newChoices= new ArrayList<>();

        for(Message message: messages){
            Choice newChoice = new Choice(message, conversationHistory);
            newChoices.add(newChoice);
        }
        chatSessionService.updateConversationHistory(username, newChoices);
    }

    public List<Choice> getConversation(String username) {
        return chatSessionService.getConversationHistory(username);
    }

    public Message getMessageLastMessageAndUpdateConversationHistory(Authentication authentication, ChatResponse response) {
        List<Message> messages = response.getChoices().stream()
                .map(Choice::getMessage)
                .toList();
        Message lastMessage = messages.get(messages.lastIndexOf(messages.get(messages.size() - 1)));
        addResponseToConversationHistory(authentication.getName(), messages);
        return lastMessage;
    }
}
