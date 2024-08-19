package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.*;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@ActiveProfiles("unittest")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class GoalRepositoryTest {

    @Autowired
    private GoalRepository goalRepository;
    @Autowired
    GoalTypeRepository goalTypeRepository;
    @Autowired
    CountryOfProductionRepository countryOfProductionRepository;

    @Autowired
    GenreRepository genreRepository;

    private GoalEntity goal1;
    private GoalTypeEntity goalTypeEntityByType;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown() {
        goalRepository.delete(goal1);
    }

    @Test
    void shouldFindAllGoals() {
        List<GoalEntity> list = goalRepository.findAll();

        BDDAssertions.then(list).isNotNull();
        BDDAssertions.then(list.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    void shouldDeleteGoalById() {
        GoalEntity goalToDelete = goalRepository.findAll().get(0);
        int size = goalRepository.findAll().size();
        goalRepository.deleteById(goalToDelete.getId());

        List<GoalEntity> remainingGoals = goalRepository.findAll();
        BDDAssertions.then(remainingGoals.size()).isEqualTo(size-1);
        BDDAssertions.then(remainingGoals.contains(goalToDelete)).isFalse();
    }

    @Test
    void shouldUpdateGoal() {
        GoalEntity goalToUpdate = goalRepository.findAll().get(0);
        goalToUpdate.setMinYear(1990);

        GoalEntity updatedGoal = goalRepository.save(goalToUpdate);

        BDDAssertions.then(updatedGoal.getMinYear()).isEqualTo(1990);
    }

    @Test
    void shouldFindGoalById() {
        GoalEntity goal = goalRepository.findAll().get(0);
        Optional<GoalEntity> foundGoal = goalRepository.findById(goal.getId());

        BDDAssertions.then(foundGoal).isPresent();
        BDDAssertions.then(foundGoal.get().getId()).isEqualTo(goal.getId());
    }
}
