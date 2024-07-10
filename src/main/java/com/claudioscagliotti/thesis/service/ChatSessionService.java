package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.openai.Choice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {

    private final Map<String, List<Choice>> sessionStorage = new ConcurrentHashMap<>();

    public List<Choice> getConversationHistory(String username) {
        return sessionStorage.getOrDefault(username, new ArrayList<>());
    }


    public void updateConversationHistory(String username, List<Choice> choices) {
        sessionStorage.computeIfAbsent(username, k -> new ArrayList<>()).addAll(choices);
    }
}

