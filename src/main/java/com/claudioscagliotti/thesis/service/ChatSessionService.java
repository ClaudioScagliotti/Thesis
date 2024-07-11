package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.openai.Choice;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Service class for managing chat session histories.
 */
@Service
public class ChatSessionService {

    // In-memory storage for session histories, using ConcurrentHashMap for thread-safety
    private final Map<String, List<Choice>> sessionStorage = new ConcurrentHashMap<>();

    /**
     * Retrieves the conversation history for a given username.
     *
     * @param username The username of the user whose conversation history is to be retrieved.
     * @return The list of Choice objects representing the conversation history.
     */
    public List<Choice> getConversationHistory(String username) {
        return sessionStorage.getOrDefault(username, new ArrayList<>());
    }

    /**
     * Updates the conversation history for a given username.
     * If the username does not already have a history, initializes a new list.
     *
     * @param username The username of the user whose conversation history is to be updated.
     * @param choices  The list of Choice objects representing the new conversation choices.
     */
    public void updateConversationHistory(String username, List<Choice> choices) {
        sessionStorage.computeIfAbsent(username, k -> new ArrayList<>()).addAll(choices);
    }
}

