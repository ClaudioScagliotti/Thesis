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
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class LessonProgressService {

    private final LessonProgressRepository lessonProgressRepository;
    private final LessonRepository lessonRepository;

    private final UserService userService;

    private final LessonProgressMapper lessonProgressMapper;

    public LessonProgressService(LessonProgressRepository lessonProgressRepository, LessonRepository lessonRepository, UserService userService, LessonProgressMapper lessonProgressMapper) {
        this.lessonProgressRepository = lessonProgressRepository;
        this.lessonRepository = lessonRepository;
        this.userService = userService;
        this.lessonProgressMapper = lessonProgressMapper;
    }

    public LessonProgressDto getLessonProgress(String username, Long lessonId) {
        UserEntity userEntity = userService.findByUsername(username);
        Optional<LessonEntity> lessonEntity = lessonRepository.findById(lessonId);
        if (lessonEntity.isPresent()) {
            Optional<LessonProgressEntity> lessonProgress = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(userEntity.getId(), lessonId);
            if (lessonProgress.isPresent()) {
                return lessonProgressMapper.tolessonProgressDto(lessonProgress.get(), lessonId, username);
            } else
                throw new EntityNotFoundException("There is no lesson progress associated to the lessonId: " + lessonId);
        } else throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
    }

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
                    throw new IndexOutOfBoundsException("The new progress exceeded the total cards. Total cards: "+lessonEntity.getTotalCards()+", updated cards: "+updatedCards);
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
                throw new EntityNotFoundException("There is no lesson progress associated to the lessonId: " + lessonId);
            }
        } else {
            throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
        }
    }

    public void completeLesson(UserEntity userEntity, LessonEntity lessonEntity, LessonProgressEntity lessonProgressEntity) { //TODO possibile prossima feature, restituire i punti guadagnati anziche void
        int points = 0;
        if (Objects.equals(
                lessonEntity.getTotalCards(),
                lessonProgressEntity.getCompletedCards())) {
            lessonProgressEntity.setStatus(LessonStatusEnum.COMPLETED);
            points = 10;
            if (lessonProgressEntity.getQuizResult().equals(QuizResultEnum.SUCCEEDED)) {
                points += 100; //TODO RIVEDERE quantità punti
            } else {
                lessonProgressEntity.setQuizResult(QuizResultEnum.FAILED);
            }
            lessonProgressRepository.updateLessonProgressStatusAndQuizResult(lessonEntity.getId(), lessonProgressEntity.getStatus(), lessonProgressEntity.getQuizResult());
        }
        userService.addPoints(userEntity, points);

    }

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
        } else throw new EntityNotFoundException("There is no lesson with id: " + lessonId);
    }

    private static float calculateProgress(Integer newCompletedCard, Integer totalCards) {
        return (newCompletedCard / (float) totalCards) * 100;
    }
}
