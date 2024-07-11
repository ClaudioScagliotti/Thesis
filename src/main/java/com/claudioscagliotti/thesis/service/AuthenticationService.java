package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.mapper.UserMapper;
import com.claudioscagliotti.thesis.model.TokenEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.TokenRepository;
import com.claudioscagliotti.thesis.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for user authentication and registration.
 */
@Service
public class AuthenticationService {

    private final UserMapper userMapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructs an AuthenticationService with the required dependencies.
     *
     * @param userMapper            The mapper for converting between UserEntity and DTOs.
     * @param repository            The repository for accessing user data.
     * @param passwordEncoder       The encoder for password hashing.
     * @param jwtService            The service for JWT token generation and validation.
     * @param tokenRepository       The repository for managing authentication tokens.
     * @param authenticationManager The authentication manager for authenticating users.
     */
    public AuthenticationService(UserMapper userMapper, UserRepository repository,
                                 PasswordEncoder passwordEncoder, JwtService jwtService,
                                 TokenRepository tokenRepository, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.repository = repository;
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
    public AuthenticationResponse register(RegisterRequest request) {
        if (repository.findByUsername(request.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, null, "User already exists");
        }

        UserEntity user = userMapper.toUserEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPoints(0);
        user.setStreak(0);
        user = repository.save(user);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(accessToken, refreshToken, user);
        return new AuthenticationResponse(accessToken, refreshToken, "User registration was successful");
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

        UserEntity user = repository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, refreshToken, user);

        return new AuthenticationResponse(accessToken, refreshToken, "User login was successful");
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
    private void saveUserToken(String accessToken, String refreshToken, UserEntity user) {
        TokenEntity token = new TokenEntity();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param request  The HTTP request containing the refresh token in the Authorization header.
     * @param response The HTTP response to be modified upon successful token refresh.
     * @return An AuthenticationResponse containing the new access and refresh tokens.
     * @throws UnauthorizedUserException If the refresh token is invalid.
     */
    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedUserException("Bearer token is missing");
        }

        String token = authHeader.substring(7); // Extract token from Authorization header

        String username = jwtService.extractUsername(token);
        UserEntity user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user found"));

        if (jwtService.isValidRefreshToken(token, user)) {
            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new AuthenticationResponse(accessToken, refreshToken, "New tokens generated");
        } else {
            throw new UnauthorizedUserException("Invalid refresh token");
        }
    }
}
