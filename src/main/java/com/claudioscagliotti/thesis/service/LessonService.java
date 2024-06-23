package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.response.LessonDto;
import com.claudioscagliotti.thesis.exception.UnsubscribedUserException;
import com.claudioscagliotti.thesis.mapper.LessonMapper;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import com.claudioscagliotti.thesis.model.LessonProgressEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.LessonProgressRepository;
import com.claudioscagliotti.thesis.repository.LessonRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final LessonProgressRepository lessonProgressRepository;
    private final CourseService courseService;
    private final UserService userService;
    private final LessonMapper lessonMapper;
    private final LessonProgressService lessonProgressService;

    public LessonService(LessonRepository lessonRepository, LessonProgressRepository lessonProgressRepository, CourseService courseService, UserService userService, LessonMapper lessonMapper, LessonProgressService lessonProgressService) {
        this.lessonRepository = lessonRepository;
        this.lessonProgressRepository = lessonProgressRepository;
        this.courseService = courseService;
        this.userService = userService;
        this.lessonMapper = lessonMapper;
        this.lessonProgressService = lessonProgressService;
    }

    public List<LessonDto> getAllLessonByCourse(String username, Long courseId){
        if(!courseService.checkSubscription(username, courseId)){
            throw new UnsubscribedUserException("The user with username "+username+" is not subscribed to course with id: "+ courseId);
        }
        CourseEntity courseEntity = courseService.findCourseById(courseId);
        List<LessonEntity> lessonEntityList = lessonRepository.getAllLessonByCourseEntity(courseEntity);
        return lessonMapper.toLessonDto(lessonEntityList);
    }
    public LessonDto getNextLessonByCourse(String username, Long courseId){
        if(!courseService.checkSubscription(username, courseId)){
            throw new UnsubscribedUserException("The user with username "+username+" is not subscribed to course with id: "+ courseId);
        }
        UserEntity user = userService.findByUsername(username);
        Optional<LessonProgressEntity> lessonProgress = lessonProgressRepository.getNextUncompletedLessonProgressByCourseId(courseId, user.getId());

        if(lessonProgress.isPresent()){
            LessonProgressEntity entity= lessonProgress.get();
            return lessonMapper.toLessonDto(entity.getLessonEntity());
        }
        else{ // se non trovo un lesson Progress guardo se il corso è finito o no
            List<Long> lessonsIdList = lessonRepository.findLessonIdsByCourseId(courseId);
            List<Long> lessonProgressIdList = lessonProgressRepository.findLessonProgressIdsByCourseIdAndUserId(courseId, user.getId());
            Long smallestMissingId = findSmallestMissingId(lessonsIdList, lessonProgressIdList);
            if(smallestMissingId!=null){ //se ho trovato che mancano delle lezioni creo il progress per la successiva lezione
                lessonProgressService.createLessonProgress(user, smallestMissingId, 0);
                Optional<LessonEntity> lessonEntity = lessonRepository.findById(smallestMissingId);
                if(lessonEntity.isPresent()){
                    return lessonMapper.toLessonDto(lessonEntity.get());
                }
            }
            return null;
            //se invece sono tutte completate ritorno null con messaggio, non eccezione.
        }
    }

    public Long findSmallestMissingId(List<Long> lessonsIdList, List<Long> lessonProgressIdList) {
        Set<Long> lessonProgressIdSet = new HashSet<>(lessonProgressIdList);
        Collections.sort(lessonsIdList);
        for (Long lessonId : lessonsIdList) {
            if (!lessonProgressIdSet.contains(lessonId)) {
                return lessonId;
            }
        }
        return null;
    }





}
