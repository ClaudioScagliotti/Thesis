package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.dto.request.QuizRequest;
import com.claudioscagliotti.thesis.dto.response.QuizDto;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.QuizTypeEnum;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.mapper.QuizMapper;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.QuizEntity;
import com.claudioscagliotti.thesis.repository.QuizRepository;
import com.claudioscagliotti.thesis.service.QuizService;
import com.claudioscagliotti.thesis.utility.JsonComparator;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Service class for handling quiz-related operations.
 */
@Service
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final QuizMapper quizMapper;
    private final AdviceServiceImpl adviceService;
    private final LessonServiceImpl lessonService;
    private final CourseServiceImpl courseService;

    /**
     * Constructs a QuizService with the specified dependencies.
     *
     * @param quizRepository The QuizRepository to be used.
     * @param quizMapper     The QuizMapper to be used.
     * @param adviceService  The AdviceService to be used.
     * @param lessonService  The LessonService to be used.
     * @param courseService  The CourseService to be used.
     */
    public QuizServiceImpl(QuizRepository quizRepository, QuizMapper quizMapper, AdviceServiceImpl adviceService, LessonServiceImpl lessonService, CourseServiceImpl courseService) {
        this.quizRepository = quizRepository;
        this.quizMapper = quizMapper;
        this.adviceService = adviceService;
        this.lessonService = lessonService;
        this.courseService = courseService;
    }

    /**
     * Finds all quizzes by advice ID and checks if the user is authorized.
     *
     * @param adviceId The ID of the advice.
     * @param username The username of the user.
     * @return A list of QuizDto objects.
     * @throws UnauthorizedUserException if the user does not own the advice.
     */
    public List<QuizDto> findAllByAdviceId(Long adviceId, String username) {
        AdviceEntity adviceEntity = adviceService.getById(adviceId);
        if (!adviceEntity.getUserEntity().getUsername().equals(username)) {
            throw new UnauthorizedUserException("The user with username: " + username + " does not have the advice with the id: " + adviceId);
        }
        List<QuizEntity> quizEntities = quizRepository.findAllByAdviceId(adviceId);
        return quizMapper.toQuizDto(quizEntities);
    }

    /**
     * Finds all quizzes by lesson ID and checks if the user is subscribed to the course.
     *
     * @param lessonId The ID of the lesson.
     * @param username The username of the user.
     * @return A list of QuizDto objects.
     * @throws SubscriptionUserException if the user is not subscribed to the course.
     */
    public List<QuizDto> findAllByLessonId(Long lessonId, String username) {
        LessonEntity lessonEntity = lessonService.getById(lessonId);
        if (!courseService.isNotSubscribed(username, lessonEntity.getCourseEntity().getId())) {
            throw new SubscriptionUserException("The user " + username + " is not subscribed to the course with title: " + lessonEntity.getCourseEntity().getTitle());
        } else {
            List<QuizEntity> quizEntities = quizRepository.findAllByLessonId(lessonId);
            return quizMapper.toQuizDto(quizEntities);
        }
    }

    /**
     * Completes the quizzes based on the provided requests.
     *
     * @param requestList The list of quiz requests.
     * @return A list of QuizDto objects.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    public List<QuizDto> completeQuiz(List<QuizRequest> requestList) throws JsonProcessingException {
        List<QuizEntity> entities = findQuizById(requestList);
        return checkQuizSolutions(requestList, entities);
    }

    /**
     * Finds quizzes by their IDs.
     *
     * @param requestList The list of quiz requests.
     * @return A list of QuizEntity objects.
     */
    private List<QuizEntity> findQuizById(List<QuizRequest> requestList) {
        List<QuizEntity> quizEntities = new ArrayList<>();
        for (QuizRequest request : requestList) {
            Optional<QuizEntity> entity = quizRepository.findById(request.getQuizId());
            entity.ifPresent(quizEntities::add);
        }
        return quizEntities;
    }

    /**
     * Checks the quiz solutions and updates their status.
     *
     * @param requestList   The list of quiz requests.
     * @param solutionsList The list of quiz solutions.
     * @return A list of QuizDto objects.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    private List<QuizDto> checkQuizSolutions(List<QuizRequest> requestList, List<QuizEntity> solutionsList) throws JsonProcessingException {
        List<QuizEntity> resultList = new ArrayList<>();

        for (QuizRequest request : requestList) {
            QuizEntity solutionEntity = solutionsList.stream()
                    .filter(q -> q.getId().equals(request.getQuizId()))
                    .findFirst()
                    .orElse(null);

            if (solutionEntity == null) {
                throw new EntityNotFoundException("Not found solution for the quiz request with id: " + request.getQuizId());
            }
            boolean isCorrect = false;

            if (solutionEntity.getType().equals(QuizTypeEnum.CHRONOLOGICAL_ORDER)) {
                isCorrect = checkChronologicalOrderAnswer(solutionEntity, request.getCorrectOrder());
            } else if (solutionEntity.getType().equals(QuizTypeEnum.MULTIPLE_CHOICE)) {
                isCorrect = checkMultipleChoiceAnswer(solutionEntity, request.getCorrectOption());
            } else if (solutionEntity.getType().equals(QuizTypeEnum.TRUE_FALSE)) {
                isCorrect = checkTrueFalseAnswer(solutionEntity, request.getCorrectAnswer());
            }

            if (isCorrect) {
                updateQuizStatus(solutionEntity.getId(), QuizResultEnum.SUCCEEDED);
            } else {
                updateQuizStatus(solutionEntity.getId(), QuizResultEnum.FAILED);
            }

            Optional<QuizEntity> updatedQuizEntity = quizRepository.findById(solutionEntity.getId());
            updatedQuizEntity.ifPresent(resultList::add);
        }

        return quizMapper.toQuizDto(resultList);
    }

    /**
     * Updates the status of a quiz.
     *
     * @param quizId     The ID of the quiz.
     * @param resultEnum The result to be set.
     */
    private void updateQuizStatus(Long quizId, QuizResultEnum resultEnum) {
        quizRepository.updateStatusById(quizId, resultEnum);
    }

    /**
     * Checks the true/false answer for the quiz.
     *
     * @param quiz       The quiz entity.
     * @param userAnswer The user's answer.
     * @return true if the answer is correct, false otherwise.
     */
    private boolean checkTrueFalseAnswer(QuizEntity quiz, Boolean userAnswer) {
        return userAnswer == quiz.getCorrectAnswer();
    }

    /**
     * Checks the multiple-choice answer for the quiz.
     *
     * @param quiz       The quiz entity.
     * @param userAnswer The user's answer.
     * @return true if the answer is correct, false otherwise.
     */
    private boolean checkMultipleChoiceAnswer(QuizEntity quiz, Integer userAnswer) {
        return Objects.equals(quiz.getCorrectOption(), userAnswer);
    }

    /**
     * Checks the chronological order answer for the quiz.
     *
     * @param quiz       The quiz entity.
     * @param userAnswer The user's answer in JSON format.
     * @return true if the answer is correct, false otherwise.
     * @throws JsonProcessingException if an error occurs during JSON processing.
     */
    private boolean checkChronologicalOrderAnswer(QuizEntity quiz, String userAnswer) throws JsonProcessingException {
        return JsonComparator.compareJson(userAnswer, quiz.getCorrectOrder());
    }
}
