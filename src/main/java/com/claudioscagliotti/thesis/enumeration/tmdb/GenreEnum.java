package com.claudioscagliotti.thesis.enumeration.tmdb;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum GenreEnum {
    ACTION(28, "Action"),
    ADVENTURE(12, "Adventure"),
    ANIMATION(16, "Animation"),
    COMEDY(35, "Comedy"),
    CRIME(80, "Crime"),
    DOCUMENTARY(99, "Documentary"),
    DRAMA(18, "Drama"),
    FAMILY(10751, "Family"),
    FANTASY(14, "Fantasy"),
    HISTORY(36, "History"),
    HORROR(27, "Horror"),
    MUSIC(10402, "Music"),
    MYSTERY(9648, "Mystery"),
    ROMANCE(10749, "Romance"),
    SCIENCE_FICTION(878, "Science Fiction"),
    TV_MOVIE(10770, "TV Movie"),
    THRILLER(53, "Thriller"),
    WAR(10752, "War"),
    WESTERN(37, "Western");

    private final int tmdb_id;
    private final String name;

    GenreEnum(int tmdb_id, String name) {
        this.tmdb_id = tmdb_id;
        this.name = name;
    }

    public int getTmdbId() {
        return tmdb_id;
    }

    public String getName() {
        return name;
    }

    public static GenreEnum getById(int id) {
        for (GenreEnum genre : values()) {
            if (genre.tmdb_id == id) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No genre found with id: " + id);
    }

    public static GenreEnum getByName(String name) {
        for (GenreEnum genre : values()) {
            if (genre.name.equalsIgnoreCase(name)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("No genre found with name: " + name);
    }

    public static List<String> getAllNames() {
        return Arrays.stream(values())
                .map(GenreEnum::getName)
                .collect(Collectors.toList());
    }

    public static GenreEnum fromTmdbId(int tmdbId) {
        for (GenreEnum genre : values()) {
            if (genre.getTmdbId() == tmdbId) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown TMDB ID: " + tmdbId);
    }
}

