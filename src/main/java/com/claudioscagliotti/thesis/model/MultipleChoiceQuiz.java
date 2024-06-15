package com.claudioscagliotti.thesis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuiz extends QuizEntity {

    @Column(nullable = false, columnDefinition = "jsonb")
    private String options; // Memorizzato come JSONB

    @Column(nullable = false)
    private Integer correctOption;
}
