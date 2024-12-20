package com.claudioscagliotti.thesis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Set;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "lesson")
public class LessonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity courseEntity;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content")
    private String content; // TODO how the content must be splitted in cards?

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "total_cards", nullable = false)
    private Integer totalCards;

    @Column(name = "lesson_order")
    private Long progressiveId;

    @OneToMany(mappedBy = "lesson", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizEntity> quizzes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "lesson_image",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    private Set<ImageEntity> imageEntities;
}
