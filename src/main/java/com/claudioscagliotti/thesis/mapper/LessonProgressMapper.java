package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LessonProgressMapper {
    LessonProgressMapper INSTANCE = Mappers.getMapper(LessonProgressMapper.class);

    //@Mapping(source = "quizzes", target = "quizzes")
    LessonProgressDto tolessonProgressDto(LessonProgressEntity lessonProgressEntity, Long lessonId, String username);

}
