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
@PrimaryKeyJoinColumn(name = "quiz_id")
@DiscriminatorValue("CHRONOLOGICAL_ORDER")
public class ChronologicalOrderQuizEntity extends QuizEntity {

    @Column(nullable = false, columnDefinition = "jsonb")
    private String correctOrder; // Memorizzato come JSONB
}
