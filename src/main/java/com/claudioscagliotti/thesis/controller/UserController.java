package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.configuration.CustomLogoutHandler;
import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.PasswordChangeRequest;
import com.claudioscagliotti.thesis.dto.request.PasswordResetRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.UserStatsDto;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.impl.AuthenticationServiceImpl;
import com.claudioscagliotti.thesis.service.impl.UserServiceImpl;
import com.claudioscagliotti.thesis.service.impl.UserStatsServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.claudioscagliotti.thesis.utility.ConstantsUtil.*;

/**
 * Rest controller for managing user authentication.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationServiceImpl authService;
    private final UserStatsServiceImpl userStatsService;
    private final UserServiceImpl userService;
    private final CustomLogoutHandler customLogoutHandler;

    /**
     * Constructs a UserController instance with the provided AuthenticationService dependency.
     *
     * @param authService         The AuthenticationService dependency.
     * @param userStatsService    The UserStatsService dependency.
     * @param userService         The UserService dependency.
     * @param customLogoutHandler The CustomLogoutHandler dependency.
     */
    public UserController(AuthenticationServiceImpl authService, UserStatsServiceImpl userStatsService, UserServiceImpl userService, CustomLogoutHandler customLogoutHandler) {
        this.authService = authService;
        this.userStatsService = userStatsService;
        this.userService = userService;
        this.customLogoutHandler = customLogoutHandler;
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
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(SUCCESS, message, register);
            return ResponseEntity.ok(response);
        } catch (BadRequestException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
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
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(SUCCESS, message, authenticate);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (BadCredentialsException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, "Invalid credentials", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Refreshes the user's authentication token.
     *
     * @param httpRequest  The HTTP request containing the refresh token.
     * @return A ResponseEntity containing the new authentication response.
     */
    @PostMapping("/refresh_token")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> refreshToken(HttpServletRequest httpRequest) {
        try {
            AuthenticationResponse refreshToken = authService.refreshToken(httpRequest);
            String message = "Refresh succeeded";
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(SUCCESS, message, refreshToken);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnauthorizedUserException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Logs out the currently authenticated user.
     *
     * @return A ResponseEntity indicating the logout status.
     */
    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<Void>> logoutUser(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {
        try {
            customLogoutHandler.logout(httpServletRequest, httpServletResponse, authentication);
            String message = "User logged out successfully";
            GenericResponse<Void> response = new GenericResponse<>(SUCCESS, message, null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
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
            GenericResponse<List<UserStatsDto>> response = new GenericResponse<>(SUCCESS, message, allUserStats);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<List<UserStatsDto>> response = new GenericResponse<>(ERROR, e.getMessage(), null);
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
            GenericResponse<UserStatsDto> response = new GenericResponse<>(SUCCESS, message, userStats);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            GenericResponse<UserStatsDto> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<GenericResponse<Void>> requestPasswordReset(@RequestBody PasswordResetRequest request) {
        try {
            userService.resetUserPassword(request);
            String message = "Password reset link has been sent to your email";
            GenericResponse<Void> response = new GenericResponse<>(SUCCESS, message, null);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<GenericResponse<Void>> changePassword(@RequestBody PasswordChangeRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            userService.changePassword(userDetails.getUsername(), request);
            String message = "Your password has been successfully changed";
            GenericResponse<Void> response = new GenericResponse<>(SUCCESS, message, null);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/new-password")
    public ResponseEntity<GenericResponse<Void>> saveNewPassword(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        try {
            authService.saveNewPassword(userService.findByUsername(request.getUsername()),request, httpRequest);
            String message = "Your new password has been successfully saved";
            GenericResponse<Void> response = new GenericResponse<>(SUCCESS, message, null);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnauthorizedUserException e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>(ERROR, UNEXPECTED_ERROR, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

