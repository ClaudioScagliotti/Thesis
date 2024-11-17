package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.mapper.CourseMapper;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.CourseRepository;
import com.claudioscagliotti.thesis.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class responsible for managing courses and subscriptions for users.
 */
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserServiceImpl userService;

    /**
     * Constructor to initialize CourseService with repositories and mappers.
     *
     * @param courseRepository The repository for CourseEntity.
     * @param courseMapper     The mapper for converting CourseEntity to CourseDto.
     * @param userService      The service for managing user operations.
     */
    public CourseServiceImpl(CourseRepository courseRepository, CourseMapper courseMapper, UserServiceImpl userService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userService = userService;
    }

    /**
     * Retrieves the list of courses that the user is subscribed to.
     *
     * @param username The username of the user.
     * @return List of CourseDto representing subscribed courses.
     */
    public List<CourseDto> subscribedCourses(String username) {
        UserEntity userEntity = userService.findByUsername(username);
        return courseMapper.toCourseDto(userEntity.getCourseEntityList());
    }

    /**
     * Retrieves a list of courses suggested based on the user's goal type.
     *
     * @param username The username of the user.
     * @return List of CourseDto representing suggested courses.
     */
    public List<CourseDto> suggestCourses(String username) {
        UserEntity userEntity = userService.findByUsername(username);

        GoalTypeEntity goalType = userEntity.getGoalEntity().getGoalType();
        List<CourseEntity> courseEntityList = courseRepository.findAllByGoalType(goalType);
        if(courseEntityList.isEmpty()){
            throw new EntityNotFoundException("There are not courses avaliable for this goal type!");
        }

        return courseMapper.toCourseDto(courseEntityList);
    }

    /**
     * Subscribes the user to a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to subscribe.
     * @return CourseDto representing the subscribed course.
     * @throws EntityNotFoundException   If the course with the given courseId is not found.
     * @throws SubscriptionUserException If the user is already subscribed to the course.
     */
    public CourseDto subscribeCourse(String username, Long courseId) {
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);

        addCourseToUser(userEntity, courseEntity);
        userService.saveUser(userEntity);
        return courseMapper.toCourseDto(courseEntity);
    }

    /**
     * Finds a course by its ID.
     *
     * @param courseId The ID of the course to find.
     * @return CourseEntity representing the course found.
     * @throws EntityNotFoundException If the course with the given courseId is not found.
     */
    protected CourseEntity findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with id: " + courseId + " not found"));
    }

    /**
     * Adds a course to the user's subscribed courses.
     *
     * @param userEntity   The UserEntity to add the course to.
     * @param courseEntity The CourseEntity to add.
     * @throws SubscriptionUserException If the user is already subscribed to the course.
     */
    private void addCourseToUser(UserEntity userEntity, CourseEntity courseEntity) {
        if (!userEntity.getCourseEntityList().contains(courseEntity)) {
            userEntity.getCourseEntityList().add(courseEntity);
        } else {
            throw new SubscriptionUserException("The user with username: " + userEntity.getUsername() + " is already subscribed to course with title: " + courseEntity.getTitle());
        }
    }

    /**
     * Unsubscribes the user from a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to unsubscribe.
     * @throws SubscriptionUserException If the user is not subscribed to the course.
     */
    public void unsubscribeCourse(String username, Long courseId) {
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);

        if (!checkSubscription(username, courseId)) {
            throw new SubscriptionUserException("The user " + username + " is not subscribed to the course with title: " + courseEntity.getTitle());
        } else {
            userEntity.getCourseEntityList().remove(courseEntity);
            userService.saveUser(userEntity);
        }
    }

    /**
     * Checks if the user is subscribed to a course identified by courseId.
     *
     * @param username The username of the user.
     * @param courseId The ID of the course to check subscription for.
     * @return true if the user is subscribed to the course, false otherwise.
     */
    public boolean checkSubscription(String username, Long courseId) {
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);
        return userEntity.getCourseEntityList().contains(courseEntity);
    }
}
