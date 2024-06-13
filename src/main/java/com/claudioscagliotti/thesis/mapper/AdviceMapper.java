package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", uses = MovieMapper.class)
public interface AdviceMapper {
    AdviceMapper INSTANCE = Mappers.getMapper(AdviceMapper.class);

    AdviceEntity toAdviceEntity(AdviceDto dto);
    @Mapping(source = "movie", target = "movieDto")
    AdviceDto toAdviceDto(AdviceEntity entity);

    @Mapping(source = "movie", target = "movieDto")
    List<AdviceDto> toAdviceDtoList(List<AdviceEntity> entityList);
}
