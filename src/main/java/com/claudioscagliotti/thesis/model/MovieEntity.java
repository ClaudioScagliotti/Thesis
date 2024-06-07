package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "movie")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String overview;
    private String originalLanguage;
    private String originalTitle;
    private String title;
    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genreEntities;
    private String posterPath; //TODO GESTIONE IMMAGINI https://image.tmdb.org/t/p/, Poster Sizes: w92, w154, w185, w342, w500, w780, original.
    private LocalDate releaseDate;
    private Integer tmdbId;
}
