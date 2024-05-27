package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.AdviceEntity;
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
class AdviceRepositoryTest {

    @Autowired
    private AdviceRepository adviceRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testGetAllAdvices() {
        List<AdviceEntity> adviceEntities = adviceRepository.findAll();
        assertThat(adviceEntities).hasSize(4);
    }
}