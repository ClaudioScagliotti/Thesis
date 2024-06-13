package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "genre")
public class GenreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToMany(mappedBy = "genreEntities")
    private List<MovieEntity> movies;

    public GenreEntity(String name) {
        this.name = name;
    }

    public GenreEntity(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
