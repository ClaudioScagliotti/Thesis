package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.KeywordEntity;
import com.claudioscagliotti.thesis.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing operations related to keywords.
 */
@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;


    /**
     * Constructs a KeywordService with the specified KeywordRepository.
     *
     * @param keywordRepository     The KeywordRepository to interact with KeywordEntity data.

     */
    public KeywordService(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    /**
     * Saves a list of keyword entities into the database.
     *
     * @param keywordEntityList The list of KeywordEntity objects to be saved.
     * @return The list of saved KeywordEntity objects.
     */
    @Transactional
    public List<KeywordEntity> saveAll(List<KeywordEntity> keywordEntityList) {
        return keywordRepository.saveAll(keywordEntityList);
    }

}
