package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.openai.Choice;
import com.claudioscagliotti.thesis.dto.openai.Message;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.proxy.openai.OpenAiApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiApiClient openAiApiClient;

    public ChatController(OpenAiApiClient openAiApiClient) {
        this.openAiApiClient = openAiApiClient;
    }

    @PostMapping("/{role}")
    public ResponseEntity<GenericResponse<Message>> chatWithRole(@PathVariable("role") String role,
                                                                 @RequestBody PromptRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ChatRequest chatRequest = openAiApiClient.retrieveConversationHistory(role, request, authentication.getName());

        try {
            ChatResponse response = openAiApiClient.chat(chatRequest);

            Message lastMessage = openAiApiClient.getMessageLastMessageAndUpdateConversationHistory(authentication, response);

            String message = "Chat response generated successfully as " + role;
            GenericResponse<Message> responseBody = new GenericResponse<>("success", message, lastMessage);
            return ResponseEntity.ok(responseBody);

        } catch (UnknownRoleException e) {
            GenericResponse<Message> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(400).body(response);

        } catch (InvalidApiKeyException e) {
            GenericResponse<Message> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(500).body(response);

        } catch (Exception e) {
            String errorMessage = "Failed to generate chat response as " + role;
            GenericResponse<Message> response = new GenericResponse<>("error", errorMessage, null);
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<GenericResponse<List<Choice>>> getFullConversationHistory() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            List<Choice> conversationHistory = openAiApiClient.getConversation(authentication.getName());

            if (conversationHistory.isEmpty()) {
                return ResponseEntity.status(404).body(new GenericResponse<>("error", "No conversation history found for user: " + authentication.getName(), null));
            }

            return ResponseEntity.ok(new GenericResponse<>("success", "Full conversation history retrieved successfully", conversationHistory));
        } catch (Exception e) {
            String errorMessage = "Failed to retrieve conversation history for user: " + authentication.getName();
            return ResponseEntity.status(500).body(new GenericResponse<>("error", errorMessage, null));
        }
    }
}