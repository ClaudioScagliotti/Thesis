package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.exception.ExternalAPIException;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
    private final TmdbApiClient client;

    public KeywordController(TmdbApiClient client) {
        this.client = client;
    }

    @GetMapping
    public ResponseEntity<GenericResponse<KeywordResponse>> getKeywords(@RequestParam String keyword) {//TODO only an admin with user role can do this call
        try {
            KeywordResponse keywordResponse = client.searchKeywords(keyword);
            String message = "Found " + keywordResponse.totalResults() + " keywords";
            GenericResponse<KeywordResponse> response = new GenericResponse<>("success", message, keywordResponse);
            return ResponseEntity.ok(response);

        } catch (ExternalAPIException e) {
            GenericResponse<KeywordResponse> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
