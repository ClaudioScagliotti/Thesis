package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.model.QuizEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    List<QuizEntity> toQuizEntity(List<QuizDto> quizDtoList);

    List<QuizDto> toQuizDto(List<QuizEntity> quizEntityList);

    QuizDto toQuizDto(QuizEntity quizEntity);


}
