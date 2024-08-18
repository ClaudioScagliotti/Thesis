package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.enumeration.GoalTypeEnum;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
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
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    GoalTypeRepository goalTypeRepository;
    @Autowired
    CountryOfProductionRepository countryOfProductionRepository;

    @Autowired
    GenreRepository genreRepository;
    @Autowired
    GoalRepository goalRepository;

    private UserEntity user1;
    private UserEntity user2;
    private GoalEntity goal1;
    private GoalEntity goal2;
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

        goal2 = new GoalEntity();
        goal2.setTimeToDedicate(3.0f);
        goal2.setMinYear(1990);
        goal2.setMaxYear(2020);
        goal2.setGoalType(goalTypeEntityByType);
        goal2.setGenreEntityList(List.of(drama.get()));
        goal2.setCountryOfProductionEntityList(List.of(fr));
        goalRepository.save(goal2);

        user1 = new UserEntity();
        user1.setUsername("john_doe");
        user1.setEmail("john.doe@example.com");
        user1.setGoalEntity(goal1);
        user1.setRole(RoleEnum.USER);
        user1.setPassword("Pass");
        user1.setFirstName("a");
        user1.setLastName("a");
        userRepository.save(user1);

        user2 = new UserEntity();
        user2.setUsername("jane_doe");
        user2.setEmail("jane.doe@example.com");
        user2.setGoalEntity(goal2);
        user2.setRole(RoleEnum.ADMIN);
        user2.setPassword("Pass");
        user2.setFirstName("a");
        user2.setLastName("a");
        userRepository.save(user2);
    }


    @AfterEach
    void tearDown() {
        userRepository.delete(user2);
        userRepository.delete(user1);
        goalRepository.delete(goal1);
        goalRepository.delete(goal2);
    }

    @Test
    void testFindByUsername() {
        Optional<UserEntity> foundUser = userRepository.findByUsername("john_doe");
        BDDAssertions.then(foundUser).isPresent();
        BDDAssertions.then(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    void testFindByUsernameAndEmail() {
        Optional<UserEntity> foundUser = userRepository.findByUsernameAndEmail("jane_doe", "jane.doe@example.com");
        BDDAssertions.then(foundUser).isPresent();
        BDDAssertions.then(foundUser.get().getUsername()).isEqualTo("jane_doe");
    }

    @Test
    void testUpdateUserGoal() {
        userRepository.updateUserGoal(user1.getId(), goal1.getId());
        Optional<UserEntity> updatedUser = userRepository.findById(user1.getId());
        BDDAssertions.then(updatedUser).isPresent();
        BDDAssertions.then(updatedUser.get().getGoalEntity().getId()).isEqualTo(goal1.getId());
    }


    @Test
    void testGetGoalEntityByUsername() {
        GoalEntity foundGoal = userRepository.getGoalEntityByUsername("john_doe");
        BDDAssertions.then(foundGoal).isNotNull();
        BDDAssertions.then(foundGoal).isEqualTo(goal1);
    }

    @Test
    void testGetAllUserByRole() {
        List<UserEntity> users = userRepository.getAllUserByRole(RoleEnum.USER);
        BDDAssertions.then(users).isNotNull();
        BDDAssertions.then(users.size()).isGreaterThan(1);
        BDDAssertions.then(users.stream().map(UserEntity::getUsername))
                .contains("john_doe");
    }

    @Test
    void testDeleteByUsername() {
        userRepository.deleteByUsername("john_doe");
        Optional<UserEntity> deletedUser = userRepository.findByUsername("john_doe");
        BDDAssertions.then(deletedUser).isNotPresent();
    }
}