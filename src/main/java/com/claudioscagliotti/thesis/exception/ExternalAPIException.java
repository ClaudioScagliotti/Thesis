package com.claudioscagliotti.thesis.exception;

public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException() {
    }

    public ExternalAPIException(String message) {
        super(message);
    }
}
