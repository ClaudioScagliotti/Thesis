package com.claudioscagliotti.thesis.mapper;

import com.claudioscagliotti.thesis.dto.response.UserStatsDto;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {MovieMapper.class})
public interface UserStatsMapper {

    /**
     * Converts a list of UserEntity objects to a list of UserStatsDto objects.
     *
     * @param entityList the list of UserEntity objects
     * @return the list of UserStatsDto objects
     */
    List<UserStatsDto> toUserStatsDtoList(List<UserEntity> entityList);

    /**
     * Converts a UserEntity object to a UserStatsDto object.
     *
     * @param entity the UserEntity object
     * @return the UserStatsDto object
     */
    @Mapping(source = "badgeEntitySet", target = "badgeDtos")
    @Mapping(source = "courseEntityList", target = "courseList")
    UserStatsDto toUserStatsDto(UserEntity entity);

    /**
     * Custom mapping method to convert GenreEntity to Long.
     *
     * @param genreEntity the GenreEntity object
     * @return the genre ID as Long
     */
    default Long map(GenreEntity genreEntity) {
        return genreEntity != null ? genreEntity.getId() : null;
    }
}

