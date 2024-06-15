package com.claudioscagliotti.thesis.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CHRONOLOGICAL_ORDER")
public class ChronologicalOrderQuiz extends QuizEntity {

    @Column(nullable = false, columnDefinition = "jsonb")
    private String correctOrder; // Memorizzato come JSONB
}
