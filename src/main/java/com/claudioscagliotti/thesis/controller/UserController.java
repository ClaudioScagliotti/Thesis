package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.UserStatsDto;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.AuthenticationService;
import com.claudioscagliotti.thesis.service.UserStatsService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest controller for managing user authentication.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationService authService;
    private final UserStatsService userStatsService;

    /**
     * Constructs a UserController instance with the provided AuthenticationService dependency.
     *
     * @param authService      The AuthenticationService dependency.
     * @param userStatsService The UserStatsService dependency.
     */
    public UserController(AuthenticationService authService, UserStatsService userStatsService) {
        this.authService = authService;
        this.userStatsService = userStatsService;
    }

    /**
     * Registers a new user.
     *
     * @param request The registration request containing user details.
     * @return A ResponseEntity containing the registration response.
     */
    @PostMapping("/register")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
        try {
            AuthenticationResponse register = authService.register(request);
            String message = "Registration succeeded for the username: " + request.getUsername();
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, register);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Authenticates a user and returns an authentication response.
     *
     * @param request The login request containing user credentials.
     * @return A ResponseEntity containing the authentication response.
     */
    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        try {
            AuthenticationResponse authenticate = authService.authenticate(request);
            String message = "Login succeeded for the username: " + request.getUsername();
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, authenticate);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Refreshes the user's authentication token.
     *
     * @param httpRequest  The HTTP request containing the refresh token.
     * @param httpResponse The HTTP response to be updated.
     * @return A ResponseEntity containing the new authentication response.
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> refreshToken(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            AuthenticationResponse refreshToken = authService.refreshToken(httpRequest, httpResponse);
            String message = "Refresh succeeded";
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, refreshToken);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnauthorizedUserException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @return A ResponseEntity indicating the logout status.
     */
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<Void>> logoutUser() {
        try {
            String message = "User logged out successfully";
            GenericResponse<Void> response = new GenericResponse<>("success", message, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping
    @RequestMapping("all/stats")
    public ResponseEntity<GenericResponse<List<UserStatsDto>>> getUserStats(){
        try {
            List<UserStatsDto> allUserStats = userStatsService.getAllUserStats(RoleEnum.USER);
            String message = "Retrieved all " + allUserStats.size() + " users";
            GenericResponse<List<UserStatsDto>> response = new GenericResponse<>("success", message, allUserStats);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<List<UserStatsDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

