package com.claudioscagliotti.thesis.dto.request.openai;

import com.claudioscagliotti.thesis.dto.openai.Message;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatRequest {
    @JsonProperty("max_tokens")
    private int maxTokens;
    private String model;
    private int n;
    private double temperature;
    private List<Message> messages;

    public ChatRequest(List<Message> messages) {
        this.messages = messages;
    }

    public static ChatRequest createChatRequest(String model, int maxTokens, int n, double temperature, List<Message> messages) {
        ChatRequest chatRequest = new ChatRequest(messages);
        chatRequest.setModel(model);
        chatRequest.setMaxTokens(maxTokens);
        chatRequest.setN(n);
        chatRequest.setTemperature(temperature);
        return chatRequest;
    }
}
