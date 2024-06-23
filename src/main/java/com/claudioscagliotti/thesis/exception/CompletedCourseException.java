package com.claudioscagliotti.thesis.exception;

public class CompletedCourseException extends RuntimeException {
    public CompletedCourseException() {
    }

    public CompletedCourseException(String message) {
        super(message);
    }
}
