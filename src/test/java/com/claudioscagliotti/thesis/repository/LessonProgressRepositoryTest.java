package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.enumeration.LessonStatusEnum;
import com.claudioscagliotti.thesis.enumeration.QuizResultEnum;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.model.*;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@ActiveProfiles("test")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class LessonProgressRepositoryTest {

    @Autowired
    private LessonProgressRepository lessonProgressRepository;
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    GoalTypeRepository goalTypeRepository;
    @Autowired
    CountryOfProductionRepository countryOfProductionRepository;

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    GoalRepository goalRepository;
    private GoalTypeEntity goalTypeEntityByType;
    private LessonEntity lesson1;
    private LessonEntity lesson2;
    private GoalEntity goal1;
    private UserEntity user1;
    private CourseEntity course1;
    private LessonProgressEntity lessonProgress1;
    private LessonProgressEntity lessonProgress2;


    @BeforeEach
    void setUp() {
        goalTypeEntityByType = goalTypeRepository.findGoalTypeEntityByType(GoalTypeEnum.DISCOVER);

        course1 = new CourseEntity();
        course1.setTitle("Introduction to film directing");
        course1.setGoalTypeEntityList(List.of(goalTypeEntityByType));
        courseRepository.save(course1);
        Optional<GenreEntity> action = genreRepository.getGenreEntityByName("Action");
        Optional<GenreEntity> drama = genreRepository.getGenreEntityByName("Drama");
        goalTypeEntityByType = goalTypeRepository.findGoalTypeEntityByType(GoalTypeEnum.DISCOVER);
        CountryOfProductionEntity us = countryOfProductionRepository.getCountryOfProductionByCountryCode("US");
        CountryOfProductionEntity fr = countryOfProductionRepository.getCountryOfProductionByCountryCode("FR");

        goal1 = new GoalEntity();
        goal1.setTimeToDedicate(2.5f);
        goal1.setMinYear(2000);
        goal1.setMaxYear(2020);
        goal1.setGoalType(goalTypeEntityByType);
        goal1.setGenreEntityList(List.of(action.get(), drama.get()));
        goal1.setCountryOfProductionEntityList(List.of(us, fr));
        goalRepository.save(goal1);


        user1 = new UserEntity();
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");
        user1.setGoalEntity(goal1);
        user1.setRole(RoleEnum.USER);
        user1.setPassword("Pass");
        user1.setFirstName("a");
        user1.setLastName("a");
        user1.setCourseEntityList(List.of(course1));
        userRepository.save(user1);

        lesson1 = new LessonEntity();
        lesson1.setTitle("Lesson 1");
        lesson1.setTotalCards(10);
        lesson1.setCourseEntity(course1);
        lesson1.setProgressiveId(1L);
        lessonRepository.save(lesson1);

        lesson2 = new LessonEntity();
        lesson2.setTitle("Lesson 2");
        lesson2.setTotalCards(4);
        lesson2.setCourseEntity(course1);
        lesson1.setProgressiveId(2L);
        lessonRepository.save(lesson2);

        // Creazione del progresso delle lezioni
        lessonProgress1 = new LessonProgressEntity(lesson1, user1, 0.5f, 5, QuizResultEnum.UNCOMPLETED, LessonStatusEnum.UNCOMPLETED);
        lessonProgressRepository.save(lessonProgress1);

        lessonProgress2 = new LessonProgressEntity(lesson2, user1, 1.0f, 4, QuizResultEnum.SUCCEEDED, LessonStatusEnum.COMPLETED);
        lessonProgressRepository.save(lessonProgress2);
    }

    @AfterEach
    void tearDown() {
        lessonProgressRepository.deleteAll();
        lessonRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testFindAll() {
        List<LessonProgressEntity> lessonProgressList = lessonProgressRepository.findAll();
        BDDAssertions.then(lessonProgressList).isNotNull();
        BDDAssertions.then(lessonProgressList.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetLessonProgressByUserIdAndLessonId() {
        var lessonProgress = lessonProgressRepository.getLessonProgressByUserIdAndLessonId(user1.getId(), lesson1.getId());
        BDDAssertions.then(lessonProgress).isPresent();
        BDDAssertions.then(lessonProgress.get().getLessonEntity().getTitle()).isEqualTo("Lesson 1");
    }

    @Test
    void testGetNextUncompletedLessonProgressByCourseId() {
        Optional<LessonProgressEntity> nextLessonProgress = lessonProgressRepository.getNextUncompletedLessonProgressByCourseId(user1.getCourseEntityList().get(0).getId(), user1.getId());
        BDDAssertions.then(nextLessonProgress).isPresent();
        BDDAssertions.then(nextLessonProgress.get().getStatus()).isEqualTo(LessonStatusEnum.UNCOMPLETED);
    }

    @Test
    void testFindLessonProgressByCourseIdAndUserId() {
        List<LessonProgressEntity> lessonProgresses = lessonProgressRepository.findLessonProgressByCourseIdAndUserId(user1.getCourseEntityList().get(0).getId(), user1.getId());
        BDDAssertions.then(lessonProgresses).isNotNull();
        BDDAssertions.then(lessonProgresses.size()).isGreaterThanOrEqualTo(1);
    }
}
