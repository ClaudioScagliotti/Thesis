package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisApplication.class)
class GoalServiceTest {
    @Autowired
    GoalService goalService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
    @Test
    void composeParamsTest() {
        GenreEntity g1 = new GenreEntity("Crime");
        GenreEntity g2 = new GenreEntity("Action");

        KeywordEntity k1 = new KeywordEntity("italy", 131);

        CountryOfProductionEntity c1 = new CountryOfProductionEntity("IT");

        List<KeywordEntity> lk = List.of(k1);
        List<GenreEntity> genreEntities = List.of(g1, g2);
        List<CountryOfProductionEntity> lc = List.of(c1);

        GoalTypeEntity goalTypeEntity= new GoalTypeEntity();
        goalTypeEntity.setId(1L);
        goalTypeEntity.setType(GoalTypeEnum.NOW_PLAYING);
        GoalEntity goalEntity = new GoalEntity();
        goalEntity.setTimeToDedicate(2F);
        goalEntity.setGoalType(goalTypeEntity);
        goalEntity.setMinYear(2000);
        goalEntity.setMaxYear(2020);
        goalEntity.setGenreEntityList(genreEntities);
        goalEntity.setKeywordEntityList(lk);
        goalEntity.setCountryOfProductionEntityList(lc);

        String s = goalService.composeParams(goalEntity);
        System.out.println(s);


    }
}