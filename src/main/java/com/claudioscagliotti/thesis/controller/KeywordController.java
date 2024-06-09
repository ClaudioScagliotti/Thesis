package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
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
    public ResponseEntity<KeywordResponse> getKeywords(@RequestParam String keyword){
        return ResponseEntity.ok().body(client.searchKeywords(keyword));
    }
}
