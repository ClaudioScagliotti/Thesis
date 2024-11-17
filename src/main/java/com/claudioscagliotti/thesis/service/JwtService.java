package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.UserEntity;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JwtService {

    /**
     * Generates an access token for the given user entity.
     *
     * @param user The UserEntity for which the access token is generated.
     * @return The generated access token as a string.
     */
    String generateAccessToken(UserEntity user);

    /**
     * Generates a refresh token for the given user entity.
     *
     * @param user The UserEntity for which the refresh token is generated.
     * @return The generated refresh token as a string.
     */
    String generateRefreshToken(UserEntity user);

    /**
     * Generates a reset token for the given user entity.
     *
     * @param user The UserEntity for which the reset token is generated.
     * @return The generated reset token as a string.
     */
    String generateResetToken(UserEntity user);

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token from which the username is extracted.
     * @return The username extracted from the token.
     */
    String extractUsername(String token);

    /**
     * Checks if the provided JWT token is valid for the specified UserDetails.
     *
     * @param token The JWT token to validate.
     * @param user  The UserDetails object representing the user to validate against.
     * @return true if the token is valid, false otherwise.
     */
    boolean isValid(String token, UserDetails user);

    /**
     * Checks if the provided refresh token is valid for the specified UserEntity.
     *
     * @param token The refresh token to validate.
     * @param user  The UserEntity representing the user to validate against.
     * @return true if the refresh token is valid, false otherwise.
     */
    boolean isValidRefreshToken(String token, UserEntity user);

    /**
     * Validates the reset password token.
     *
     * @param token The reset password token to validate.
     * @param username The username of the user who wants to create a new password.
     * @return true if the reset token is valid, false otherwise.
     */
    boolean isValidResetPasswordToken(String token, String username);

    /**
     * Extracts a specific claim from the JWT token using the provided resolver function.
     *
     * @param token    The JWT token from which the claim is extracted.
     * @param resolver The function used to resolve the specific claim from the Claims object.
     * @param <T>      The type of the claim being extracted.
     * @return The resolved claim value.
     */
    <T> T extractClaim(String token, Function<Claims, T> resolver);
}
