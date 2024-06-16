package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuizDto extends QuizDto {

    private String options;

    private Integer correctOption;
}
