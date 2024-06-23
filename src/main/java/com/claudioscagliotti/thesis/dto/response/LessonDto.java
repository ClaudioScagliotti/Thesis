package com.claudioscagliotti.thesis.dto.response;

import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class LessonDto {
    private Long id;
    private String title;
    private String content;
    private String type;
    private Integer totalCards;
    private List<QuizDto> quizzes;

}
