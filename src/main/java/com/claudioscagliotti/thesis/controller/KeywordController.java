package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.tmdb.response.keyword.KeywordResponse;
import com.claudioscagliotti.thesis.proxy.tmdb.TmdbApiClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/keywords")
public class KeywordController {

    private final TmdbApiClient client;
    public KeywordController(TmdbApiClient client) {
        this.client = client;
    }
    //DATA UNA WEYWORD, CHIAMO TMDB E RICERCO I RISULTATI
    //SE è PIù DI UNA PAGINA CHIEDO DI ESSERE PIù SPECIFICO
    //LA PRIMA LA MOSTRO COMUNQUE
    @GetMapping
    public ResponseEntity<KeywordResponse> getKeywords(@RequestParam String keyword){
        return ResponseEntity.ok().body(client.searchKeywords(keyword));
    }
}
