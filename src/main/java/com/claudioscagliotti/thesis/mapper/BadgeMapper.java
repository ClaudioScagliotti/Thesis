package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.model.BadgeEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BadgeMapper {
    List<BadgeDto> toBadgeDto(List<BadgeEntity> badgeEntityList);

    BadgeDto toBadgeDto(BadgeEntity badgeEntity);


}
