package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class AdviceDto {
    private Long id;
    private MovieDto movieDto;
    private String status;
    private LocalDateTime creationDate;
    private LocalDateTime deadline;
    private Integer points;
    private String quizResult;
}
