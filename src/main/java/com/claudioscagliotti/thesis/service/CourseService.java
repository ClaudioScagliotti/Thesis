package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.CourseDto;
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
        userService.updateUserCourses(userEntity.getId(), userEntity.getCourseEntityList());
        return courseMapper.toCourseDto(courseEntity);
    }
    private CourseEntity findCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course with ID " + courseId + " not found"));
    }
    private void addCourseToUser(UserEntity userEntity, CourseEntity courseEntity) {

        if (!userEntity.getCourseEntityList().contains(courseEntity)) {
            userEntity.getCourseEntityList().add(courseEntity);
        }
    }

    public CourseDto unsubscribeCourse(String username, Long courseId){ //TODO
        //recupero l'utente
        // controllo che il corso esista e che l'utente sia iscritto
        // userEntity -> CourseEntity
        // se non è iscritto mando un errore non bloccante, se è iscritto disiscrivo (tolgo una riga dalla tabella app_user_course)
        return new CourseDto();
    }

}
