package com.claudioscagliotti.thesis.exception;

public class NoMovieException extends RuntimeException {
    public NoMovieException() {
    }

    public NoMovieException(String message) {
        super(message);
    }
}
