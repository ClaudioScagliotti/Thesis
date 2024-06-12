package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.service.AdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/advices")
public class AdivceController {
private final AdviceService adviceService;

    public AdivceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping
    public ResponseEntity<?> createAdviceList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<AdviceEntity> adviceList = adviceService.createAdviceList(userDetails.getUsername());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adviceList.stream().findFirst().orElseThrow().getId())
                .toUri();
        return ResponseEntity.created(location).body(adviceList.stream().findFirst());
    }//TODO exceptions
}
