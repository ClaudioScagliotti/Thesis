package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.NoAdviceAvailableException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.AdviceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for managing advice-related operations.
 */
@RestController
@RequestMapping("/advice")
public class AdviceController {

    private final AdviceService adviceService;

    /**
     * Constructs an AdviceController instance with the provided dependencies.
     *
     * @param adviceService The AdviceService dependency.
     */
    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    /**
     * Creates a list of advices for the authenticated user.
     *
     * @return A ResponseEntity containing the created list of advices.
     */
    @PostMapping
    public ResponseEntity<GenericResponse<List<AdviceDto>>> createAdviceList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<AdviceDto> adviceList = adviceService.createAdviceList(userDetails.getUsername());
            String message = "Created " + adviceList.size() + " advices";

            GenericResponse<List<AdviceDto>> response = new GenericResponse<>("success", message, adviceList);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<List<AdviceDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException e) {
            GenericResponse<List<AdviceDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<List<AdviceDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Skips the next advice for the authenticated user based on the provided advice ID.
     *
     * @param adviceId The ID of the advice to skip.
     * @return A ResponseEntity containing the skipped advice.
     */
    @PostMapping("skip/{adviceId}")
    public ResponseEntity<GenericResponse<AdviceDto>> skipNextAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto = adviceService.skipAdvice(userDetails.getUsername(), adviceId);
            String message = "Skipped advice with id: " + adviceId;
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Marks the specified advice as completed for the authenticated user.
     *
     * @param adviceId The ID of the advice to mark as completed.
     * @return A ResponseEntity containing the completed advice.
     */
    @PostMapping("complete/{adviceId}")
    public ResponseEntity<GenericResponse<AdviceDto>> completeAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto = adviceService.completeAdvice(userDetails.getUsername(), adviceId);
            String message = "Completed advice with id: " + adviceId;
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedUserException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

    }

    /**
     * Retrieves the next available advice for the authenticated user.
     *
     * @return A ResponseEntity containing the next available advice.
     */
    @GetMapping
    public ResponseEntity<GenericResponse<AdviceDto>> getNextAdvice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto = adviceService.getNextAdvice(userDetails.getUsername());
            String message = "Found advice with id: " + adviceDto.getId();
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (NoAdviceAvailableException | EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}