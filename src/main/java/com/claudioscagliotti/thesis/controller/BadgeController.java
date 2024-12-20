package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.BadgeDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.impl.BadgeServiceImpl;
import com.claudioscagliotti.thesis.service.impl.UserServiceImpl;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.claudioscagliotti.thesis.utility.ConstantsUtil.*;


/**
 * Rest controller for managing badge-related operations.
 */
@RestController
@RequestMapping("/badge")
public class BadgeController {

    private final BadgeServiceImpl badgeService;
    private final UserServiceImpl userService;

    /**
     * Constructs a BadgeController instance with the provided dependencies.
     *
     * @param badgeService The BadgeService dependency.
     * @param userService  The UserService dependency.
     */
    public BadgeController(BadgeServiceImpl badgeService, UserServiceImpl userService) {
        this.badgeService = badgeService;
        this.userService = userService;
    }

    /**
     * Retrieves all badges for the authenticated user.
     *
     * @return A ResponseEntity containing the list of badges for the authenticated user.
     */
    @GetMapping("/user")
    public ResponseEntity<GenericResponse<List<BadgeDto>>> getAllBadgesForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<BadgeDto> badgeDtoList = badgeService.getAllBadgeByUsername(userDetails.getUsername());
            String message = "Retrieved " + badgeDtoList.size() + " badges for user: " + userDetails.getUsername();
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>(SUCCESS, message, badgeDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Adds a badge to the authenticated user based on the provided badge ID.
     *
     * @param badgeId The ID of the badge to add.
     * @return A ResponseEntity containing the added badge.
     */
    @PostMapping("/add/{badgeId}")
    public ResponseEntity<GenericResponse<BadgeDto>> addBadgeToUser(@PathVariable("badgeId") Long badgeId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            userService.checkIsAdmin();
            BadgeDto badgeDto = badgeService.addBadgeForUser(userDetails.getUsername(), badgeId);
            String message = "Badge with id: " + badgeId + " added to user: " + userDetails.getUsername();
            GenericResponse<BadgeDto> response = new GenericResponse<>(SUCCESS, message, badgeDto);
            return ResponseEntity.ok(response);

        } catch (UnauthorizedUserException e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (EntityNotFoundException e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (EntityExistsException e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<BadgeDto> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves all available badges.
     *
     * @return A ResponseEntity containing the list of all available badges.
     */
    @GetMapping("/all")
    public ResponseEntity<GenericResponse<List<BadgeDto>>> getAllBadges() {
        try {
            List<BadgeDto> badgeDtoList = badgeService.getAllBadge();
            String message = "Retrieved all " + badgeDtoList.size() + " badges";
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>(SUCCESS, message, badgeDtoList);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<List<BadgeDto>> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

