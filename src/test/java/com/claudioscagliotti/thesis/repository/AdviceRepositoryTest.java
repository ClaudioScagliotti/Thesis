package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.AdviceStatusEnum;
import com.claudioscagliotti.thesis.model.AdviceEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("unittest")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class AdviceRepositoryTest {

    @Autowired
    private AdviceRepository adviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;

    private AdviceEntity completedAdvice;
    private AdviceEntity uncompletedAdvice1;
    private AdviceEntity uncompletedAdvice2;

    @BeforeEach
    void setUp() {
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("aaa");
        user.setFirstName("Nome");
        user.setLastName("Cognome");
        user.setEmail("@");
        userRepository.save(user);

        completedAdvice = new AdviceEntity();
        completedAdvice.setUserEntity(user);
        completedAdvice.setStatus(AdviceStatusEnum.COMPLETED);
        completedAdvice.setMovie(movieRepository.findAll().get(0));
        adviceRepository.save(completedAdvice);

        uncompletedAdvice1 = new AdviceEntity();
        uncompletedAdvice1.setUserEntity(user);
        uncompletedAdvice1.setStatus(AdviceStatusEnum.UNCOMPLETED);
        uncompletedAdvice1.setMovie(movieRepository.findAll().get(0));
        adviceRepository.save(uncompletedAdvice1);

        uncompletedAdvice2 = new AdviceEntity();
        uncompletedAdvice2.setUserEntity(user);
        uncompletedAdvice2.setStatus(AdviceStatusEnum.UNCOMPLETED);
        uncompletedAdvice2.setMovie(movieRepository.findAll().get(0));
        adviceRepository.save(uncompletedAdvice2);

        adviceRepository.flush();
        userRepository.flush();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteByUsername("testuser");
        adviceRepository.delete(completedAdvice);
        adviceRepository.delete(uncompletedAdvice1);
        adviceRepository.delete(uncompletedAdvice2);

    }

    @Test
    void testFindUncompletedAdviceByUsername_WithExistingUser() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AdviceEntity> uncompletedAdvices = adviceRepository.findUncompletedAdviceByUsername("testuser", pageable);

        BDDAssertions.then(uncompletedAdvices)
                .isNotNull()
                .hasSize(2)
                .extracting(AdviceEntity::getStatus)
                .containsOnly(AdviceStatusEnum.UNCOMPLETED);

    }

    @Test
    void testFindUncompletedAdviceByUsername_WithNoUncompletedAdvices() {
        Pageable pageable = PageRequest.of(0, 10);
        List<AdviceEntity> uncompletedAdvices = adviceRepository.findUncompletedAdviceByUsername("anotheruser", pageable);

        BDDAssertions.then(uncompletedAdvices).isEmpty();
    }

    @Test
    void testFindUncompletedAdviceByUsername_WithPagination() {
        Pageable pageable = PageRequest.of(0, 1);
        List<AdviceEntity> uncompletedAdvices = adviceRepository.findUncompletedAdviceByUsername("testuser", pageable);

        BDDAssertions.then(uncompletedAdvices)
                .isNotNull()
                .hasSize(1);

    }
}