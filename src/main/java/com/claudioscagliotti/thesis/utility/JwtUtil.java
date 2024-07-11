package com.claudioscagliotti.thesis.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * Utility class for generating and validating JWT tokens.
 */
@Component
public class JwtUtil {

    /**
     * Secret key used for signing JWT tokens.
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /**
     * Expiration time for JWT tokens in milliseconds.
     */
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    /**
     * Key used for signing JWT tokens using HS256 algorithm.
     */
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    /**
     * Generates a JWT token for the given authentication object.
     *
     * @param authentication the authentication object containing the user's details.
     * @return the generated JWT token.
     */
    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    /**
     * Validates the given JWT token.
     *
     * @param token the JWT token to validate.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts the username from the given JWT token.
     *
     * @param token the JWT token.
     * @return the username contained in the token.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }
}
