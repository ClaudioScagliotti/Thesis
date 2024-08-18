package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.GoalTypeEntity;
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
class GoalTypeRepositoryTest {

    @Autowired
    GoalTypeRepository goalTypeRepository;


    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindGoalTypeEntityByType_Found() {
        GoalTypeEntity foundGoalType = goalTypeRepository.findGoalTypeEntityByType(GoalTypeEnum.DISCOVER);

        BDDAssertions.then(foundGoalType).isNotNull();
        BDDAssertions.then(foundGoalType.getType()).isEqualTo(GoalTypeEnum.DISCOVER);
    }


    @Test
    void testFindAllGoalTypes() {
        List<GoalTypeEntity> goalTypes = goalTypeRepository.findAll();

        BDDAssertions.then(goalTypes).isNotNull();
        BDDAssertions.then(goalTypes.size()).isGreaterThan(3);
        BDDAssertions.then(goalTypes.stream().map(GoalTypeEntity::getType))
                .containsExactlyInAnyOrder(GoalTypeEnum.DISCOVER,GoalTypeEnum.MOST_POPULAR,GoalTypeEnum.TOP_RATED,GoalTypeEnum.NOW_PLAYING);
    }
}
