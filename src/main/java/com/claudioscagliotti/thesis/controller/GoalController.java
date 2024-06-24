package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.GoalDto;
import com.claudioscagliotti.thesis.service.GoalService;
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

@RestController
@RequestMapping("/goal")
@Validated
public class GoalController {
    private final GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<GoalDto>> createGoal(@Valid @RequestBody GoalDto createGoalRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            GoalDto createdGoal = goalService.createGoal(createGoalRequest, userDetails.getUsername());

            String message = "Created goal with id: " + createdGoal.getId();
            GenericResponse<GoalDto> response = new GenericResponse<>("success", message, createdGoal);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<GenericResponse<GoalDto>> getGoalByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            GoalDto goalDto = goalService.getGoalByUser(userDetails.getUsername());
            String message = "Found goal with id: " + goalDto.getId();
            GenericResponse<GoalDto> response = new GenericResponse<>("success", message, goalDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<GoalDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
