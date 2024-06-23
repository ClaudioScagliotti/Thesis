package com.claudioscagliotti.thesis.exception;

public class UnsubscribedUserException extends RuntimeException{
    public UnsubscribedUserException() {
    }

    public UnsubscribedUserException(String message) {
        super(message);
    }
}
