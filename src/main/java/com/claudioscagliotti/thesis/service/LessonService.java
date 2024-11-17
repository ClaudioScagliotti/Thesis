package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.exception.CompletedCourseException;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface LessonService {

    /**
     * Retrieves all lessons of a course for a given user.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course.
     * @return A list of LessonDto objects representing all lessons of the course.
     * @throws SubscriptionUserException if the user is not subscribed to the course.
     */
    List<LessonDto> getAllLessonByCourse(String username, Long courseId);

    /**
     * Retrieves the next lesson of a course for a given user.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course.
     * @return A LessonDto object representing the next lesson of the course.
     * @throws SubscriptionUserException if the user is not subscribed to the course.
     * @throws CompletedCourseException  if all lessons of the course are already completed.
     * @throws EntityNotFoundException   if the lesson with the smallest missing ID is not found.
     */
    LessonDto getNextLessonByCourse(String username, Long courseId);

    /**
     * Finds the smallest missing lesson ID from the provided list of lesson IDs and lesson progress entities.
     *
     * @param lessonsIdList      The list of all lesson IDs in the course.
     * @param lessonProgressList The list of lesson progress entities for the user and course.
     * @return The smallest lesson ID that is missing from the lesson progress list, or null if none are missing.
     */
    Long findSmallestMissingId(List<Long> lessonsIdList, List<LessonProgressEntity> lessonProgressList);

    /**
     * Retrieves a LessonEntity by its ID.
     *
     * @param lessonId The ID of the lesson to retrieve.
     * @return The LessonEntity corresponding to the given ID.
     * @throws EntityNotFoundException if no lesson exists with the given ID.
     */
    LessonEntity getById(Long lessonId);
}
