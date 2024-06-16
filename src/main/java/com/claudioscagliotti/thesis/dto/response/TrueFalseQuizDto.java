package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrueFalseQuizDto extends QuizDto{
    private Boolean correctAnswer;
}
