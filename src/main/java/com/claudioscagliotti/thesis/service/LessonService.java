package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.exception.CompletedCourseException;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.mapper.LessonMapper;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.LessonProgressRepository;
import com.claudioscagliotti.thesis.repository.LessonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Service class for handling lesson-related operations.
 */
@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final LessonMapper lessonMapper;
    private final LessonProgressService lessonProgressService;

    /**
     * Constructs a LessonService with the specified repositories, services, and mappers.
     *
     * @param lessonRepository         The LessonRepository to interact with LessonEntity data.
     * @param lessonProgressRepository The LessonProgressRepository to interact with LessonProgressEntity data.
     * @param courseService            The CourseService to manage course-related operations.
     * @param userService              The UserService to manage user-related operations.
     * @param lessonMapper             The LessonMapper to convert between LessonEntity and LessonDto.
     * @param lessonProgressService    The LessonProgressService to manage lesson progress-related operations.
     */
    public LessonService(LessonRepository lessonRepository, LessonProgressRepository lessonProgressRepository,
                         CourseService courseService, UserService userService, LessonMapper lessonMapper,
                         LessonProgressService lessonProgressService) {
        this.lessonRepository = lessonRepository;
        this.lessonProgressRepository = lessonProgressRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.lessonMapper = lessonMapper;
        this.lessonProgressService = lessonProgressService;
    }

    /**
     * Retrieves all lessons of a course for a given user.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course.
     * @return A list of LessonDto objects representing all lessons of the course.
     * @throws SubscriptionUserException if the user is not subscribed to the course.
     */
    public List<LessonDto> getAllLessonByCourse(String username, Long courseId) {
        if (!courseService.checkSubscription(username, courseId)) {
            throw new SubscriptionUserException("The user with username " + username +
                    " is not subscribed to course with id: " + courseId);
        }
        CourseEntity courseEntity = courseService.findCourseById(courseId);
        List<LessonEntity> lessonEntityList = lessonRepository.getAllLessonByCourseEntity(courseEntity);
        return lessonMapper.toLessonDto(lessonEntityList);
    }

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
    public LessonDto getNextLessonByCourse(String username, Long courseId) {
        if (!courseService.checkSubscription(username, courseId)) {
            throw new SubscriptionUserException("The user with username " + username +
                    " is not subscribed to course with id: " + courseId);
        }
        UserEntity user = userService.findByUsername(username);
        Optional<LessonProgressEntity> lessonProgress = lessonProgressRepository.getNextUncompletedLessonProgressByCourseId(courseId, user.getId());

        if (lessonProgress.isPresent()) {
            LessonProgressEntity entity = lessonProgress.get();
            return lessonMapper.toLessonDto(entity.getLessonEntity());
        } else { // if I can't find a LessonProgressEntity I check if the course is finished
            List<Long> lessonsIdList = lessonRepository.findLessonIdsByCourseId(courseId);
            List<LessonProgressEntity> lessonProgressList = lessonProgressRepository.findLessonProgressByCourseIdAndUserId(courseId, user.getId());
            Long smallestMissingId = findSmallestMissingId(lessonsIdList, lessonProgressList);

            if (smallestMissingId != null) {  // If I find that lessons are uncompleted, I create the LessonProgressEntity for the next lesson
                lessonProgressService.createLessonProgress(user, smallestMissingId, 0);
                Optional<LessonEntity> lessonEntity = lessonRepository.findById(smallestMissingId);
                if (lessonEntity.isPresent()) {
                    return lessonMapper.toLessonDto(lessonEntity.get());
                } else {
                    throw new EntityNotFoundException("The lesson with id: " + smallestMissingId + " is not found");
                }
            }
            throw new CompletedCourseException("The course with id: " + courseId + " has all lessons completed");
        }
    }

    /**
     * Finds the smallest missing lesson ID from the provided list of lesson IDs and lesson progress entities.
     *
     * @param lessonsIdList      The list of all lesson IDs in the course.
     * @param lessonProgressList The list of lesson progress entities for the user and course.
     * @return The smallest lesson ID that is missing from the lesson progress list, or null if none are missing.
     */
    public Long findSmallestMissingId(List<Long> lessonsIdList, List<LessonProgressEntity> lessonProgressList) {
        Set<Long> lessonProgressIdSet = lessonProgressList.stream()
                .map(LessonProgressEntity::getLessonEntity)
                .map(LessonEntity::getId)
                .collect(Collectors.toSet());
        Collections.sort(lessonsIdList);
        for (Long lessonId : lessonsIdList) {
            if (!lessonProgressIdSet.contains(lessonId)) {
                return lessonId;
            }
        }
        return null;
    }

    /**
     * Retrieves a LessonEntity by its ID.
     *
     * @param lessonId The ID of the lesson to retrieve.
     * @return The LessonEntity corresponding to the given ID.
     * @throws EntityNotFoundException if no lesson exists with the given ID.
     */
    public LessonEntity getById(Long lessonId) {
        return lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("There is no Lesson with id: " + lessonId));
    }
}
