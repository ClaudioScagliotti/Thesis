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
public class GoalEntity { // TODO errore di progettazione: goal type deve essere una tabella. in modo che course si riferisca a goal type e non a goal.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "page")
    private Integer page;
    @PrePersist
    protected void onCreate() {
        this.page = 1;
    }
    @Column(name = "time_to_dedicate", nullable = false)
    private Float timeToDedicate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    private GoalTypeEntity goalType;

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
