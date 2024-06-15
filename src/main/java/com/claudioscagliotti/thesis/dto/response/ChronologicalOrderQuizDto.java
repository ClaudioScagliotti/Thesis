package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChronologicalOrderQuizDto extends QuizDto{
    private String correctOrder;
}
