package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.service.BadgeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/badge")
public class BadgeController {
    private final BadgeService badgeService;
    public BadgeController(BadgeService badgeService) {
        this.badgeService = badgeService;
    }
    @GetMapping("/user")
    public ResponseEntity<GenericResponse<List<BadgeDto>>> getAllBadgesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<BadgeDto> badgeDtoList = badgeService.getAllBadgeByUsername(userDetails.getUsername());
            String message = "Retrieved " + badgeDtoList.size() + " badges for user: " + userDetails.getUsername();
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>("success", message, badgeDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/add/{badgeId}")
    public ResponseEntity<GenericResponse<BadgeDto>> addBadgeToUser(@PathVariable("badgeId") Long badgeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            BadgeDto badgeDto = badgeService.addBadgeForUser(userDetails.getUsername(), badgeId);
            String message = "Badge with id: " + badgeId + " added to user: " + userDetails.getUsername();
            GenericResponse<BadgeDto> response = new GenericResponse<>("success", message, badgeDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (EntityExistsException e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<GenericResponse<List<BadgeDto>>> getAllBadges() {
        try {
            List<BadgeDto> badgeDtoList = badgeService.getAllBadge();
            String message = "Retrieved all " + badgeDtoList.size() + " badges";
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>("success", message, badgeDtoList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

