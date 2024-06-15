package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.model.QuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface QuizMapper {
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    List<QuizEntity> toQuizEntity(List<QuizDto> quizDtoList);
    List<QuizDto> toQuizDto(List<QuizEntity> quizEntityList);

}
