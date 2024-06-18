package com.claudioscagliotti.thesis.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizRequest {

    private Long quizId;
    private Integer correctOption;
    private Boolean correctAnswer;
    private String correctOrder;
}
