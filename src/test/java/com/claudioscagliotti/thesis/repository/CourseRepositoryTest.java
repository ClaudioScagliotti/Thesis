package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import jakarta.transaction.Transactional;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("unittest")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GoalTypeRepository goalTypeRepository;

    private CourseEntity course1;
    private CourseEntity course2;

    private GoalTypeEntity goalTypeEntityByType;
    @BeforeEach
    void setUp() {
        goalTypeEntityByType = goalTypeRepository.findGoalTypeEntityByType(GoalTypeEnum.DISCOVER);

        course1 = new CourseEntity();
        course1.setTitle("Introduction to film directing");
        course1.setGoalTypeEntityList(List.of(goalTypeEntityByType));
        courseRepository.save(course1);

        course2 = new CourseEntity();
        course2.setTitle("Advanced film editing");
        course2.setGoalTypeEntityList(List.of(goalTypeEntityByType));
        courseRepository.save(course2);
    }

    @AfterEach
    void tearDown() {
        courseRepository.delete(course1);
        courseRepository.delete(course2);
    }

    @Test
    void testFindCourse() {
        Optional<CourseEntity> foundCourse = courseRepository.findById(course1.getId());
        BDDAssertions.then(foundCourse).isPresent();
        foundCourse.ifPresent(course -> BDDAssertions.then(course.getTitle()).isEqualTo("Introduction to film directing"));
    }

    @Test
    void testFindAllByGoalType_WithSingleGoalType() {
        List<CourseEntity> courses = courseRepository.findAllByGoalType(goalTypeEntityByType);

        BDDAssertions.then(courses).isNotNull();
        BDDAssertions.then(courses.size()).isGreaterThan(1); // Ci sono due corsi associati a goalType1
        BDDAssertions.then(courses)
                .extracting(CourseEntity::getTitle)
                .contains("Introduction to film directing", "Advanced film editing");
    }

    @Test
    void testFindAllByGoalType_WithMultipleGoalTypes() {
        List<CourseEntity> courses = courseRepository.findAllByGoalType(goalTypeEntityByType);

        BDDAssertions.then(courses).isNotNull();
        BDDAssertions.then(courses.size()).isGreaterThan(1);


        BDDAssertions.then(courses.stream().map(CourseEntity::getTitle))
                .contains("Advanced film editing");
    }

}
