package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.openai.ChatRequest;
import com.claudioscagliotti.thesis.dto.request.openai.PromptRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.InvalidApiKeyException;
import com.claudioscagliotti.thesis.exception.UnknownRoleException;
import com.claudioscagliotti.thesis.proxy.openai.OpenAiApiClient;
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
    public ResponseEntity<GenericResponse<String>> chatWithRole(@PathVariable("role") String role, @RequestBody PromptRequest request) {

        ChatRequest chatRequest = new ChatRequest(request.getPrompt(), openAiApiClient.getProfile(role));

        try {
            String responseContent = openAiApiClient.chat(chatRequest);
            String message = "Chat response generated successfully as " + role;
            GenericResponse<String> response = new GenericResponse<>("success", message, responseContent);
            return ResponseEntity.ok(response);

        } catch (UnknownRoleException | InvalidApiKeyException e) {
            GenericResponse<String> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(500).body(response);

        } catch (Exception e) {
            String errorMessage = "Failed to generate chat response as " + role;
            GenericResponse<String> response = new GenericResponse<>("error", errorMessage, null);
            return ResponseEntity.status(500).body(response);
        }
    }

}
