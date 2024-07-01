package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.openai.Choice;
import com.claudioscagliotti.thesis.dto.request.openai.Message;
import com.claudioscagliotti.thesis.dto.response.openai.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ChatSessionService {

    private final Map<String, List<Message>> sessionStorage = new ConcurrentHashMap<>();

    public List<Message> getConversationHistory(String username) {
        return sessionStorage.getOrDefault(username, new ArrayList<>());
    }


    public void updateConversationHistory(String sessionId, ChatResponse response) {
        sessionStorage.computeIfAbsent(sessionId, k -> new ArrayList<>()).addAll(response.getChoices().stream()
                .map(Choice::getMessage)
                .toList());
    }
}

