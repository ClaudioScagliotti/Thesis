package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonEntity toLessonEntity(LessonDto dto);
    List<LessonDto> toLessonDto(List<LessonEntity> lessonEntity);
    //@Mapping(source = "quizzes", target = "quizzes")
    LessonDto toLessonDto(LessonEntity lessonEntity);

}
