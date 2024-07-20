package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.UserStatsDto;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.mapper.UserStatsMapper;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.model.UserStatsEntity;
import com.claudioscagliotti.thesis.repository.UserStatsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Service class for managing user statistics.
 */
@Service
public class UserStatsService {
    @Value("${points.for.level}")
    private Integer pointsForLevel;

    private final UserStatsRepository userStatsRepository;
    private final UserService userService;
    private final UserStatsMapper userStatsMapper;

    /**
     * Constructor for UserStatsService.
     *
     * @param userStatsRepository the repository to manage user statistics
     * @param userService the service to retrieve the users
     * @param userStatsMapper the mapper for the UserStatsDto
     */
    public UserStatsService(UserStatsRepository userStatsRepository, UserService userService, UserStatsMapper userStatsMapper) {
        this.userStatsRepository = userStatsRepository;
        this.userService = userService;
        this.userStatsMapper = userStatsMapper;
    }

    /**
     * Adds statistics for the given user based on the genres of the movies they have watched.
     *
     * @param user  the user entity for which the statistics are being recorded
     * @param genres the list of genres associated with the movies watched by the user
     */
    @Transactional
    public void addUserStats(UserEntity user, List<GenreEntity> genres) {
        for (GenreEntity genre : genres) {
            UserStatsEntity userStats = new UserStatsEntity();
            userStats.setUser(user);
            userStats.setGenre(genre);
            userStatsRepository.save(userStats);
        }
    }

    /**
     * Retrieves a map with genre IDs as keys and their occurrence counts as values for a given user.
     *
     * @param userId the ID of the user
     * @return a map where the key is the genre ID and the value is the count of occurrences
     */
    public Map<Long, Long> getGenreCountByUserId(Long userId) {
        List<Object[]> results = userStatsRepository.findGenreCountByUserId(userId);
        Map<Long, Long> genreCountMap = new HashMap<>();

        for (Object[] result : results) {
            Long genreId = (Long) result[0];
            Long count = (Long) result[1];
            genreCountMap.put(genreId, count);
        }

        return genreCountMap;
    }
    /**
     * Retrieves all user statistics for users with a specific role.
     *
     * This method fetches all users with the specified role, maps them to UserStatsDto objects,
     * calculates the genre count and level for each user, and returns a list of these DTOs.
     *
     * @param roleEnum the role of the users to retrieve statistics for
     * @return a list of UserStatsDto containing statistics for each user with the specified role
     */
    public List<UserStatsDto> getAllUserStats(RoleEnum roleEnum){
        List<UserEntity> users = userService.getAllUsersWithRole(roleEnum);
        List<UserStatsDto> dtoList= new ArrayList<>();
        for(UserEntity user: users){
            UserStatsDto statsDto = getUserStatsDto(user);
            dtoList.add(statsDto);
        }
        return dtoList;
    }

    /**
     * Retrieves the statistics for a user identified by their username.
     *
     * This method finds the user by their username using the `userService` and then converts
     * the user entity into a `UserStatsDto` object by calling the `getUserStatsDto` method.
     *
     * @param username the username of the user whose statistics are to be retrieved.
     * @return a `UserStatsDto` object containing the user's statistics including genre counts and level.
     */
    public UserStatsDto getUserStats(String username) {
        return getUserStatsDto(userService.findByUsername(username));
    }

    /**
     * Converts a `UserEntity` object into a `UserStatsDto` object, including additional statistics.
     *
     * This method maps the provided `UserEntity` to a `UserStatsDto` using the `userStatsMapper`.
     * It also sets the genre count and level for the user by calling `getGenreCountByUserId` and
     * `calculateLevel` methods, respectively.
     *
     * @param user the `UserEntity` object for which the statistics are to be retrieved.
     * @return a `UserStatsDto` object containing the user's statistics, including genre counts and level.
     * @throws IllegalArgumentException if the user's points are invalid for level calculation.
     */
    public UserStatsDto getUserStatsDto(UserEntity user) {
        UserStatsDto statsDto = userStatsMapper.toUserStatsDto(user);
        statsDto.setGenreCount(getGenreCountByUserId(user.getId()));
        statsDto.setLevel(calculateLevel(user.getPoints()));
        return statsDto;
    }

    /**
     * Calculates the user level based on the number of points.
     *
     * This method converts the number of points into a level where each level requires a specified
     * number of points. For example, if each level requires 1000 points, then 2890 points would
     * correspond to level 2.
     *
     * @param points the number of points a user has
     * @return the calculated level
     * @throws IllegalArgumentException if points are null or negative
     */
    public Integer calculateLevel(Integer points) {
        if (points == null || points < 0) {
            throw new IllegalArgumentException("Points must be a non-negative integer");
        }
        return points / pointsForLevel;
    }
}

