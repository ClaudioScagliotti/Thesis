package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.model.KeywordEntity;

import java.util.List;

public interface KeywordService {
    /**
     * Saves a list of keyword entities into the database.
     *
     * @param keywordEntityList The list of KeywordEntity objects to be saved.
     * @return The list of saved KeywordEntity objects.
     */
    List<KeywordEntity> saveAll(List<KeywordEntity> keywordEntityList);
}
