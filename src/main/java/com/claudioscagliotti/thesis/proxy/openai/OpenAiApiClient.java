package com.claudioscagliotti.thesis.proxy.openai;

import com.claudioscagliotti.thesis.dto.openai.Choice;
import com.claudioscagliotti.thesis.dto.openai.Message;
import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.exception.ExternalApiException;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.exception.UnlockedRoleException;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.service.ChatSessionService;
import com.claudioscagliotti.thesis.service.RoleplayProfileService;
import com.claudioscagliotti.thesis.service.UserService;
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
/**
 * OpenAiApiClient is a service class responsible for interacting with the OpenAI API
 * to perform chat operations and manage conversation history.
 *
 * This class provides methods to create chat requests, send them to the OpenAI API,
 * retrieve conversation history, add responses to the conversation history, and manage
 * user profiles for role-playing scenarios.
 *
 * Example usage:
 * - Create a chat request with specified parameters and send it to the OpenAI API.
 * - Retrieve conversation history for a user and update it with new responses.
 * - Manage user profiles based on role definitions for role-playing scenarios.
 *
 * Dependencies:
 * - Requires a configured RestTemplate with appropriate interceptors for API communication.
 * - Uses a ChatSessionService to manage and retrieve conversation histories for users.
 */
@Service
public class OpenAiApiClient {

    @Value("${openai.request.context.messages}")
    private int N_RECENT_MESSAGES;
    private final RestTemplate restTemplate;
    private final String apiUrl;
    private final ChatSessionService chatSessionService;
    private final RoleplayProfileService roleplayProfileService;
    private final UserService userService;
    @Value("${openai.model}")
    private String model;

    @Value("${openai.maxTokens}")
    private int maxTokens;

    @Value("${openai.response.number}")
    private int n;

    @Value("${openai.response.temperature}")
    private double temperature;

    /**
     * Creates a chat request with the provided messages.
     *
     * @param messages List of messages to include in the chat request.
     * @return ChatRequest object initialized with specified parameters and messages.
     */
    public ChatRequest createChatRequestWithMessages(List<Message> messages) {
        return ChatRequest.createChatRequest(model, maxTokens, n, temperature, messages);
    }

    /**
     * Constructs OpenAiApiClient with RestTemplate, API URL, and ChatSessionService dependencies.
     *
     * @param restTemplate           Configured RestTemplate for making API calls to OpenAI.
     * @param apiUrl                 URL of the OpenAI API endpoint.
     * @param chatSessionService     Service for managing and retrieving user conversation histories.
     * @param roleplayProfileService Service for managing rules of roles
     * @param userService
     */
    public OpenAiApiClient(
            @Qualifier("openaiRestTemplate") RestTemplate restTemplate,
            @Value("${openai.api.url}") String apiUrl,
            ChatSessionService chatSessionService, RoleplayProfileService roleplayProfileService, UserService userService) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
        this.chatSessionService = chatSessionService;
        this.roleplayProfileService = roleplayProfileService;
        this.userService = userService;
    }

    /**
     * Retrieves the last N recent messages from the provided list.
     *
     * @param messages List of messages to filter for recent messages.
     * @return List of relevant recent messages based on configured N_RECENT_MESSAGES.
     */
    private List<Message> getLastMessages(List<Message> messages) {
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


    /**
     * Sends a chat request to the OpenAI API and retrieves the response.
     *
     * @param chatRequest ChatRequest object containing parameters and messages for the API request.
     * @return ChatResponse object containing the response received from the OpenAI API.
     * @throws InvalidApiKeyException If the API key used for authentication is invalid.
     * @throws ExternalApiException   If there is an error communicating with the OpenAI API.
     */
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
            throw new ExternalApiException("Bad request to external service", e);

        } catch (HttpServerErrorException e) {
            throw new ExternalApiException("External service encountered a server error", e);

        } catch (RestClientException e) {
            throw new ExternalApiException("Failed to communicate with external service", e);
        }

    }

    /**
     * Truncates the messages in the ChatRequest to retain only the recent messages.
     *
     * @param chatRequest ChatRequest object whose messages need to be truncated.
     */
    private void truncateChatRequestMessages(ChatRequest chatRequest) {
        List<Message> truncatedMessages = getLastMessages(chatRequest.getMessages());
        chatRequest.setMessages(truncatedMessages);
    }

    /**
     * Retrieves the profile description based on the provided role.
     *
     * @param role Role for which the profile description needs to be retrieved.
     * @return Profile description associated with the provided role.
     * @throws UnknownRoleException If the provided role does not match any known roles.
     */
    public RoleplayProfileEnum getProfile(String role) {
        try {
            return RoleplayProfileEnum.fromRole(role);
        } catch (IllegalArgumentException e) {
            throw new UnknownRoleException("Unknown role: " + role);
        }
    }

    /**
     * Retrieves the conversation history for the specified user and constructs a ChatRequest
     * based on the history to resume the conversation.
     *
     * @param role     Role associated with the user.
     * @param request  PromptRequest object containing the prompt for the conversation.
     * @param username Username of the user for whom the conversation history needs to be retrieved.
     * @return ChatRequest object initialized with the conversation history messages.
     */
public ChatRequest retrieveConversationHistory(String role, PromptRequest request, String username) {
    List<Choice> conversationHistory = getConversation(username);
    List<Choice> newChoices = new ArrayList<>();
    int index= Choice.calculateLastIndex(conversationHistory);

    if (conversationHistory.isEmpty()) {
        Message profileMessage = new Message("system", getProfile(role).getProfileDescription());
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
    /**
     * Adds the provided list of messages to the conversation history for the specified user.
     *
     * @param username Username of the user for whom the messages need to be added to the conversation history.
     * @param messages List of messages to be added to the conversation history.
     */
    public void addResponseToConversationHistory(String username, List<Message> messages) {
        List<Choice> conversationHistory = getConversation(username);
        List<Choice> newChoices= new ArrayList<>();

        for(Message message: messages){
            Choice newChoice = new Choice(message, conversationHistory);
            newChoices.add(newChoice);
        }
        chatSessionService.updateConversationHistory(username, newChoices);
    }
    /**
     * Retrieves the conversation history for the specified user.
     *
     * @param username Username of the user for whom the conversation history needs to be retrieved.
     * @return List of Choice objects representing the conversation history for the user.
     */
    public List<Choice> getConversation(String username) {
        return chatSessionService.getConversationHistory(username);
    }

    /**
     * Retrieves the last message from the ChatResponse and updates the conversation history
     * for the authenticated user with the received messages.
     *
     * @param authentication Authentication object representing the authenticated user.
     * @param response       ChatResponse object containing the response received from the OpenAI API.
     * @return Last Message object from the ChatResponse.
     */
    public Message getMessageLastMessageAndUpdateConversationHistory(Authentication authentication, ChatResponse response) {
        List<Message> messages = response.getChoices().stream()
                .map(Choice::getMessage)
                .toList();
        Message lastMessage = messages.get(messages.lastIndexOf(messages.get(messages.size() - 1)));
        addResponseToConversationHistory(authentication.getName(), messages);
        return lastMessage;
    }

    public void checkRolePermission(String username, String role){
        RoleplayProfileEnum roleplayProfileEnum = getProfile(role);
        UserEntity userEntity = userService.findByUsername(username);

        boolean checkUnlockedRole = roleplayProfileService.checkUnlockedRole(userEntity, roleplayProfileEnum);
        if(!checkUnlockedRole){
            throw new UnlockedRoleException("The role "+roleplayProfileEnum.name()+" for the user "+username+" is unlocked");
        }
    }
    public List<String> getAllUnlockedProfiles(String username){
        UserEntity userEntity = userService.findByUsername(username);
        List<RoleplayProfileEnum> allUnlockedProfiles = roleplayProfileService.getAllUnlockedProfiles(userEntity);
        return RoleplayProfileEnum.convertEnumListToStringList(allUnlockedProfiles);
    }
}
