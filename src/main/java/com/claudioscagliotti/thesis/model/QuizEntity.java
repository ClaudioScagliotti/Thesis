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
    private String options;

    private Integer correctOption;
    private Boolean correctAnswer;
    private String correctOrder;
}
