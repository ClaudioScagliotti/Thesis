package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.LessonEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("unittest")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class LessonRepositoryTest {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    private LessonEntity lesson1;
    private LessonEntity lesson2;
    private CourseEntity course;

    @BeforeEach
    void setUp() {

        course = new CourseEntity();
        course.setTitle("Test Course");
        courseRepository.save(course);

        lesson1 = new LessonEntity();
        lesson1.setCourseEntity(course);
        lesson1.setTitle("Lesson 1");
        lesson1.setContent("Content 1");
        lesson1.setTotalCards(5);
        lessonRepository.save(lesson1);

        lesson2 = new LessonEntity();
        lesson2.setCourseEntity(course);
        lesson2.setTitle("Lesson 2");
        lesson2.setContent("Content 2");
        lesson2.setTotalCards(3);
        lessonRepository.save(lesson2);
    }

    @AfterEach
    void tearDown() {
        lessonRepository.delete(lesson1);
        lessonRepository.delete(lesson2);
        courseRepository.delete(course);
    }

    @Test
    void testFindAll() {
        List<LessonEntity> lessons = lessonRepository.findAll();
        BDDAssertions.then(lessons).isNotNull();
        BDDAssertions.then(lessons.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetAllLessonByCourseEntity() {
        List<LessonEntity> lessons = lessonRepository.getAllLessonByCourseEntity(course);
        BDDAssertions.then(lessons).isNotNull();
        BDDAssertions.then(lessons.size()).isGreaterThanOrEqualTo(2);
        BDDAssertions.then(lessons.get(0).getCourseEntity()).isEqualTo(course);
    }

    @Test
    void testFindLessonIdsByCourseId() {
        List<Long> lessonIds = lessonRepository.findLessonIdsByCourseId(course.getId());
        BDDAssertions.then(lessonIds).isNotNull();
        BDDAssertions.then(lessonIds.size()).isGreaterThanOrEqualTo(2);
        BDDAssertions.then(lessonIds).contains(lesson1.getId(), lesson2.getId());
    }
}
