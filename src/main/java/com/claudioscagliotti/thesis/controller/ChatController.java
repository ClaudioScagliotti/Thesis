package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.openai.Choice;
import com.claudioscagliotti.thesis.dto.openai.Message;
import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.exception.UnlockedRoleException;
import com.claudioscagliotti.thesis.proxy.openai.OpenAiApiClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for managing chat-related operations.
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiApiClient openAiApiClient;


    /**
     * Constructs a ChatController instance with the provided dependencies.
     *
     * @param openAiApiClient The OpenAiApiClient dependency.
     */
    public ChatController(OpenAiApiClient openAiApiClient) {
        this.openAiApiClient = openAiApiClient;
    }

    /**
     * Handles chat requests with a specified role.
     *
     * @param role    The role to use for the chat.
     * @param request The chat prompt request.
     * @return A ResponseEntity containing the chat response message.
     */
    @PostMapping("/{role}")
    public ResponseEntity<GenericResponse<Message>> chatWithRole(@PathVariable("role") String role,
                                                                 @RequestBody PromptRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            openAiApiClient.checkRolePermission(userDetails.getUsername(), role);
            ChatRequest chatRequest = openAiApiClient.retrieveConversationHistory(role, request, authentication.getName());
            ChatResponse response = openAiApiClient.chat(chatRequest);

            Message lastMessage = openAiApiClient.getMessageLastMessageAndUpdateConversationHistory(authentication, response);

            String message = "Chat response generated successfully as " + role;
            GenericResponse<Message> responseBody = new GenericResponse<>("success", message, lastMessage);
            return ResponseEntity.ok(responseBody);

        } catch (UnknownRoleException | UnlockedRoleException e) {
            GenericResponse<Message> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (InvalidApiKeyException e) {
            GenericResponse<Message> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

        } catch (Exception e) {
            String errorMessage = "Failed to generate chat response as " + role;
            GenericResponse<Message> response = new GenericResponse<>("error", errorMessage, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the full conversation history for the authenticated user.
     *
     * @return A ResponseEntity containing the list of conversation history choices.
     */
    @GetMapping("/history")
    public ResponseEntity<GenericResponse<List<Choice>>> getFullConversationHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            List<Choice> conversationHistory = openAiApiClient.getConversation(authentication.getName());

            if (conversationHistory.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GenericResponse<>("error", "No conversation history found for user: " + authentication.getName(), null));
            }

            return ResponseEntity.ok(new GenericResponse<>("success", "Full conversation history retrieved successfully", conversationHistory));
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve conversation history for user: " + authentication.getName();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new GenericResponse<>("error", errorMessage, null));
        }
    }
    /**
     * Retrieves all the roleplay profiles that the user is currently allowed to chat with.
     *
     * @return A ResponseEntity containing the list of roleplay profiles.
     */
    @GetMapping("/available-roles")
    public ResponseEntity<GenericResponse<List<String>>> getAvailableRoles() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<String> availableRoles = openAiApiClient.getAllUnlockedProfiles(userDetails.getUsername());

        if (availableRoles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new GenericResponse<>("error", "No available roles found for user: " + userDetails.getUsername(), null));
        }

        return ResponseEntity.ok(new GenericResponse<>("success", "Available roles retrieved successfully", availableRoles));
    }
}