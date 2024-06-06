package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.mapper.KeywordMapper;
import com.claudioscagliotti.thesis.model.KeywordEntity;
import com.claudioscagliotti.thesis.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KeywordService {

    private final KeywordRepository keywordRepository;
    private final KeywordMapper keywordMapper;

    public KeywordService(KeywordRepository keywordRepository, KeywordMapper keywordMapper) {
        this.keywordRepository = keywordRepository;
        this.keywordMapper = keywordMapper;
    }

    @Transactional
    public List<KeywordEntity> saveAll(List<KeywordEntity> keywordEntityList) {
        return keywordRepository.saveAll(keywordEntityList);
    }
}
