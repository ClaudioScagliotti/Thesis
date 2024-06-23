package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisApplication.class)
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testFindAll() {
        List<GoalEntity> list = goalRepository.findAll();
        assertThat(list.size()).isEqualTo(2L);
    }

    @Test
    void getGoalTypeByGoalId() {
        GoalTypeEntity goalTypeByGoalId = goalRepository.getGoalTypeById(1L);
        assertThat(goalTypeByGoalId).isNotNull();
    }
    @Test
    void getTGoalType() {//TODO
        List<GoalTypeEnum> enums= goalRepository.findGoalTypesById(1L);

        assertThat(enums).isNotNull();

    }
    @Test
    void getTGoalTypeEntity() {
        List<GoalTypeEntity> entity = goalRepository.findGoalTypesById2(1L);
        assertThat(entity).isNotNull();

    }

}