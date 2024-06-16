package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{number}")
                .buildAndExpand(quizDtoList.size())
                .toUri();
        return ResponseEntity.ok(quizDtoList);
    }
    @GetMapping("advice/{adviceId}")
    public ResponseEntity<?> getAllQuizForAdvice(@PathVariable("adviceId")Long adviceId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<QuizDto> quizDtoList= quizService.findAllByAdviceId(adviceId);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{number}")
                .buildAndExpand(quizDtoList.size())
                .toUri();
        return ResponseEntity.ok(quizDtoList);
    }
}
