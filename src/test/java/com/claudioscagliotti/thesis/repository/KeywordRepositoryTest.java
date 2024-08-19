package com.claudioscagliotti.thesis.repository;

import com.claudioscagliotti.thesis.ThesisApplication;
import com.claudioscagliotti.thesis.model.KeywordEntity;
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
class KeywordRepositoryTest {

    @Autowired
    private KeywordRepository keywordRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        keywordRepository.deleteAll();
    }

    @Test
    void shouldFindAllKeywords() {
        List<KeywordEntity> list = keywordRepository.findAll();
        BDDAssertions.then(list).isNotNull();
        BDDAssertions.then(list.size()).isGreaterThan(0);
    }

    @Test
    void shouldSaveNewKeyword() {
        KeywordEntity newKeyword = new KeywordEntity();
        newKeyword.setName("New Keyword");

        KeywordEntity savedKeyword = keywordRepository.save(newKeyword);

        BDDAssertions.then(savedKeyword.getId()).isNotNull();
        BDDAssertions.then(savedKeyword.getName()).isEqualTo("New Keyword");
    }

    @Test
    void shouldDeleteKeywordById() {
        KeywordEntity newKeyword = new KeywordEntity();
        newKeyword.setName("New Keyword");

        KeywordEntity keywordToDelete = keywordRepository.save(newKeyword);
        int size = keywordRepository.findAll().size();
        keywordRepository.deleteById(keywordToDelete.getId());

        List<KeywordEntity> remainingKeywords = keywordRepository.findAll();
        BDDAssertions.then(remainingKeywords.size()).isEqualTo(size-1);
        BDDAssertions.then(remainingKeywords.contains(keywordToDelete)).isFalse();
    }

    @Test
    void shouldUpdateKeyword() {
        KeywordEntity keywordToUpdate = keywordRepository.findAll().get(0);
        keywordToUpdate.setName("Updated Keyword");

        KeywordEntity updatedKeyword = keywordRepository.save(keywordToUpdate);

        BDDAssertions.then(updatedKeyword.getName()).isEqualTo("Updated Keyword");
    }

    @Test
    void shouldFindKeywordById() {
        KeywordEntity keyword = keywordRepository.findAll().get(0);
        Optional<KeywordEntity> foundKeyword = keywordRepository.findById(keyword.getId());

        BDDAssertions.then(foundKeyword).isPresent();
        BDDAssertions.then(foundKeyword.get().getId()).isEqualTo(keyword.getId());
    }
}
