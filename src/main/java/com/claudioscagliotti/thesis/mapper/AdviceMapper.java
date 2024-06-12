package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdviceMapper {
    AdviceMapper INSTANCE = Mappers.getMapper(AdviceMapper.class);

    AdviceEntity toAdviceEntity(AdviceDto dto);
    @Mapping(source = "movie", target = "movieDto")
    AdviceDto toAdviceDto(AdviceEntity entity);

    List<AdviceDto> toAdviceDtoList(List<AdviceEntity> entityList);
}
