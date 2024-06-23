package com.claudioscagliotti.thesis.dto.response;

import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LessonProgressDto {

    private Long lessonId;
    private String username;
    private Long id;
    private Float progress;
    private Integer completedCards;

    private QuizResultEnum quizResult;

    private LessonStatusEnum status;
}
