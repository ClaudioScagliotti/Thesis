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
    private Long id;

    @Column(length = 800)
    private String overview;

    @Column(name = "original_language")
    private String originalLanguage;

    @Column(name = "original_title", length = 400)
    private String originalTitle;

    @Column(length = 400)
    private String title;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genreEntities;

    @Column(name = "poster_path", length = 500)
    private String posterPath;

    @Column(name = "backdrop_path", length = 500)
    private String backdropPath;

    // get the image from TMDB https://image.tmdb.org/t/p/, Poster Sizes: w92, w154, w185, w342, w500, w780, original.
    // example: https://image.tmdb.org/t/p/w300//ibMZ6Ahqjd9fQ8siWvnslwNoZ3y.jpg
    // actually is sufficient to send to frontend the poster path and the background path for every movie,
    // is it superfluous to use the dedicated endpoint to catch a lot of images in all languages and sizes.

    private LocalDate releaseDate;

    private Long tmdbId;
}
