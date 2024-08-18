package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.GenreEntity;
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

@ActiveProfiles("test")
@SpringBootTest(classes = ThesisApplication.class)
@Transactional
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    private GenreEntity genre1;
    private GenreEntity genre2;

    @BeforeEach
    void setUp() {
        genre1 = genreRepository.getGenreEntityByName("Action").get();
        genre2 = genreRepository.getGenreEntityByName("Drama").get();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindAll() {
        List<GenreEntity> list = genreRepository.findAll();
        BDDAssertions.then(list).isNotEmpty();
        BDDAssertions.then(list.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    void testGetGenreEntityByName() {
        Optional<GenreEntity> foundGenre = genreRepository.getGenreEntityByName("Action");
        BDDAssertions.then(foundGenre).isPresent();
        BDDAssertions.then(foundGenre.get().getTmdbId()).isEqualTo(genre1.getTmdbId());
    }

    @Test
    void testFindByTmdbId() {
        Optional<GenreEntity> foundGenre = genreRepository.findByTmdbId(genre2.getTmdbId());
        BDDAssertions.then(foundGenre).isPresent();
        BDDAssertions.then(foundGenre.get().getName()).isEqualTo(genre2.getName());
    }
}
