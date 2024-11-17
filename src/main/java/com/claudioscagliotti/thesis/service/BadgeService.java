package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.model.UserEntity;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface BadgeService {

    /**
     * Retrieves all badges associated with a specific user.
     *
     * @param username The username of the user to retrieve badges for.
     * @return A list of BadgeDto objects representing the badges of the user.
     */
    List<BadgeDto> getAllBadgeByUsername(String username);

    /**
     * Adds a badge to a user.
     *
     * @param username The username of the user to add the badge to.
     * @param badgeId  The ID of the badge to add.
     * @return The BadgeDto representing the added badge.
     * @throws EntityExistsException   If the user already has the badge.
     * @throws EntityNotFoundException If the badge with the given ID does not exist.
     */
    BadgeDto addBadgeForUser(String username, Long badgeId);

    /**
     * Retrieves all available badges.
     *
     * @return A list of BadgeDto objects representing all available badges.
     */
    List<BadgeDto> getAllBadge();

    /**
     * Retrieves all badges by genreId.
     *
     * @param genreId The ID of the genre for which to retrieve badges.
     * @return A list of BadgeDto objects representing all available badges for the specified genre.
     */
    List<BadgeDto> getAllBadgeByGenreId(Long genreId);

    /**
     * Checks if the user has accumulated enough movies in a specific genre to warrant a badge.
     *
     * @param user The user for whom to check and assign badges. Must not be null.
     */
    void checkBadgeForUser(UserEntity user);

    /**
     * Retrieves the total count of badges for the specified user.
     *
     * @param userId the ID of the user
     * @return the total count of badges for the user
     */
    Integer getBadgeCountByUserId(Long userId);
}
