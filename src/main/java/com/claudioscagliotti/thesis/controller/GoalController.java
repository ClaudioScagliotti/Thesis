package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.request.GoalDto;
import com.claudioscagliotti.thesis.service.impl.GoalServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest controller for managing user goals.
 */
@RestController
@RequestMapping("/goal")
@Validated
public class GoalController {

    private final GoalServiceImpl goalService;

    /**
     * Constructs a GoalController instance with the provided dependencies.
     *
     * @param goalService The GoalService dependency.
     */
    public GoalController(GoalServiceImpl goalService) {
        this.goalService = goalService;
    }

    /**
     * Creates a new goal for the authenticated user.
     *
     * @param createGoalRequest The GoalDto containing goal details.
     * @return A ResponseEntity containing the created goal details.
     */
    @PostMapping
    public ResponseEntity<GenericResponse<GoalDto>> createGoal(@Valid @RequestBody GoalDto createGoalRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            GoalDto createdGoal = goalService.createGoal(createGoalRequest, userDetails.getUsername());
            String message = "Created goal for the user: " + userDetails.getUsername();
            GenericResponse<GoalDto> response = new GenericResponse<>("success", message, createdGoal);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the goal of the authenticated user.
     *
     * @return A ResponseEntity containing the user's goal details.
     */
    @GetMapping
    public ResponseEntity<GenericResponse<GoalDto>> getGoalByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            GoalDto goalDto = goalService.getGoalByUser(userDetails.getUsername());
            String message = "Found goal for the user: " + userDetails.getUsername();
            GenericResponse<GoalDto> response = new GenericResponse<>("success", message, goalDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
