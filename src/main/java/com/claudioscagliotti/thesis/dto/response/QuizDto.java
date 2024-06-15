package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizDto {
    private Long id;
    private String title;
    private String question;
}
