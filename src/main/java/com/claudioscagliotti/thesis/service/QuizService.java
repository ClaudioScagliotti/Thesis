package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface QuizService {

    /**
     * Finds all quizzes by advice ID and checks if the user is authorized.
     *
     * @param adviceId The ID of the advice.
     * @param username The username of the user.
     * @return A list of QuizDto objects.
     * @throws UnauthorizedUserException if the user does not own the advice.
     */
    List<QuizDto> findAllByAdviceId(Long adviceId, String username);

    /**
     * Finds all quizzes by lesson ID and checks if the user is subscribed to the course.
     *
     * @param lessonId The ID of the lesson.
     * @param username The username of the user.
     * @return A list of QuizDto objects.
     * @throws SubscriptionUserException if the user is not subscribed to the course.
     */
    List<QuizDto> findAllByLessonId(Long lessonId, String username);

    /**
     * Completes the quizzes based on the provided requests.
     *
     * @param requestList The list of quiz requests.
     * @return A list of QuizDto objects.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    List<QuizDto> completeQuiz(List<QuizRequest> requestList) throws JsonProcessingException;
}
