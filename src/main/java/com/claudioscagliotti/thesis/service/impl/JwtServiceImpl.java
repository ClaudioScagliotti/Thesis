package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.TokenRepository;
import com.claudioscagliotti.thesis.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

/**
 * Service class for JWT token generation and validation.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long accessTokenExpire;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpire;

    @Value("${jwt.reset.expiration}")
    private long resetTokenExpire;

    private final TokenRepository tokenRepository;

    /**
     * Constructs a JwtService with the specified TokenRepository.
     *
     * @param tokenRepository The TokenRepository used to interact with token data.
     */
    public JwtServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Generates an access token for the given user entity.
     *
     * @param user The UserEntity for which the access token is generated.
     * @return The generated access token as a string.
     */
    public String generateAccessToken(UserEntity user) {
        return generateToken(user, accessTokenExpire, "access_token");
    }

    /**
     * Generates a refresh token for the given user entity.
     *
     * @param user The UserEntity for which the refresh token is generated.
     * @return The generated refresh token as a string.
     */
    public String generateRefreshToken(UserEntity user) {
        return generateToken(user, refreshTokenExpire, "refresh_token");
    }

    /**
     * Generates a reset token for the given user entity.
     *
     * @param user The UserEntity for which the reset token is generated.
     * @return The generated reset token as a string.
     */
    public String generateResetToken(UserEntity user) {
        return generateToken(user, resetTokenExpire, "reset_password_token");
    }


    /**
     * Generates a JWT token for the specified user entity and expiration time.
     *
     * @param user       The UserEntity for which the token is generated.
     * @param expireTime The expiration time of the token in milliseconds.
     * @param claimType  The claim type to include in the token (e.g., "access_token").
     * @return The generated JWT token as a string.
     */
    private String generateToken(UserEntity user, long expireTime, String claimType) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("type", claimType)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Retrieves the signing key from the secret key.
     *
     * @return The SecretKey used for signing JWT tokens.
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extracts all claims from the JWT token.
     *
     * @param token The JWT token from which claims are extracted.
     * @return The Claims object containing all parsed claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extracts the username from the JWT token.
     *
     * @param token The JWT token from which the username is extracted.
     * @return The username extracted from the token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Checks if the provided JWT token is valid for the specified UserDetails.
     *
     * @param token The JWT token to validate.
     * @param user  The UserDetails object representing the user to validate against.
     * @return true if the token is valid, false otherwise.
     */
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean validToken = tokenRepository.findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && isTokenNotExpired(token) && validToken;
    }

    /**
     * Checks if the provided refresh token is valid for the specified UserEntity.
     *
     * @param token The refresh token to validate.
     * @param user  The UserEntity representing the user to validate against.
     * @return true if the refresh token is valid, false otherwise.
     */
    public boolean isValidRefreshToken(String token, UserEntity user) {
        String username = extractUsername(token);

        boolean validRefreshToken = tokenRepository.findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && isTokenNotExpired(token) && validRefreshToken;
    }

    /**
     * Checks if the provided JWT token has expired.
     *
     * @param token The JWT token to check.
     * @return true if the token has not expired, false if it has expired.
     */
    private boolean isTokenNotExpired(String token) {
        return !extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token from which the expiration date is extracted.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided resolver function.
     *
     * @param token    The JWT token from which the claim is extracted.
     * @param resolver The function used to resolve the specific claim from the Claims object.
     * @param <T>      The type of the claim being extracted.
     * @return The resolved claim value.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Validates the reset password token.
     *
     * @param token The reset password token to validate.
     * @param username The username of the user who want to create the new password .
     * @return true if the reset token is valid, false otherwise.
     */
    public boolean isValidResetPasswordToken(String token, String username) {
        String tokenUsername = extractUsername(token);
        boolean validToken = tokenRepository.findByResetPasswordToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (tokenUsername.equals(username)) &&isTokenNotExpired(token) && validToken;
    }
}