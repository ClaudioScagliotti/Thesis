package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = MovieMapper.class)
public interface AdviceMapper {

    @Mapping(source = "movie", target = "movieDto")
    AdviceDto toAdviceDto(AdviceEntity entity);

    @Mapping(source = "movie", target = "movieDto")
    List<AdviceDto> toAdviceDto(List<AdviceEntity> entityList);
}
