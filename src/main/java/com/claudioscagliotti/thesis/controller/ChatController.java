package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.ChatResponse;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.proxy.openai.OpenAiApiClient;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final OpenAiApiClient openAiApiClient;

    public ChatController(OpenAiApiClient openAiApiClient) {
        this.openAiApiClient = openAiApiClient;
    }

    @PostMapping("/{role}")
    public ResponseEntity<GenericResponse<ChatResponse>> chatWithRole(@PathVariable("role") String role, @RequestBody @Valid PromptRequest request) {

        ChatRequest chatRequest = new ChatRequest(request.getPrompt(), openAiApiClient.getProfile(role));

        try {
            ChatResponse responseContent = openAiApiClient.chat(chatRequest);
            String message = "Chat response generated successfully as " + role;
            GenericResponse<ChatResponse> response = new GenericResponse<>("success", message, responseContent);
            return ResponseEntity.ok(response);

        } catch (UnknownRoleException e) {
            GenericResponse<ChatResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(400).body(response);

        } catch (InvalidApiKeyException e) {
            GenericResponse<ChatResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(500).body(response);

        } catch (Exception e) {
            String errorMessage = "Failed to generate chat response as " + role;
            GenericResponse<ChatResponse> response = new GenericResponse<>("error", errorMessage, null);
            return ResponseEntity.status(500).body(response);
        }
    }

}
