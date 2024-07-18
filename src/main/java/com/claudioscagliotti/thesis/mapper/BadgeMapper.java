package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.model.BadgeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BadgeMapper {
    List<BadgeDto> toBadgeDto(List<BadgeEntity> badgeEntityList);

    @Mapping(source = "genreToUnlock.id", target = "genreToUnlock")
    BadgeDto toBadgeDto(BadgeEntity badgeEntity);
    @Mapping(source = "genreToUnlock", target = "genreToUnlock.id")
    BadgeEntity toBadgeEntity(BadgeDto badgeDto);


}
