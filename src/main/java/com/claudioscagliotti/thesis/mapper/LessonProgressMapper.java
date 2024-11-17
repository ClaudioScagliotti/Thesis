package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessonProgressMapper {

    LessonProgressDto tolessonProgressDto(LessonProgressEntity lessonProgressEntity, Long lessonId, String username);

}
