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
@Table(name = "theme")
public class KeywordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 100)
    private String name;
    @Column(name = "tmdb_id")
    private Integer tmdbId;


    @ManyToMany(fetch = FetchType.LAZY)
    private List<GoalEntity> goalEntityList;

    public KeywordEntity(String name, Integer tmdbId) {
        this.name = name;
        this.tmdbId = tmdbId;
    }
}
