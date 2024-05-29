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
@Table(name = "goal")
public class GoalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_to_dedicate", nullable = false)
    private Integer timeToDedicate;

    @Column(name = "goal_type", nullable = false)
    private String goalType;

    @Column(name = "min_year", nullable = false)
    private Integer minYear;
    @Column(name = "max_year", nullable = false)
    private Integer maxYear;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_genre",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genreEntityList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_keyword",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "keyword_id")
    )
    private List<KeywordEntity> keywordEntityList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_country_of_production",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "country_of_production_id")
    )
    private List<CountryOfProductionEntity> countryOfProductionEntityList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_course",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<CourseEntity> courseEntityList;
}
