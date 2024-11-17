package com.claudioscagliotti.thesis.enumeration.tmdb;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum GenreEnum {
    ACTION(28L, "Action"),
    ADVENTURE(12L, "Adventure"),
    ANIMATION(16L, "Animation"),
    COMEDY(35L, "Comedy"),
    CRIME(80L, "Crime"),
    DOCUMENTARY(99L, "Documentary"),
    DRAMA(18L, "Drama"),
    FAMILY(10751L, "Family"),
    FANTASY(14L, "Fantasy"),
    HISTORY(36L, "History"),
    HORROR(27L, "Horror"),
    MUSIC(10402L, "Music"),
    MYSTERY(9648L, "Mystery"),
    ROMANCE(10749L, "Romance"),
    SCIENCE_FICTION(878L, "Science Fiction"),
    TV_MOVIE(10770L, "TV Movie"),
    THRILLER(53L, "Thriller"),
    WAR(10752L, "War"),
    WESTERN(37L, "Western");

    private final Long tmdb_id;
    private final String name;

    GenreEnum(Long tmdb_id, String name) {
        this.tmdb_id = tmdb_id;
        this.name = name;
    }

    public Long getTmdbId() {
        return tmdb_id;
    }

    public String getName() {
        return name;
    }

    public static GenreEnum getById(Long id) {
        for (GenreEnum genre : values()) {
            if (Objects.equals(genre.tmdb_id, id)) {
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
                .toList();
    }

    public static GenreEnum fromTmdbId(Long tmdbId) {
        for (GenreEnum genre : values()) {
            if (Objects.equals(genre.getTmdbId(), tmdbId)) {
                return genre;
            }
        }
        throw new IllegalArgumentException("Unknown TMDB ID: " + tmdbId);
    }
}

