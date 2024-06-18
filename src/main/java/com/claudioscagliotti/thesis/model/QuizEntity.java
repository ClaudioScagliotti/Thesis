package com.claudioscagliotti.thesis.model;

import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.QuizTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
//@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "quiz")
public class QuizEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private QuizTypeEnum type;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private QuizResultEnum status;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private LessonEntity lesson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advice_id")
    private AdviceEntity advice;

    // Campi specifici per MULTIPLE_CHOICE
    @Column(columnDefinition = "jsonb")
    private String options;

    private Integer correctOption;

    // Campi specifici per TRUE_FALSE
    private Boolean correctAnswer;

    // Campi specifici per CHRONOLOGICAL_ORDER
    @Column(columnDefinition = "jsonb")
    private String correctOrder;
}
