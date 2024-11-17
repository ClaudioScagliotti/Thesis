package com.claudioscagliotti.thesis.model;

import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
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
@Table(name = "app_user_lesson_progress")
public class LessonProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private LessonEntity lessonEntity;

    @ManyToOne
    @JoinColumn(name = "app_user_id", nullable = false)
    private UserEntity userEntity;

    private Float progress;

    private Integer completedCards;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_result", length = 50)
    private QuizResultEnum quizResult; //necessary but not sufficient condition for lessonProgress to have the status SUCCEEDED

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private LessonStatusEnum status;

    public LessonProgressEntity(LessonEntity lessonEntity, UserEntity userEntity, Float progress, Integer completedCards, QuizResultEnum quizResult, LessonStatusEnum status) {
        this.lessonEntity = lessonEntity;
        this.userEntity = userEntity;
        this.progress = progress;
        this.completedCards = completedCards;
        this.quizResult = quizResult;
        this.status = status;
    }

}
