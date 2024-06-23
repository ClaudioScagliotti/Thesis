package com.claudioscagliotti.thesis.exception;

public class NoAdviceAvailableException extends RuntimeException {
    public NoAdviceAvailableException() {
    }

    public NoAdviceAvailableException(String message) {
        super(message);
    }
}
