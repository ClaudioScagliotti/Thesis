package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.model.UserEntity;

import java.util.List;

public interface RoleplayProfileService {

    /**
     * Checks if the user has unlocked the specified roleplay profile based on their badges.
     *
     * @param userEntity the user entity
     * @param roleplayProfileEnum the roleplay profile to check
     * @return true if the roleplay profile is unlocked, false otherwise
     */
    boolean checkUnlockedRole(UserEntity userEntity, RoleplayProfileEnum roleplayProfileEnum);

    /**
     * Retrieves a list of all unlocked roleplay profiles for the user.
     *
     * @param userEntity the user entity
     * @return a list of unlocked roleplay profiles
     */
    List<RoleplayProfileEnum> getAllUnlockedProfiles(UserEntity userEntity);
}
