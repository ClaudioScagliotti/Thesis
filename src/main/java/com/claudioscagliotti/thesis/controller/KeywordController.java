package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.exception.ExternalApiException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import com.claudioscagliotti.thesis.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller for managing keywords.
 */
@RestController
@RequestMapping("/keywords")
public class KeywordController {
    private final TmdbApiClient client;
    private final UserServiceImpl userService;

    /**
     * Constructs a KeywordController instance with the provided dependencies.
     *
     * @param client         The TmdbApiClient dependency.
     * @param userService    The UserService dependency.
     */
    public KeywordController(TmdbApiClient client, UserServiceImpl userService) {
        this.client = client;
        this.userService = userService;
    }

    /**
     * Retrieves keywords matching the provided search term. Only accessible by users with the admin role.
     *
     * @param keyword The search term for keywords.
     * @return A ResponseEntity containing the keyword search results.
     */
    @GetMapping
    public ResponseEntity<GenericResponse<KeywordResponse>> getKeywords(@RequestParam String keyword) {
        try {
            userService.checkIsAdmin();
            KeywordResponse keywordResponse = client.searchKeywords(keyword);
            String message = "Found " + keywordResponse.totalResults() + " keywords";
            GenericResponse<KeywordResponse> response = new GenericResponse<>("success", message, keywordResponse);
            return ResponseEntity.ok(response);

        } catch (ExternalApiException e) {
            GenericResponse<KeywordResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (UnauthorizedUserException e) {
            GenericResponse<KeywordResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<KeywordResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}