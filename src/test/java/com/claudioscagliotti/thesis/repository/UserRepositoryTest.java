package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThesisApplication.class)
class UserRepositoryTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindUser() {
        // Trova l'utente per ID
        Optional<User> foundUser = userRepository.findById(7L);

        // Verifica che l'utente sia stato trovato e che i dati corrispondano
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("John");
        assertThat(foundUser.get().getLastName()).isEqualTo("Doe");
        assertThat(foundUser.get().getUsername()).isEqualTo("johndoe");
    }

    @Test
    public void testFindAllUser() {
        // Trova l'utente per ID
        List<User> foundUser = userRepository.findAll();

        // Verifica che l'utente sia stato trovato e che i dati corrispondano
        assertThat((long) foundUser.size()).isEqualTo(3L);
    }

}