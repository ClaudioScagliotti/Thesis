package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "goal")
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "time_to_dedicate", nullable = false)
    private Integer timeToDedicate;

    @Column(name = "goal_type", nullable = false)
    private String goalType;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "points", nullable = false)
    private Integer points;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_genre",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genreList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_theme",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "theme_id")
    )
    private List<Theme> themeList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_country_of_production",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "country_of_production_id")
    )
    private List<CountryOfProduction> countryOfProductionList;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "goal_course",
            joinColumns = @JoinColumn(name = "goal_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courseList;
}
