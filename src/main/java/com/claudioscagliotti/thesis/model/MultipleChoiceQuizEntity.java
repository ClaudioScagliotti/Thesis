package com.claudioscagliotti.thesis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@PrimaryKeyJoinColumn(name = "quiz_id")
@DiscriminatorValue("MULTIPLE_CHOICE")
public class MultipleChoiceQuizEntity extends QuizEntity {

    @Column(nullable = false, columnDefinition = "jsonb")
    private String options; // Memorizzato come JSONB

    @Column(nullable = false)
    private Integer correctOption;
}
