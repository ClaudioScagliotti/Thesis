package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.impl.QuizServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Rest controller for managing quizzes.
 */
@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizServiceImpl quizService;

    /**
     * Constructs a QuizController instance with the provided QuizService dependency.
     *
     * @param quizService The QuizService dependency.
     */
    public QuizController(QuizServiceImpl quizService) {
        this.quizService = quizService;
    }

    /**
     * Retrieves all quizzes for a given lesson.
     *
     * @param lessonId The ID of the lesson.
     * @return A ResponseEntity containing the list of quizzes.
     */
    @GetMapping("lesson/{lessonId}")
    public ResponseEntity<GenericResponse<List<QuizDto>>> getAllQuizForLesson(@PathVariable("lessonId") Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<QuizDto> quizDtoList = quizService.findAllByLessonId(lessonId, userDetails.getUsername());
            String message = "There are " + quizDtoList.size() + " quiz for the lesson with id: " + lessonId;
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (SubscriptionUserException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves all quizzes for a given advice.
     *
     * @param adviceId The ID of the advice.
     * @return A ResponseEntity containing the list of quizzes.
     */
    @GetMapping("advice/{adviceId}")
    public ResponseEntity<GenericResponse<List<QuizDto>>> getAllQuizForAdvice(@PathVariable("adviceId") Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            List<QuizDto> quizDtoList = quizService.findAllByAdviceId(adviceId, userDetails.getUsername());
            String message = "There are " + quizDtoList.size() + " quiz for the advice with id: " + adviceId;
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedUserException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        } catch (Exception e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Completes the quizzes.
     *
     * @param quizRequest A list of QuizRequest objects containing the quiz completion data.
     * @return A ResponseEntity containing the completed quizzes.
     */
    @PostMapping
    public ResponseEntity<GenericResponse<List<QuizDto>>> completeQuiz(@Valid @RequestBody List<QuizRequest> quizRequest) {

        try {
            List<QuizDto> quizDtoList = quizService.completeQuiz(quizRequest);
            String message = "Completed " + quizDtoList.size() + " quiz";
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (JsonProcessingException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", "Error with the string userAnswer: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}