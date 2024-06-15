package com.claudioscagliotti.thesis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("TRUE_FALSE")
public class TrueFalseQuiz extends QuizEntity {

    @Column(nullable = false)
    private Boolean correctAnswer;

}
