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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    /**
     * Controller method to retrieve all user statistics.
     * This method handles GET requests to the stats/all endpoint.
     *
     * @return a ResponseEntity containing a GenericResponse with a list of user's statistics.
     * The response will include a success message and all the user's statistics data if the operation
     * is successful. In case of an error, it will return an error message and an HTTP 500 status.
     */
    @GetMapping
    @RequestMapping("stats/all")
    public ResponseEntity<GenericResponse<List<UserStatsDto>>> getUsersStats(){
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
    /**
     * Controller method to retrieve user statistics.
     * This method handles GET requests to the /stats endpoint.
     *
     * @return a ResponseEntity containing a GenericResponse with the user's statistics.
     * The response will include a success message and the user's statistics data if the operation
     * is successful. In case of an error, it will return an error message and an HTTP 500 status.
     */
    @GetMapping
    @RequestMapping("stats")
    public ResponseEntity<GenericResponse<UserStatsDto>> getUserStats(){
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            UserStatsDto userStats = userStatsService.getUserStats(userDetails.getUsername());
            String message = "Retrieved stats for user: " + userDetails.getUsername();
            GenericResponse<UserStatsDto> response = new GenericResponse<>("success", message, userStats);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<UserStatsDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

