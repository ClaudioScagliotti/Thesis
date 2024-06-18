package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.service.AdviceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@RestController
@RequestMapping("/advice")
public class AdivceController {
private final AdviceService adviceService;

    public AdivceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping
    public ResponseEntity<?> createAdviceList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<AdviceDto> adviceList = adviceService.getAdviceListResponse(userDetails.getUsername());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(adviceList.stream().findFirst().orElseThrow().getId())
                .toUri();
        return ResponseEntity.created(location).body(adviceList);
    }//TODO exceptions

    @PostMapping("/skip/{adviceId}")
    public ResponseEntity<?> skipNextAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdviceDto adviceDto= adviceService.skipAdvice(userDetails.getUsername(), adviceId);

        return ResponseEntity.ok(adviceDto);
    }//TODO exceptions

    @PostMapping("/complete/{adviceId}")
    public ResponseEntity<?> completeAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdviceDto adviceDto= adviceService.completeAdvice(userDetails.getUsername(), adviceId);

        return ResponseEntity.ok(adviceDto);
    }//TODO exceptions

    @GetMapping
    public ResponseEntity<?> getNextAdvice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AdviceDto advice = adviceService.getNextAdvice(userDetails.getUsername());

        return ResponseEntity.ok(advice);
    }
}
