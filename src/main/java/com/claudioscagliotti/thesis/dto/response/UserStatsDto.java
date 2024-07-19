package com.claudioscagliotti.thesis.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatsDto {

    private String username;
    private Integer level;
    private Map<Long, Long> genreCount;
    private Integer points;
    private Integer streak;
    private Set<BadgeDto> badgeDtos;
    private List<CourseDto> courseList;
}
