package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.enumeration.RoleplayProfileEnum;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleplayProfileService {

    private final BadgeService badgeService;

    private static final Map<RoleplayProfileEnum, Integer> profileUnlockThresholds = new HashMap<>();
    private static final Set<RoleplayProfileEnum> specificBadgeUnlockProfiles = new HashSet<>();

    static {
        profileUnlockThresholds.put(RoleplayProfileEnum.SPIELBERG, 1);
        profileUnlockThresholds.put(RoleplayProfileEnum.CHAPLIN, 2);
        profileUnlockThresholds.put(RoleplayProfileEnum.MELIES, 3);
        profileUnlockThresholds.put(RoleplayProfileEnum.EASTWOOD, 4);
        profileUnlockThresholds.put(RoleplayProfileEnum.TRUFFAUT, 5);
        profileUnlockThresholds.put(RoleplayProfileEnum.SORRENTINO, 6);
        profileUnlockThresholds.put(RoleplayProfileEnum.SCORSESE, 7);
        profileUnlockThresholds.put(RoleplayProfileEnum.COPPOLA, 8);
        profileUnlockThresholds.put(RoleplayProfileEnum.TARANTINO, 9);
        profileUnlockThresholds.put(RoleplayProfileEnum.KUBRICK, 10);


        specificBadgeUnlockProfiles.add(RoleplayProfileEnum.FINCHER);
        specificBadgeUnlockProfiles.add(RoleplayProfileEnum.FELLINI);
        specificBadgeUnlockProfiles.add(RoleplayProfileEnum.FORD);
        specificBadgeUnlockProfiles.add(RoleplayProfileEnum.KUROSAWA);
        specificBadgeUnlockProfiles.add(RoleplayProfileEnum.HITCHCOCK);
    }


    public RoleplayProfileService(BadgeService badgeService) {
        this.badgeService = badgeService;
    }

    /**
     * Checks if the user has unlocked the specified roleplay profile based on their badges.
     *
     * @param userEntity the user entity
     * @param roleplayProfileEnum the roleplay profile to check
     * @return true if the roleplay profile is unlocked, false otherwise
     */
    public boolean checkUnlockedRole(UserEntity userEntity, RoleplayProfileEnum roleplayProfileEnum) {
        Integer badgeCount = badgeService.getBadgeCountByUserId(userEntity.getId());
        List<BadgeDto> allBadgeByUsername = badgeService.getAllBadgeByUsername(userEntity.getUsername());

        if (profileUnlockThresholds.containsKey(roleplayProfileEnum)) {
            // Check the number of badges required for the profile
            int requiredBadgeCount = profileUnlockThresholds.get(roleplayProfileEnum);
            return badgeCount >= requiredBadgeCount;
        } else if (specificBadgeUnlockProfiles.contains(roleplayProfileEnum)) {

            return getSpecificBadgeId(roleplayProfileEnum,allBadgeByUsername);
        }
        return false;
    }

    /**
     * Retrieves a list of all unlocked roleplay profiles for the user.
     *
     * @param userEntity the user entity
     * @return a list of unlocked roleplay profiles
     */
    public List<RoleplayProfileEnum> getAllUnlockedProfiles(UserEntity userEntity) {
        List<RoleplayProfileEnum> unlockedProfiles = new ArrayList<>();
        for (RoleplayProfileEnum profile : profileUnlockThresholds.keySet()) {
            if (checkUnlockedRole(userEntity, profile)) {
                unlockedProfiles.add(profile);
            }
        }
        for (RoleplayProfileEnum profile : specificBadgeUnlockProfiles) {
            if (checkUnlockedRole(userEntity, profile)) {
                unlockedProfiles.add(profile);
            }
        }
        return unlockedProfiles;
    }

    /**
     * Placeholder method for retrieving specific badge IDs associated with roleplay profiles.
     *
     * @param roleplayProfileEnum the roleplay profile
     * @return true if the role is unlocked and false il it is locked
     */
    private boolean getSpecificBadgeId(RoleplayProfileEnum roleplayProfileEnum, List<BadgeDto> allBadgeByUser) {
        switch (roleplayProfileEnum) {
            case FINCHER -> {
                return allBadgeByUser.stream()
                        .anyMatch(badgeDto -> "crime beginner".equals(badgeDto.getName()));
            }
            case FELLINI -> {
                return allBadgeByUser.stream()
                        .anyMatch(badgeDto -> "fantasy beginner".equals(badgeDto.getName()));
            }
            case FORD -> {
                return allBadgeByUser.stream()
                        .anyMatch(badgeDto -> "western beginner".equals(badgeDto.getName()));
            }
            case KUROSAWA -> {
                return allBadgeByUser.stream()
                        .anyMatch(badgeDto -> "drama beginner".equals(badgeDto.getName()));
            }
            case HITCHCOCK -> {
                return allBadgeByUser.stream()
                        .anyMatch(badgeDto -> "thriller beginner".equals(badgeDto.getName()));
            }
            default -> {
                return false;
            }
        }
    }

}

