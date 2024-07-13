package com.claudioscagliotti.thesis.filter;

import com.claudioscagliotti.thesis.service.JwtService;
import com.claudioscagliotti.thesis.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthenticationFilter is a filter responsible for JWT-based authentication in Spring Security.
 *
 * This filter intercepts incoming requests and validates JWT tokens provided in the Authorization header.
 * It uses JwtService to extract the username from the JWT token and UserDetailsServiceImpl to load
 * user details from the database based on the username.
 *
 * Example usage:
 * - Intercepts requests and checks for a JWT token in the Authorization header.
 * - Extracts the username from the JWT token and validates its authenticity using JwtService.
 * - Loads UserDetails from the database using UserDetailsServiceImpl and populates the SecurityContext
 *   with an authenticated UsernamePasswordAuthenticationToken if the token is valid.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Constructs JwtAuthenticationFilter with JwtService and UserDetailsServiceImpl dependencies.
     *
     * @param jwtService           Service responsible for JWT operations (token extraction and validation).
     * @param userDetailsService  Service for loading user-specific data.
     */
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Filters incoming requests to authenticate based on JWT token.
     *
     * @param request     HTTP request object.
     * @param response    HTTP response object.
     * @param filterChain Filter chain for processing the request.
     * @throws ServletException If an error occurs during servlet processing.
     * @throws IOException      If an I/O error occurs during the execution of the filter.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtService.isValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}

