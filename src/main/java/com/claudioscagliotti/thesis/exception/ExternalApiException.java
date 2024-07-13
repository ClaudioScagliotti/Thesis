package com.claudioscagliotti.thesis.exception;

public class ExternalApiException extends RuntimeException {
    public ExternalApiException() {
    }
    public ExternalApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExternalApiException(String message) {
        super(message);
    }
}
