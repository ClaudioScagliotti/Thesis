package com.claudioscagliotti.thesis.exception;

public class InvalidApiKeyException extends RuntimeException{
    public InvalidApiKeyException(String message) {
        super(message);
    }

    public InvalidApiKeyException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidApiKeyException() {
        super("Error occurred while authenticating with External API");
    }
}
