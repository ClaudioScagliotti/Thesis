package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.ChronologicalOrderQuizDto;
import com.claudioscagliotti.thesis.dto.response.MultipleChoiceQuizDto;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.dto.response.TrueFalseQuizDto;
import com.claudioscagliotti.thesis.model.ChronologicalOrderQuizEntity;
import com.claudioscagliotti.thesis.model.MultipleChoiceQuizEntity;
import com.claudioscagliotti.thesis.model.QuizEntity;
import com.claudioscagliotti.thesis.model.TrueFalseQuizEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
@Mapper(componentModel = "spring")
public interface QuizMapper {
    QuizMapper INSTANCE = Mappers.getMapper(QuizMapper.class);

    List<QuizEntity> toQuizEntity(List<QuizDto> quizDtoList);
    List<QuizDto> toQuizDto(List<QuizEntity> quizEntityList);
    QuizDto toQuizDto(QuizEntity quizEntity);
    MultipleChoiceQuizDto toMultipleChoiceQuizDto(MultipleChoiceQuizEntity quiz);

    TrueFalseQuizDto toTrueFalseQuizDto(TrueFalseQuizEntity quiz);

    ChronologicalOrderQuizDto toChronologicalOrderQuizDto(ChronologicalOrderQuizEntity quiz);


}
