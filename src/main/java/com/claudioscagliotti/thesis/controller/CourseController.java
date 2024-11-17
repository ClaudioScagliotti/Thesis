package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.service.impl.CourseServiceImpl;
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
 * Rest controller for managing course subscriptions and suggestions.
 */
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseServiceImpl courseService;

    /**
     * Constructs a CourseController instance with the provided dependencies.
     *
     * @param courseService The CourseService dependency.
     */
    public CourseController(CourseServiceImpl courseService) {
        this.courseService = courseService;
    }

    /**
     * Subscribes the authenticated user to a course.
     *
     * @param courseId The ID of the course to subscribe to.
     * @return A ResponseEntity containing the subscribed course details.
     */
    @PostMapping("subscribe/{courseId}")
    public ResponseEntity<GenericResponse<CourseDto>> subscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            CourseDto courseDto = courseService.subscribeCourse(userDetails.getUsername(), courseId);
            String message = "Subscribed to course with id: " + courseId;
            GenericResponse<CourseDto> response = new GenericResponse<>("success", message, courseDto);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<CourseDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SubscriptionUserException e) {
            GenericResponse<CourseDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            GenericResponse<CourseDto> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Unsubscribes the authenticated user from a course.
     *
     * @param courseId The ID of the course to unsubscribe from.
     * @return A ResponseEntity indicating the result of the unsubscription.
     */
    @PostMapping("unsubscribe/{courseId}")
    public ResponseEntity<GenericResponse<Void>> unsubscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            courseService.unsubscribeCourse(userDetails.getUsername(), courseId);
            String message = "Unsubscribed from course with id: " + courseId + " successfully";
            GenericResponse<Void> response = new GenericResponse<>("success", message, null);
            return ResponseEntity.ok(response);

        } catch (SubscriptionUserException e) {
            GenericResponse<Void> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<Void> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<Void> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Suggests courses for the authenticated user.
     *
     * @return A ResponseEntity containing the list of suggested courses.
     */
    @GetMapping("suggested")
    public ResponseEntity<GenericResponse<List<CourseDto>>> suggestCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<CourseDto> dtoList = courseService.suggestCourses(userDetails.getUsername());
            String message = "Suggested " + dtoList.size() + " courses";
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("success", message, dtoList);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Retrieves the courses the authenticated user is subscribed to.
     *
     * @return A ResponseEntity containing the list of subscribed courses.
     */
    @GetMapping
    public ResponseEntity<GenericResponse<List<CourseDto>>> subscribedCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<CourseDto> dtoList = courseService.subscribedCourses(userDetails.getUsername());
            String message = "Subscribed to " + dtoList.size() + " courses";
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("success", message, dtoList);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", "An unexpected error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}