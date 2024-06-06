package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.KeywordDto;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    KeywordMapper INSTANCE = Mappers.getMapper(KeywordMapper.class);

    KeywordEntity toKeywordEntity(KeywordDto dto);
    KeywordDto toKeywordDto(KeywordEntity KeywordEntity);
}
