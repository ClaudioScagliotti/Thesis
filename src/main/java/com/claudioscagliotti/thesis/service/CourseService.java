package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
import com.claudioscagliotti.thesis.exception.SubscriptionUserException;
import com.claudioscagliotti.thesis.mapper.CourseMapper;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserService userService;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper, UserService userService) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.userService = userService;
    }

    public List<CourseDto> subscribedCourses(String username){
        UserEntity userEntity= userService.findByUsername(username);
        return courseMapper.toCourseDto(userEntity.getCourseEntityList());
    }

    public List<CourseDto> suggestCourses(String username){
        UserEntity userEntity= userService.findByUsername(username);

        GoalTypeEntity goalType = userEntity.getGoalEntity().getGoalType();
        List<CourseEntity> courseEntityList = courseRepository.findAllByGoalType(goalType);

        return courseMapper.toCourseDto(courseEntityList);
    }

    public CourseDto subscribeCourse(String username, Long courseId) {
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);

        addCourseToUser(userEntity, courseEntity);
        userService.saveUser(userEntity);
        return courseMapper.toCourseDto(courseEntity);
    }
    protected CourseEntity findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with id: " + courseId + " not found"));
    }
    private void addCourseToUser(UserEntity userEntity, CourseEntity courseEntity) {

        if (!userEntity.getCourseEntityList().contains(courseEntity)) {
            userEntity.getCourseEntityList().add(courseEntity);
        }
        else throw new SubscriptionUserException("The user with id: "+userEntity.getId()+" is already subscribed to course with title: " +courseEntity.getTitle());
    }

    public void unsubscribeCourse(String username, Long courseId){
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);

        if(!checkSubscription(username,courseId)){
            throw new SubscriptionUserException("The user "+username+" is not subscribed to the course with title: "+courseEntity.getTitle());
        }
        else {
            userEntity.getCourseEntityList().remove(courseEntity);
            userService.saveUser(userEntity);
        }
    }

    public boolean checkSubscription(String username, Long courseId){
        UserEntity userEntity = userService.findByUsername(username);
        CourseEntity courseEntity = findCourseById(courseId);
        return userEntity.getCourseEntityList().contains(courseEntity);
    }

}
