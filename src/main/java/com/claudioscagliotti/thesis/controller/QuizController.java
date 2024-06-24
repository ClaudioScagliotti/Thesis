package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.service.QuizService;
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

@RestController
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("lesson/{lessonId}")
    public ResponseEntity<GenericResponse<List<QuizDto>>> getAllQuizForLesson(@PathVariable("lessonId")Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<QuizDto> quizDtoList= quizService.findAllByLessonId(lessonId, userDetails.getUsername());
            String message = "There are " + quizDtoList.size()+" quiz for the lesson with id: "+lessonId;
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        }  catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (SubscriptionUserException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    @GetMapping("advice/{adviceId}")
    public ResponseEntity<GenericResponse<List<QuizDto>>> getAllQuizForAdvice(@PathVariable("adviceId")Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            List<QuizDto> quizDtoList= quizService.findAllByAdviceId(adviceId, userDetails.getUsername());
            String message = "There are " + quizDtoList.size()+" quiz for the advice with id: "+adviceId;
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        }  catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (UnauthorizedUserException e){
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<GenericResponse<List<QuizDto>>> completeQuiz(@Valid @RequestBody List<QuizRequest> quizRequest) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        try {
            List<QuizDto> quizDtoList = quizService.completeQuiz(quizRequest);
            String message = "Completed " + quizDtoList.size()+" quiz";
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("success", message, quizDtoList);
            return ResponseEntity.ok(response);

        }  catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }  catch (JsonProcessingException e) {
            GenericResponse<List<QuizDto>> response = new GenericResponse<>("error", "Error with the string userAnswer: "+e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
}
