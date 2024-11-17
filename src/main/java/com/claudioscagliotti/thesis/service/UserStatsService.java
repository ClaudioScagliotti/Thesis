package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.UserStatsDto;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;

import java.util.List;
import java.util.Map;

public interface UserStatsService {
    /**
     * Adds statistics for the given user based on the genres of the movies they have watched.
     *
     * @param user   the user entity for which the statistics are being recorded
     * @param genres the list of genres associated with the movies watched by the user
     */
    void addUserStats(UserEntity user, List<GenreEntity> genres);

    /**
     * Retrieves a map with genre IDs as keys and their occurrence counts as values for a given user.
     *
     * @param userId the ID of the user
     * @return a map where the key is the genre ID and the value is the count of occurrences
     */
    Map<Long, Long> getGenreCountByUserId(Long userId);

    /**
     * Retrieves all user statistics for users with a specific role.
     *
     * @param roleEnum the role of the users to retrieve statistics for
     * @return a list of UserStatsDto containing statistics for each user with the specified role
     */
    List<UserStatsDto> getAllUserStats(RoleEnum roleEnum);

    /**
     * Retrieves the statistics for a user identified by their username.
     *
     * @param username the username of the user whose statistics are to be retrieved
     * @return a `UserStatsDto` object containing the user's statistics, including genre counts and level
     */
    UserStatsDto getUserStats(String username);

    /**
     * Converts a `UserEntity` object into a `UserStatsDto` object, including additional statistics.
     *
     * @param user the `UserEntity` object for which the statistics are to be retrieved
     * @return a `UserStatsDto` object containing the user's statistics, including genre counts and level
     */
    UserStatsDto getUserStatsDto(UserEntity user);

    /**
     * Calculates the user level based on the number of points.
     *
     * @param points the number of points a user has
     * @return the calculated level
     * @throws IllegalArgumentException if points are null or negative
     */
    Integer calculateLevel(Integer points);
}
