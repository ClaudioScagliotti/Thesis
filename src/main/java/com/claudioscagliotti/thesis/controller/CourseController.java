package com.claudioscagliotti.thesis.controller;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("subscribe/{courseId}")
    public ResponseEntity<?> subscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        CourseDto courseDto = courseService.subscribeCourse(userDetails.getUsername(), courseId);

        return ResponseEntity.ok(courseDto);
    }//TODO exceptions

    @PostMapping("unsubscribe/{courseId}")
    public ResponseEntity<?> unsubscribeCourse(@PathVariable("courseId") Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        courseService.unsubscribeCourse(userDetails.getUsername(), courseId);

        return ResponseEntity.ok("Unsubscribed from course successfully");
    }

    @GetMapping("suggested")
    public ResponseEntity<?> suggestCourses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok().body(courseService.suggestCourses(userDetails.getUsername()));
    }
    @GetMapping()
    public ResponseEntity<?> subscribedCourses(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok().body(courseService.subscribedCourses(userDetails.getUsername()));
    }
}
