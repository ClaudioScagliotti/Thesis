package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.mapper.BadgeMapper;
import com.claudioscagliotti.thesis.model.BadgeEntity;
import com.claudioscagliotti.thesis.model.GenreEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.BadgeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Service class for managing badges.
 */
@Service
public class BadgeService {

    @Value("${number.film.badge}")
    private Integer numberOfFilm;

    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final UserService userService;
    private final UserStatsService userStatsService;
    private final GenreService genreService;

    /**
     * Constructs a BadgeService with required dependencies.
     *
     * @param badgeRepository  The repository for badge operations.
     * @param badgeMapper      The mapper for converting between BadgeEntity and DTOs.
     * @param userService      The service for user-related operations.
     * @param userStatsService The service to manage userStatistics
     * @param genreService     The service to manage Genre
     */
    public BadgeService(BadgeRepository badgeRepository, BadgeMapper badgeMapper, UserService userService, UserStatsService userStatsService, GenreService genreService) {
        this.badgeRepository = badgeRepository;
        this.badgeMapper = badgeMapper;
        this.userService = userService;
        this.userStatsService = userStatsService;
        this.genreService = genreService;
    }

    /**
     * Retrieves all badges associated with a specific user.
     *
     * @param username The username of the user to retrieve badges for.
     * @return A list of BadgeDto objects representing the badges of the user.
     */
    public List<BadgeDto> getAllBadgeByUsername(String username) {
        UserEntity userEntity = userService.findByUsername(username);
        List<BadgeEntity> badgeEntityList = badgeRepository.findByUserEntityListContaining(userEntity);
        return badgeMapper.toBadgeDto(badgeEntityList);
    }

    /**
     * Adds a badge to a user.
     *
     * @param username The username of the user to add the badge to.
     * @param badgeId  The ID of the badge to add.
     * @return The BadgeDto representing the added badge.
     * @throws EntityExistsException   If the user already has the badge.
     * @throws EntityNotFoundException If the badge with the given ID does not exist.
     */
    public BadgeDto addBadgeForUser(String username, Long badgeId) {
        UserEntity userEntity = userService.findByUsername(username);
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + badgeId));

        if (!userEntity.getBadgeEntitySet().contains(badgeEntity)) {
            userEntity.getBadgeEntitySet().add(badgeEntity);
            userService.saveUser(userEntity);
        }

        return badgeMapper.toBadgeDto(badgeEntity);
    }

    /**
     * Retrieves all available badges.
     *
     * @return A list of BadgeDto objects representing all available badges.
     */
    public List<BadgeDto> getAllBadge() {
        List<BadgeEntity> badgeEntityList = badgeRepository.findAll();
        return badgeMapper.toBadgeDto(badgeEntityList);
    }

    /**
     * Retrieves all badges by genreId.
     *
     * @param genreId The ID of the genre for which to retrieve badges.
     * @return A list of BadgeDto objects representing all available badges for the specified genre.
     */
    public List<BadgeDto> getAllBadgeByGenreId(Long genreId) {
        GenreEntity genreEntity = genreService.mapGenreIdsToEntities(Collections.singletonList(genreId)).stream().findFirst()
                .orElseThrow(() -> new EntityNotFoundException("There is no GenreEntity with id: " + genreId));

        List<BadgeEntity> badgeEntityList = badgeRepository.findByGenreToUnlock(genreEntity);
        return badgeMapper.toBadgeDto(badgeEntityList);
    }

    /**
     * Checks if the user has accumulated enough movies in a specific genre to warrant a badge.
     * <p>
     * This method retrieves the genre counts for the specified user and compares these counts to a predefined threshold
     * (defined by the {@code numberOfFilm} variable). If the number of movies for a genre exceeds this threshold, the method checks
     * if there is a badge associated with that genre. If a badge is found, it is assigned to the user.
     *
     * @param user The user for whom to check and assign badges. Must not be {@code null}.
     * @throws NullPointerException If {@code user} is {@code null}.
     */
    public void checkBadgeForUser(UserEntity user) {
        Map<Long, Long> genreCountByUserId = userStatsService.getGenreCountByUserId(user.getId());

        for (Map.Entry<Long, Long> entry : genreCountByUserId.entrySet()) {
            Long genreId = entry.getKey();
            Long count = entry.getValue();


            List<BadgeDto> allBadgeByGenreId = getAllBadgeByGenreId(genreId);

            for (BadgeDto badgeDto : allBadgeByGenreId) {
                if (badgeDto.getGenreToUnlock() != null && badgeDto.getGenreToUnlock().equals(genreId)) {
                    Float badgeLevel = badgeDto.getLevel();

                    if (count > numberOfFilm * badgeLevel) {
                        addBadgeForUser(user.getUsername(), badgeDto.getId());
                    }
                }
            }
        }
    }


    /**
     * Retrieves the total count of badges for the specified user.
     *
     * @param userId the ID of the user
     * @return the total count of badges for the user
     */
    public Integer getBadgeCountByUserId(Long userId) {
        return badgeRepository.countByUserId(userId);
    }
}

