package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.exception.CompletedCourseException;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.service.LessonProgressService;
import com.claudioscagliotti.thesis.service.LessonService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Rest controller for managing lessons and lesson progress.
 */
@RestController
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;
    private final LessonProgressService lessonProgressService;

    /**
     * Constructs a LessonController instance with the provided dependencies.
     *
     * @param lessonService         The LessonService dependency.
     * @param lessonProgressService The LessonProgressService dependency.
     */
    public LessonController(LessonService lessonService, LessonProgressService lessonProgressService) {
        this.lessonService = lessonService;
        this.lessonProgressService = lessonProgressService;
    }

    /**
     * Retrieves all lessons for a given course.
     *
     * @param courseId The ID of the course.
     * @return A ResponseEntity containing the list of lessons.
     */
    @GetMapping("all/course/{courseId}")
    public ResponseEntity<GenericResponse<List<LessonDto>>> getAllLessonByCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<LessonDto> lessonDtoList = lessonService.getAllLessonByCourse(userDetails.getUsername(), courseId);
            String message = "The course with id: " + courseId + " has " + lessonDtoList.size() + " lessons";
            GenericResponse<List<LessonDto>> response = new GenericResponse<>("success", message, lessonDtoList);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<List<LessonDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (SubscriptionUserException e) {
            GenericResponse<List<LessonDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<List<LessonDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the next lesson for a given course.
     *
     * @param courseId The ID of the course.
     * @return A ResponseEntity containing the next lesson.
     */
    @GetMapping("next/course/{courseId}")
    public ResponseEntity<GenericResponse<LessonDto>> getNextLesson(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            LessonDto lesson = lessonService.getNextLessonByCourse(userDetails.getUsername(), courseId);
            String message = "The next lesson for the course with id: " + courseId + " is " + lesson.getId();
            GenericResponse<LessonDto> response = new GenericResponse<>("success", message, lesson);
            return ResponseEntity.ok(response);

        } catch (SubscriptionUserException | CompletedCourseException e) {
            GenericResponse<LessonDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<LessonDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<LessonDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the progress of a specific lesson.
     *
     * @param lessonId The ID of the lesson.
     * @return A ResponseEntity containing the lesson progress.
     */
    @GetMapping("{lessonId}/progress")
    public ResponseEntity<GenericResponse<LessonProgressDto>> getLessonProgress(@PathVariable("lessonId") Long lessonId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            LessonProgressDto progress = lessonProgressService.getLessonProgress(userDetails.getUsername(), lessonId);
            String message = "Retrieved the progress for the lesson with id: " + lessonId;
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("success", message, progress);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Updates the progress of a specific lesson.
     *
     * @param lessonId    The ID of the lesson.
     * @param newProgress The new progress value.
     * @return A ResponseEntity containing the updated lesson progress.
     */
    @PostMapping("update/{lessonId}/progress/{newProgress}")
    public ResponseEntity<GenericResponse<LessonProgressDto>> updateLessonProgress(@PathVariable("lessonId") Long lessonId,
                                                                                   @PathVariable("newProgress") int newProgress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            LessonProgressDto lessonProgressDto = lessonProgressService.updateLessonProgress(userDetails.getUsername(), lessonId, newProgress);
            String message = "Updated progress for the lesson with id: " + lessonId;
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("success", message, lessonProgressDto);
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException | UsernameNotFoundException e) {
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IndexOutOfBoundsException | IllegalStateException e) {
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<LessonProgressDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
