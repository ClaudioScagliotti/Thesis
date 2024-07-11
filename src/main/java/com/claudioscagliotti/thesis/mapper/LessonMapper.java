package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.model.LessonEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonEntity toLessonEntity(LessonDto dto);

    List<LessonDto> toLessonDto(List<LessonEntity> lessonEntity);

    LessonDto toLessonDto(LessonEntity lessonEntity);

}
