package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.model.CourseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    List<CourseEntity> toCourseEntity(List<CourseDto> courseDtoList);
    List<CourseDto> toCourseDto(List<CourseEntity> courseEntityList);
    CourseDto toCourseDto(CourseEntity courseEntity);


}
