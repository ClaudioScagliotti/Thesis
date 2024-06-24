package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.mapper.BadgeMapper;
import com.claudioscagliotti.thesis.model.BadgeEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.BadgeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;
    private final UserService userService;

    public BadgeService(BadgeRepository badgeRepository, BadgeMapper badgeMapper, UserService userService) {
        this.badgeRepository = badgeRepository;
        this.badgeMapper = badgeMapper;
        this.userService = userService;
    }

    public List<BadgeDto> getAllBadgeByUsername(String username){
        UserEntity userEntity = userService.findByUsername(username);
        List<BadgeEntity> badgeEntityList = badgeRepository.findByUserEntityListContaining(userEntity);
        return badgeMapper.toBadgeDto(badgeEntityList);
    }
    public BadgeDto addBadgeForUser(String username, Long badgeId) {
        UserEntity userEntity = userService.findByUsername(username);
        BadgeEntity badgeEntity = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new EntityNotFoundException("Badge not found with id: " + badgeId));

        if (!userEntity.getBadgeEntityList().contains(badgeEntity)) {
            userEntity.getBadgeEntityList().add(badgeEntity);
            userService.saveUser(userEntity);
        } else {
            throw new EntityExistsException("User already has this badge.");
        }

        return badgeMapper.toBadgeDto(badgeEntity);
    }

    public List<BadgeDto> getAllBadge(){
        List<BadgeEntity> badgeEntityList = badgeRepository.findAll();
        return badgeMapper.toBadgeDto(badgeEntityList);
    }
}
