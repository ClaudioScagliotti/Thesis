package com.claudioscagliotti.thesis.dto.request.openai;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRequest {

    private String model;
    private List<Message> messages;
    private int n;
    private double temperature;
    private int maxTokens;

    public ChatRequest(String prompt, String role) {
        this.messages = new ArrayList<>();
        // aggiunta contesto
        this.messages.add(new Message("system", role));
        // aggiunta prompt utente
        this.messages.add(new Message("user", prompt));
    }

}
