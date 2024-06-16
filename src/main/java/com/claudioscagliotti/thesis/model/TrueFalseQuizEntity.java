package com.claudioscagliotti.thesis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("TRUE_FALSE")
@PrimaryKeyJoinColumn(name = "quiz_id")
public class TrueFalseQuizEntity extends QuizEntity {

    @Column(nullable = false)
    private Boolean correctAnswer;

}
