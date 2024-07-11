package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.KeywordDto;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeywordMapper {
    KeywordEntity toKeywordEntity(KeywordDto dto);

    KeywordDto toKeywordDto(KeywordEntity keywordEntity);
}
