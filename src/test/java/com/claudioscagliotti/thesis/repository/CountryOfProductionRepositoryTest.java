package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.CountryOfProductionEntity;
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
class CountryOfProductionRepositoryTest {

    CountryOfProductionEntity italy;
    CountryOfProductionEntity france;
    @Autowired
    private CountryOfProductionRepository repository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testFindAll() {
        List<CountryOfProductionEntity> list = repository.findAll();
        BDDAssertions.then(list).isNotNull();
        BDDAssertions.then(list.size()).isGreaterThan(1);
    }

    @Test
    public void testGetCountryOfProductionByCountryCode_Found() {
        CountryOfProductionEntity entity = repository.getCountryOfProductionByCountryCode("IT");
        BDDAssertions.then(entity).isNotNull();
        BDDAssertions.then(entity.getCountryCode()).isEqualTo("IT");
        BDDAssertions.then(entity.getName()).isEqualTo("Italy");
    }

    @Test
    public void testGetCountryOfProductionByCountryCode_NotFound() {
        CountryOfProductionEntity entity = repository.getCountryOfProductionByCountryCode("AAA");
        BDDAssertions.then(entity).isNull();
    }
}
