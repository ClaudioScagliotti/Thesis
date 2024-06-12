package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisApplication.class)
class GoalTypeRepositoryTest {

    @Autowired
    GoalTypeRepository goalTypeRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    void getGoalTypeByGoalId() {
//        Optional<GoalTypeEntity> goalTypeByGoalId = goalTypeRepository.getGoalTypeByGoalId(4L);
//        goalTypeByGoalId.ifPresent(goalTypeEntity -> assertThat(goalTypeEntity.getType().name()).isEqualTo(GoalTypeEnum.DISCOVER.name()));
//    }
}