package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.AdviceDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.NoAdviceAvailableException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.AdviceService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/advice")
public class AdviceController {
private final AdviceService adviceService;

    public AdviceController(AdviceService adviceService) {
        this.adviceService = adviceService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<List<AdviceDto>>> createAdviceList() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        List<AdviceDto> adviceList = adviceService.createAdviceList(userDetails.getUsername());
        String message= "Created "+adviceList.size()+" advices";

        GenericResponse<List<AdviceDto>> response= new GenericResponse<>("success", message,adviceList);

        return ResponseEntity.ok(response);
    }//TODO exceptions

    @PostMapping("/skip/{adviceId}")
    public ResponseEntity<GenericResponse<AdviceDto>> skipNextAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto = adviceService.skipAdvice(userDetails.getUsername(), adviceId);
            String message = "Skipped advice with id: " + adviceId;
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/complete/{adviceId}")
    public ResponseEntity<GenericResponse<AdviceDto>> completeAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto= adviceService.completeAdvice(userDetails.getUsername(), adviceId);
            String message = "Completed advice with id: " + adviceId;
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedUserException e){
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

    }

    @GetMapping
    public ResponseEntity<GenericResponse<AdviceDto>> getNextAdvice() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            AdviceDto adviceDto = adviceService.getNextAdvice(userDetails.getUsername());
            String message = "Found advice with id: " + adviceDto.getId();
            GenericResponse<AdviceDto> response = new GenericResponse<>("success", message, adviceDto);
            return ResponseEntity.ok(response);

        } catch (NoAdviceAvailableException e){
            GenericResponse<AdviceDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
