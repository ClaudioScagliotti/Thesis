package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.*;
import com.claudioscagliotti.thesis.repository.CountryOfProductionRepository;
import com.claudioscagliotti.thesis.repository.GenreRepository;
import com.claudioscagliotti.thesis.repository.GoalRepository;
import com.claudioscagliotti.thesis.repository.GoalTypeRepository;
import com.claudioscagliotti.thesis.service.impl.GoalServiceImpl;
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
class GoalServiceTest {
    @Autowired
    GoalServiceImpl goalService;
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
    }
    @Test
    void composeParamsTest() {
        String s = goalService.composeParams(goal1);
        System.out.println(s);


    }
}