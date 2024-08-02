package com.claudioscagliotti.thesis.configuration;

import com.claudioscagliotti.thesis.model.TokenEntity;
import com.claudioscagliotti.thesis.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

/**
 * CustomLogoutHandler implements the LogoutHandler interface to handle logout operations
 * by marking bearer tokens as logged out in the database.
 *
 * This handler extracts the bearer token from the Authorization header of the HTTP request,
 * queries the TokenRepository to find the corresponding TokenEntity, and updates its
 * loggedOut status to true. This ensures that the token is invalidated upon user logout,
 * preventing further use for authentication.
 *
 * Example usage:
 * When a user initiates a logout request, this handler is invoked to process the request,
 * ensuring that the user's token is marked as logged out in the token repository.
 *
 * @see LogoutHandler
 * @see TokenRepository
 * @see TokenEntity
 */
@Configuration
public class CustomLogoutHandler implements LogoutHandler {

    private final TokenRepository tokenRepository;

    /**
     * Constructs a CustomLogoutHandler with the specified TokenRepository.
     *
     * @param tokenRepository The repository used to access and update token information.
     */
    public CustomLogoutHandler(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    /**
     * Handles the logout operation by marking the bearer token as logged out in the database.
     *
     * @param request        The HTTP request representing the logout operation.
     * @param response       The HTTP response associated with the request.
     * @param authentication The authentication object representing the authenticated user.
     */
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        String token = authHeader.substring(7);

        TokenEntity storedToken = tokenRepository.findByAccessToken(token).orElse(null);

        if (storedToken != null) {
            storedToken.setLoggedOut(true);
            tokenRepository.save(storedToken);
        }
    }
}