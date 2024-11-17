package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.openai.Choice;

import java.util.List;

public interface ChatSessionService {
    /**
     * Retrieves the conversation history for a given username.
     *
     * @param username The username of the user whose conversation history is to be retrieved.
     * @return The list of Choice objects representing the conversation history.
     */
    List<Choice> getConversationHistory(String username);

    /**
     * Updates the conversation history for a given username.
     * If the username does not already have a history, initializes a new list.
     *
     * @param username The username of the user whose conversation history is to be updated.
     * @param choices  The list of Choice objects representing the new conversation choices.
     */
    void updateConversationHistory(String username, List<Choice> choices);
}
