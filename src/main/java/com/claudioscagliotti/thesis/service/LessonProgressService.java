package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import jakarta.persistence.EntityNotFoundException;

public interface LessonProgressService {

    /**
     * Retrieves the lesson progress for a specific lesson and user.
     *
     * @param username The username of the user.
     * @param lessonId The ID of the lesson.
     * @return The LessonProgressDto representing the progress of the lesson for the user.
     * @throws EntityNotFoundException if no lesson progress is associated with the given lesson ID.
     */
    LessonProgressDto getLessonProgress(String username, Long lessonId);

    /**
     * Updates the lesson progress for a specific lesson and user.
     *
     * @param username          The username of the user.
     * @param lessonId          The ID of the lesson.
     * @param newCompletedCards The number of newly completed cards to update the progress.
     * @return The updated LessonProgressDto after updating the progress.
     * @throws EntityNotFoundException   if no lesson is found with the given lesson ID.
     * @throws IndexOutOfBoundsException if the new progress exceeds the total number of cards in the lesson.
     */
    LessonProgressDto updateLessonProgress(String username, Long lessonId, Integer newCompletedCards);

    /**
     * Completes the lesson progress for a user upon finishing all lesson requirements.
     *
     * @param userEntity           The UserEntity representing the user.
     * @param lessonEntity         The LessonEntity representing the lesson.
     * @param lessonProgressEntity The LessonProgressEntity representing the lesson progress.
     */
    void completeLesson(UserEntity userEntity, LessonEntity lessonEntity, LessonProgressEntity lessonProgressEntity);

    /**
     * Creates a new lesson progress for a user for the specified lesson.
     *
     * @param userEntity       The UserEntity representing the user.
     * @param lessonId         The ID of the lesson.
     * @param newCompletedCard The number of completed cards for the new lesson progress.
     * @throws EntityNotFoundException if no lesson is found with the given lesson ID.
     * @throws IllegalStateException   if a lesson progress already exists for the user and lesson.
     */
    void createLessonProgress(UserEntity userEntity, Long lessonId, Integer newCompletedCard);

}
