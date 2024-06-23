package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.dto.response.GenericResponse;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("subscribe/{courseId}")
    public ResponseEntity<GenericResponse<CourseDto>> subscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            CourseDto courseDto = courseService.subscribeCourse(userDetails.getUsername(), courseId);
            String message = "Subscribed course with id: " + courseId;
            GenericResponse<CourseDto> response = new GenericResponse<>("success", message, courseDto);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e){
            GenericResponse<CourseDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (SubscriptionUserException e) {
            GenericResponse<CourseDto> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @PostMapping("unsubscribe/{courseId}")
    public ResponseEntity<GenericResponse<Object>> unsubscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
        courseService.unsubscribeCourse(userDetails.getUsername(), courseId);

        String message = "Unsubscribed from course with id: " + courseId +" successfully";
        GenericResponse<Object> response = new GenericResponse<>("success", message, null);
        return ResponseEntity.ok(response);

        } catch (SubscriptionUserException e){
            GenericResponse<Object> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<Object> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }

    @GetMapping("suggested")
    public ResponseEntity<GenericResponse<List<CourseDto>>> suggestCourses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<CourseDto> dtoList = courseService.suggestCourses(userDetails.getUsername());
            String message = "Suggested " + dtoList.size() +" courses";
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("success", message, dtoList);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

    }
    @GetMapping()
    public ResponseEntity<?> subscribedCourses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        try {
            List<CourseDto> dtoList = courseService.subscribedCourses(userDetails.getUsername());
            String message = "Subscribed to " + dtoList.size() +" courses";
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("success", message, dtoList);
            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | EntityNotFoundException e) {
            GenericResponse<List<CourseDto>> response = new GenericResponse<>("error", e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
