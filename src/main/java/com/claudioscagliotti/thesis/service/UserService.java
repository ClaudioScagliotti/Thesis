package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.PasswordChangeRequest;
import com.claudioscagliotti.thesis.dto.request.PasswordResetRequest;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService {

    /**
     * Saves a user entity.
     *
     * @param userEntity The user entity to save.
     */
    void saveUser(UserEntity userEntity);

    /**
     * Finds a user by username.
     *
     * @param username The username to search for.
     * @return The found UserEntity.
     * @throws UsernameNotFoundException If no user is found with the specified username.
     */
    UserEntity findByUsername(String username);

    /**
     * Finds a user by username and email.
     *
     * @param username The username to search for.
     * @param email The email to search for.
     * @return The found UserEntity.
     * @throws UsernameNotFoundException If no user is found with the specified username and email.
     */
    UserEntity findByUsernameAndEmail(String username, String email);

    /**
     * Updates the user's goal.
     *
     * @param username   The username of the user.
     * @param goalEntity The new goal entity to be set.
     */
    void updateUserGoal(String username, GoalEntity goalEntity);

    /**
     * Updates the user's streak.
     *
     * @param userEntity   The UserEntity.
     * @param updateOrReset   The Boolean parameter that indicates if the streak must be increased or reset.
     */
    void updateUserStreak(UserEntity userEntity, Boolean updateOrReset);

    /**
     * Adds points to the user's total points.
     *
     * @param userEntity The user entity to update.
     * @param points     The number of points to add.
     */
    void addPoints(UserEntity userEntity, int points);

    /**
     * Retrieves all users with a specific role.
     *
     * @param roleEnum The role of the users to retrieve.
     * @return A list of UserEntity objects representing users with the specified role.
     */
    List<UserEntity> getAllUsersWithRole(RoleEnum roleEnum);

    /**
     * Initiates the password reset process for a user.
     *
     * @param request The PasswordResetRequest containing the username and email of the user.
     */
    void resetUserPassword(PasswordResetRequest request);

    /**
     * Changes the user's password.
     *
     * @param username The username of the user.
     * @param request The request containing the new password.
     */
    void changePassword(String username, PasswordChangeRequest request);

    /**
     * Check if the user has the ADMIN role.
     *
     * @throws UnauthorizedUserException If the user is not an admin.
     */
    void checkIsAdmin();
}

