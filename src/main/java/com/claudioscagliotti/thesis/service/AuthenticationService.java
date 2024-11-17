package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.model.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthenticationService{

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return An AuthenticationResponse indicating the success or failure of registration.
     * @throws BadRequestException If the username already exists.
     */
    AuthenticationResponse register(RegisterRequest request) throws BadRequestException;

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request The login request containing username and password.
     * @return An AuthenticationResponse containing access and refresh tokens upon successful authentication.
     * @throws UsernameNotFoundException If no user with the given username exists.
     */
    AuthenticationResponse authenticate(LoginRequest request);

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param request The HTTP request containing the refresh token in the Authorization header.
     * @return An AuthenticationResponse containing the new access and refresh tokens.
     * @throws UnauthorizedUserException If the refresh token is invalid.
     */
    AuthenticationResponse refreshToken(HttpServletRequest request);

    /**
     * Checks if the authenticated user has a specific role.
     *
     * @param role The role to check (e.g., "ROLE_USER" or "ROLE_ADMIN").
     * @return true if the user has the specified role, otherwise false.
     */
    boolean hasRole(String role);

    /**
     * Generates a reset password token for the given user and saves it.
     *
     * @param user The UserEntity for which the reset password token is generated.
     * @return The generated reset password token as a string.
     */
    String resetPassword(UserEntity user);

    /**
     * Saves the new password for the user if the reset token is valid.
     *
     * @param user The UserEntity for which the password is being reset.
     * @param request The LoginRequest containing the new password.
     * @param httpRequest The HttpServletRequest containing the reset token in the Authorization header.
     * @throws UnauthorizedUserException If the reset token is invalid or missing.
     */
    void saveNewPassword(UserEntity user, LoginRequest request, HttpServletRequest httpRequest);
}
