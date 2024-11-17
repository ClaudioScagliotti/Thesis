package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface CourseService {
    /**
     * Retrieves the list of courses that the user is subscribed to.
     *
     * @param username The username of the user.
     * @return List of CourseDto representing subscribed courses.
     */
    List<CourseDto> subscribedCourses(String username);

    /**
     * Retrieves a list of courses suggested based on the user's goal type.
     *
     * @param username The username of the user.
     * @return List of CourseDto representing suggested courses.
     */
    List<CourseDto> suggestCourses(String username);

    /**
     * Subscribes the user to a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to subscribe.
     * @return CourseDto representing the subscribed course.
     * @throws EntityNotFoundException   If the course with the given courseId is not found.
     * @throws SubscriptionUserException If the user is already subscribed to the course.
     */
    CourseDto subscribeCourse(String username, Long courseId);

    /**
     * Unsubscribes the user from a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to unsubscribe.
     * @throws SubscriptionUserException If the user is not subscribed to the course.
     */
    void unsubscribeCourse(String username, Long courseId);

    /**
     * Checks if the user is subscribed to a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to check subscription for.
     * @return true if the user is subscribed to the course, false otherwise.
     */
    boolean checkSubscription(String username, Long courseId);
}
