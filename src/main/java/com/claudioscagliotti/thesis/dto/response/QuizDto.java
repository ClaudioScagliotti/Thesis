package com.claudioscagliotti.thesis.dto.response;

import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.QuizTypeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class QuizDto {
    private Long id;
    private String title;
    private String question;
    private QuizTypeEnum type;
    private String options;
    private QuizResultEnum status;

}
