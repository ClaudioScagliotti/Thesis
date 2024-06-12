package com.claudioscagliotti.thesis.enumeration.tmdb;

public enum MetohdEnum {

    NOW_PLAYING("/movie/now_playing"),
    MOST_POPULAR("/movie/popular"),
    TOP_RATED("/movie/top_rated"),
    DISCOVER("/discover/movie");
    private final String value;

    MetohdEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
