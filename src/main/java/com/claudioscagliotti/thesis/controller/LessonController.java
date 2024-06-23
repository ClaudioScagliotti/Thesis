package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.service.LessonProgressService;
import com.claudioscagliotti.thesis.service.LessonService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;

    public LessonController(LessonService lessonService, LessonProgressService lessonProgressService) {
        this.lessonService = lessonService;
        this.lessonProgressService = lessonProgressService;
    }
    @GetMapping("all/course/{courseId}")
    public ResponseEntity<?> getAllLessonByCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<LessonDto> allLessonByCourse = lessonService.getAllLessonByCourse(userDetails.getUsername(), courseId);

        return ResponseEntity.ok(allLessonByCourse);
    }
    @GetMapping("next/course/{courseId}")
    public ResponseEntity<?> getNextLesson(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LessonDto lesson = lessonService.getNextLessonByCourse(userDetails.getUsername(), courseId);

        return ResponseEntity.ok(lesson);
    }// TODO exceptions
    @GetMapping("{lessonId}/progress")
    public ResponseEntity<?> getLessonProgress(@PathVariable("lessonId") Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LessonProgressDto progress = lessonProgressService.getLessonProgress(userDetails.getUsername(), lessonId);

        return ResponseEntity.ok(progress);
    }// TODO exceptions

    @PostMapping("/update/{lessonId}/progress/{newProgress}")
    public ResponseEntity<?> updateLessonProgress(@PathVariable("lessonId") Long lessonId,
                                                  @PathVariable("newProgress") int newProgress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        LessonProgressDto lessonProgressDto = lessonProgressService.updateLessonProgress(userDetails.getUsername(), lessonId, newProgress);

        return ResponseEntity.ok(lessonProgressDto);
    }


}
