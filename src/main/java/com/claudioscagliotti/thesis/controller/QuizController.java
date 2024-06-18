package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.service.QuizService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("lesson/{lessonId}") //TODO non funziona ancora l'ereditarietà in modo corretto per la query
    public ResponseEntity<?> getAllQuizForLesson(@PathVariable("lessonId")Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<QuizDto> quizDtoList= quizService.findAllByLessonId(lessonId);

        return ResponseEntity.ok(quizDtoList);
    }
    @GetMapping("advice/{adviceId}")
    public ResponseEntity<?> getAllQuizForAdvice(@PathVariable("adviceId")Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<QuizDto> quizDtoList= quizService.findAllByAdviceId(adviceId);

        return ResponseEntity.ok(quizDtoList);
    }

    @PostMapping
    public ResponseEntity<?> completeQuiz(@Valid @RequestBody List<QuizRequest> quizRequest) throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<QuizDto> quizDtoList = quizService.completeQuiz(quizRequest);

        return ResponseEntity.ok(quizDtoList);
    }//TODO exceptions
}
