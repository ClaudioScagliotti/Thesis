package com.claudioscagliotti.thesis.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
public class UserStatsDto {

    private String username;
    private Integer level;
    Map<Long, Long> genreCount;
    private int points;
    private int streak;
    private Set<BadgeDto> badgeDtos;
    private List<CourseDto> courseList;
}
