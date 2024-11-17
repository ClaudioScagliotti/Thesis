package com.claudioscagliotti.thesis.service;

public interface EmailService {
    /**
     * Sends a simple email message.
     *
     * @param to      The recipient's email address.
     * @param subject The subject of the email.
     * @param text    The body text of the email.
     */
    void sendSimpleMessage(String to, String subject, String text);
}
