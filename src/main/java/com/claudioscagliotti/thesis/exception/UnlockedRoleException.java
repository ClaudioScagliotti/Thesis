package com.claudioscagliotti.thesis.exception;

public class UnlockedRoleException extends RuntimeException{
    public UnlockedRoleException() {
    }

    public UnlockedRoleException(String message) {
        super(message);
    }
}
