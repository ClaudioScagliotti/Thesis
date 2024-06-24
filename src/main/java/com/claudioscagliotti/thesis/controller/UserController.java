package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.LoginRequest;
import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.dto.response.AuthenticationResponse;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.AuthenticationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final AuthenticationService authService;

    public UserController(AuthenticationService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse register = authService.register(request);
        String message = "Registration succeeded for the username: "+request.getUsername();
        GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, register);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> login( @RequestBody LoginRequest request) {
        try {
            AuthenticationResponse authenticate = authService.authenticate(request);
            String message = "Login succeeded for the username: "+request.getUsername();
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, authenticate);
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<GenericResponse<AuthenticationResponse>> refreshToken( HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        try {
            AuthenticationResponse refreshToken = authService.refreshToken(httpRequest, httpResponse);
            String message = "Refresh succeeded";
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("success", message, refreshToken);
            return ResponseEntity.ok(response);
        }  catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (UnauthorizedUserException e){
            GenericResponse<AuthenticationResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<GenericResponse<Void>> logoutUser() {
        String message = "User logged out successfully";
        GenericResponse<Void> response = new GenericResponse<>("success", message, null);
        return ResponseEntity.ok(response);
    }
}
