package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.LessonProgressDto;
import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.mapper.LessonProgressMapper;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.LessonProgressRepository;
import com.claudioscagliotti.thesis.repository.LessonRepository;
import com.claudioscagliotti.thesis.utility.PercentageCalculatorUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Service class for managing lesson progress-related operations.
 */
@Service
public class LessonProgressService {

    @Value("${success.percentage}")
    private double successPercentage;

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;
    private final UserService userService;
    private final LessonProgressMapper lessonProgressMapper;

    /**
     * Constructs a LessonProgressService with the specified repositories, services, and mapper.
     *
     * @param lessonProgressRepository The LessonProgressRepository to interact with LessonProgressEntity data.
     * @param lessonRepository         The LessonRepository to interact with LessonEntity data.
     * @param userService              The UserService to manage user-related operations.
     * @param lessonProgressMapper     The LessonProgressMapper to convert between LessonProgressEntity and LessonProgressDto.
     */
    public LessonProgressService(LessonProgressRepository lessonProgressRepository, LessonRepository lessonRepository,
                                 UserService userService, LessonProgressMapper lessonProgressMapper) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.lessonProgressMapper = lessonProgressMapper;
    }

    /**
     * Retrieves the lesson progress for a specific lesson and user.
     *
     * @param username The username of the user.
     * @param lessonId The ID of the lesson.
     * @return The LessonProgressDto representing the progress of the lesson for the user.
     * @throws EntityNotFoundException if no lesson progress is associated with the given lesson ID.
     */
    public LessonProgressDto getLessonProgress(String username, Long lessonId) {
        UserEntity userEntity = userService.findByUsername(username);
        Optional<LessonEntity> lessonEntity = lessonRepository.findById(lessonId);
        if (lessonEntity.isPresent()) {
            Optional<LessonProgressEntity> lessonProgress = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(userEntity.getId(), lessonId);
            if (lessonProgress.isPresent()) {
                return lessonProgressMapper.tolessonProgressDto(lessonProgress.get(), lessonId, username);
            } else {
                throw new EntityNotFoundException("There is no lesson progress associated with lessonId: " + lessonId);
            }
        } else {
            throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
        }
    }

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
    public LessonProgressDto updateLessonProgress(String username, Long lessonId, Integer newCompletedCards) {
        UserEntity userEntity = userService.findByUsername(username);
        Optional<LessonEntity> lesson = lessonRepository.findById(lessonId);

        if (lesson.isPresent()) {
            LessonEntity lessonEntity = lesson.get();
            Optional<LessonProgressEntity> lessonProgressEntity = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(userEntity.getId(), lessonId);

            if (lessonProgressEntity.isPresent()) {
                LessonProgressEntity lessonProgress = lessonProgressEntity.get();

                Integer updatedCards = lessonProgress.getCompletedCards() + newCompletedCards;
                if (updatedCards > lessonEntity.getTotalCards()) {
                    throw new IndexOutOfBoundsException("The new progress exceeds the total cards. Total cards: " + lessonEntity.getTotalCards() + ", updated cards: " + updatedCards);
                } else {
                    float progress = calculateProgress(updatedCards, lessonEntity.getTotalCards());

                    lessonProgress.setCompletedCards(updatedCards);
                    lessonProgress.setProgress(progress);

                    if (updatedCards.equals(lessonEntity.getTotalCards())) {
                        completeLesson(userEntity, lessonEntity, lessonProgress);
                    }

                    lessonProgressRepository.save(lessonProgress);
                }
            } else {
                createLessonProgress(userEntity, lessonId, newCompletedCards);
            }

            Optional<LessonProgressEntity> updatedLessonProgress = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(userEntity.getId(), lessonId);
            if (updatedLessonProgress.isPresent()) {
                return lessonProgressMapper.tolessonProgressDto(updatedLessonProgress.get(), lessonId, username);
            } else {
                throw new EntityNotFoundException("There is no lesson progress associated with lessonId: " + lessonId);
            }
        } else {
            throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
        }
    }

    /**
     * Completes the lesson progress for a user upon finishing all lesson requirements.
     *
     * @param userEntity           The UserEntity representing the user.
     * @param lessonEntity         The LessonEntity representing the lesson.
     * @param lessonProgressEntity The LessonProgressEntity representing the lesson progress.
     */
    public void completeLesson(UserEntity userEntity, LessonEntity lessonEntity, LessonProgressEntity lessonProgressEntity) {
        int points = 0;
        if (Objects.equals(lessonEntity.getTotalCards(), lessonProgressEntity.getCompletedCards())) {
            lessonProgressEntity.setStatus(LessonStatusEnum.COMPLETED);
            points = 10;

            double quizCorrectPercentage = PercentageCalculatorUtil.calculateSucceededPercentage(lessonEntity.getQuizzes());
            if (quizCorrectPercentage > successPercentage) {
                lessonProgressEntity.setQuizResult(QuizResultEnum.SUCCEEDED);
                points += 100; //TODO Review point assegnation
            } else {
                lessonProgressEntity.setQuizResult(QuizResultEnum.FAILED);
            }
            lessonProgressRepository.updateLessonProgressStatusAndQuizResult(lessonEntity.getId(), lessonProgressEntity.getStatus(), lessonProgressEntity.getQuizResult());
        }
        userService.addPoints(userEntity, points);
    }

    /**
     * Creates a new lesson progress for a user for the specified lesson.
     *
     * @param userEntity       The UserEntity representing the user.
     * @param lessonId         The ID of the lesson.
     * @param newCompletedCard The number of completed cards for the new lesson progress.
     * @throws EntityNotFoundException if no lesson is found with the given lesson ID.
     * @throws IllegalStateException   if a lesson progress already exists for the user and lesson.
     */
    protected void createLessonProgress(UserEntity userEntity, Long lessonId, Integer newCompletedCard) {
        Optional<LessonEntity> lessonEntity = lessonRepository.findById(lessonId);

        if (lessonEntity.isPresent()) {
            Optional<LessonProgressEntity> existingLessonProgress = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(userEntity.getId(), lessonEntity.get().getId());
            if (existingLessonProgress.isEmpty()) {
                float progress = calculateProgress(newCompletedCard, lessonEntity.get().getTotalCards());
                LessonProgressEntity lessonProgress = new LessonProgressEntity(
                        lessonEntity.get(),
                        userEntity,
                        progress,
                        newCompletedCard,
                        QuizResultEnum.UNCOMPLETED,
                        LessonStatusEnum.UNCOMPLETED
                );
                lessonProgressRepository.save(lessonProgress);
            } else {
                throw new IllegalStateException("A lesson progress already exists for user " + userEntity.getUsername() + " and lesson " + lessonEntity.get().getId());
            }
        } else {
            throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
        }
    }

    /**
     * Calculates the progress percentage based on the number of completed cards and total cards.
     *
     * @param newCompletedCard The number of newly completed cards.
     * @param totalCards       The total number of cards in the lesson.
     * @return The progress percentage as a float value.
     */
    private static float calculateProgress(Integer newCompletedCard, Integer totalCards) {
        return (newCompletedCard / (float) totalCards) * 100;
    }
}
