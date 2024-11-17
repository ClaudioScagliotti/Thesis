package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.mapper.UserMapper;
import com.claudioscagliotti.thesis.model.TokenEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.TokenRepository;
import com.claudioscagliotti.thesis.repository.UserRepository;
import com.claudioscagliotti.thesis.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * Service class for user authentication and registration.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtServiceImpl jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an AuthenticationService with the required dependencies.
     *
     * @param userMapper            The mapper for converting between UserEntity and DTOs.
     * @param userRepository        The repository for accessing user data.
     * @param passwordEncoder       The encoder for password hashing.
     * @param jwtService            The service for JWT token generation and validation.
     * @param tokenRepository       The repository for managing authentication tokens.
     * @param authenticationManager The authentication manager for authenticating users.
     */
    public AuthenticationServiceImpl(UserMapper userMapper, UserRepository userRepository,
                                     PasswordEncoder passwordEncoder, JwtServiceImpl jwtService,
                                     TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The registration request containing user details.
     * @return An AuthenticationResponse indicating the success or failure of registration.
     */
    public AuthenticationResponse register(RegisterRequest request) throws BadRequestException {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw  new BadRequestException("Username already exists");
        }

        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPoints(0);
        user.setStreak(0);
        user = userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, null, user);
        return new AuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request The login request containing username and password.
     * @return An AuthenticationResponse containing access and refresh tokens upon successful authentication.
     * @throws UsernameNotFoundException If no user with the given username exists.
     */
    public AuthenticationResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserEntity user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, null, user);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    /**
     * Revokes all access tokens associated with the specified user.
     *
     * @param user The user entity for whom tokens are to be revoked.
     */
    private void revokeAllTokenByUser(UserEntity user) {
        List<TokenEntity> validTokens = tokenRepository.findAllAccessTokensByUser(user.getId());
        if (validTokens.isEmpty()) {
            return;
        }
        validTokens.forEach(t -> t.setLoggedOut(true));
        tokenRepository.saveAll(validTokens);
    }

    /**
     * Saves a new access and refresh token pair for the specified user.
     *
     * @param accessToken  The generated access token.
     * @param refreshToken The generated refresh token.
     * @param user         The user entity for whom tokens are being saved.
     */
    private void saveUserToken(String accessToken, String refreshToken, String resetToken, UserEntity user) {
        TokenEntity token = new TokenEntity();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setResetPasswordToken(resetToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param request  The HTTP request containing the refresh token in the Authorization header.
     * @return An AuthenticationResponse containing the new access and refresh tokens.
     * @throws UnauthorizedUserException If the refresh token is invalid.
     */
    public AuthenticationResponse refreshToken(HttpServletRequest request) {
        String token = extractTokenFromHttpRequest(request);

        String username = jwtService.extractUsername(token);
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        if (jwtService.isValidRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, null, user);

            return new AuthenticationResponse(accessToken, refreshToken);
        } else {
            throw new UnauthorizedUserException("Invalid refresh token");
        }
    }

    /**
     * Checks if the authenticated user has a specific role.
     *
     * @param role the role to check (e.g., "ROLE_USER" or "ROLE_ADMIN")
     * @return true if the user has the specified role, otherwise false
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Generates a reset password token for the given user and saves it.
     *
     * @param user The UserEntity for which the reset password token is generated.
     * @return The generated reset password token as a string.
     */
    public String resetPassword(UserEntity user){
        String resetToken= jwtService.generateResetToken(user);
        saveUserToken(null,null, resetToken, user);
        return resetToken;
    }

    /**
     * Saves the new password for the user if the reset token is valid.
     *
     * @param user The UserEntity for which the password is being reset.
     * @param request The LoginRequest containing the new password.
     * @param httpRequest The HttpServletRequest containing the reset token in the Authorization header.
     * @throws UnauthorizedUserException if the reset token is invalid or missing.
     */
    public void saveNewPassword(UserEntity user, LoginRequest request, HttpServletRequest httpRequest) {
        String token = extractTokenFromHttpRequest(httpRequest);
        if(jwtService.isValidResetPasswordToken(token, user.getUsername())){
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(user);

            markResetTokenAsUsed(token);
        } else {
            throw new UnauthorizedUserException("Invalid reset token");
        }
    }

    /**
     * Extracts the token from the Authorization header of the HttpServletRequest.
     *
     * @param httpRequest The HttpServletRequest containing the Authorization header.
     * @return The extracted token as a string.
     * @throws UnauthorizedUserException if the Authorization header is missing or does not contain a Bearer token.
     */
    private static String extractTokenFromHttpRequest(HttpServletRequest httpRequest) {
        String authHeader = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedUserException("Bearer token is missing");
        }
        return authHeader.substring(7);
    }

    /**
     * Marks the reset token as used by setting the isLoggedOut flag to true.
     *
     * @param token The reset token to be marked as used.
     * @throws UnauthorizedUserException if the reset token is invalid.
     */
    private void markResetTokenAsUsed(String token) {
        TokenEntity tokenEntity = tokenRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new UnauthorizedUserException("Invalid reset token"));
        tokenEntity.setLoggedOut(true);
        tokenRepository.save(tokenEntity);
    }
}
